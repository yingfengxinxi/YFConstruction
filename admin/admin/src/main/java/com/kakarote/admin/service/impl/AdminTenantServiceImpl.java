package com.kakarote.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kakarote.admin.common.AdminCodeEnum;
import com.kakarote.admin.common.AdminConst;
import com.kakarote.admin.entity.BO.AdminDeptBO;
import com.kakarote.admin.entity.BO.AdminUserBO;
import com.kakarote.admin.entity.PO.*;
import com.kakarote.admin.entity.VO.AdminDeptVO;
import com.kakarote.admin.entity.VO.AdminUserVO;
import com.kakarote.admin.mapper.AdminDeptMenuMapper;
import com.kakarote.admin.mapper.AdminRoleMenuMapper;
import com.kakarote.admin.service.*;
import com.kakarote.core.common.BaseStatusEnum;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.crm.service.CrmService;
import com.kakarote.core.feign.oa.OaService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminTenantServiceImpl extends BaseServiceImpl<AdminDeptMenuMapper, AdminDeptMenu> implements IAdminTenantService {

    @Autowired
    private IAdminDeptService adminDeptService;
    @Autowired
    private AdminMenuServiceImpl adminMenuService;
    @Autowired
    private AdminUserServiceImpl adminUserService;
    @Autowired
    private IAdminRoleService adminRoleService;
    @Autowired
    private AdminRoleMenuServiceImpl adminRoleMenuService;
    @Autowired
    private IAdminUserRoleService adminUserRoleService;
    @Autowired
    private IAdminUserConfigService adminUserConfigService;
    @Autowired
    private AdminRoleMenuMapper adminRoleMenuMapper;

    @CreateCache(name = Const.ADMIN_USER_NAME_CACHE_NAME, expire = 3, timeUnit = TimeUnit.DAYS,cacheType = CacheType.LOCAL)
    private Cache<Long, String> userCache;

    /**
     * 查询企业下所有用户
     *
     * @param adminUserBO 业务对象
     * @return ids
     */
    @Override
    public BasePage<AdminUserVO> queryUserList(AdminUserBO adminUserBO) {
        BasePage<AdminUserVO> basePage = adminUserService.getBaseMapper().queryUserListByTenantId(adminUserBO.parse(), adminUserBO);
        basePage.getRecords().forEach(adminUserVO -> {
            List<AdminRole> adminRoleList = queryRoleListByUserId(adminUserVO.getUserId(),adminUserBO.getDeptId());
            adminUserVO.setRoleId(adminRoleList.stream().map(adminRole -> adminRole.getRoleId().toString()).collect(Collectors.joining(",")));
            adminUserVO.setRoleName(adminRoleList.stream().map(AdminRole::getRoleName).collect(Collectors.joining(",")));
        });
        return basePage;
    }

    private List<AdminRole> queryRoleListByUserId(Long userId,Integer tenantId) {
        QueryWrapper<AdminUserRole> wrapper = new QueryWrapper<>();
        wrapper.select("role_id");
        wrapper.eq("user_id", userId);
        wrapper.eq("tenant_id", tenantId);
        List<Integer> roleIdList = adminUserRoleService.list(wrapper).stream().map(AdminUserRole::getRoleId).collect(Collectors.toList());
        if (roleIdList.size() > 0) {
            QueryWrapper<AdminRole> wrapper2 = new QueryWrapper<>();
            wrapper2.in("role_id",roleIdList);
            wrapper2.eq("tenant_id", tenantId);
            return adminRoleService.list(wrapper2);
        }
        return new ArrayList<AdminRole>();
    }

    @Override
    public List<AdminDeptVO> queryDeptList() {
        LambdaQueryChainWrapper<AdminDept> chainWrapper =  adminDeptService.lambdaQuery().eq(AdminDept::getPid, 1);
        List<AdminDept> adminDeptList = chainWrapper.list();
        adminDeptList.forEach(record -> {
            LambdaQueryChainWrapper<AdminMenu> chainWrapper2 =  adminMenuService.lambdaQuery().eq(AdminMenu::getTenantId, record.getDeptId())
                    .eq(AdminMenu::getParentId, 0);
            List<AdminMenu> adminMenuList = chainWrapper2.list();
            Map<String, List<Integer>> map = new HashMap<>();
            for(AdminMenu adminMenu : adminMenuList){
                List<Integer> rules = getBaseMapper().getDeptMenu2(adminMenu.getMenuId(), record.getDeptId());
                map.put(adminMenu.getRealm(), rules);
            }
            record.setRules(map);
        });
        return adminDeptList.stream().map(obj -> BeanUtil.copyProperties(obj, AdminDeptVO.class)).collect(Collectors.toList());
    }

    @Override
    public void saveDeptMenu(Integer deptId, List<Integer> menuIdList) {
        List<AdminDeptMenu> adminDeptMenuList = new ArrayList<>();
        menuIdList.forEach(menuId -> {
            AdminDeptMenu adminDeptMenu = new AdminDeptMenu();
            adminDeptMenu.setMenuId(menuId);
            adminDeptMenu.setDeptId(deptId);
            adminDeptMenu.setTenantId(deptId);
            adminDeptMenuList.add(adminDeptMenu);
        });
        saveBatch(adminDeptMenuList, Const.BATCH_SAVE_SIZE);
    }

    /**
     * 修改部门（租户）菜单关系
     *
     * @param adminDept adminDept
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateDeptMenu(AdminDept adminDept) {
        JSONObject object = new JSONObject().fluentPut("dept_id", adminDept.getDeptId()).fluentPut("tenant_id", adminDept.getDeptId());
        removeByMap(object);
        saveDeptMenu(adminDept.getDeptId(), adminDept.getMenuIds());
    }

    /**
     * 根据部门查询权限
     *
     * @return data
     */
    @Override
    public List<AdminDept> getRoleByType() {

        List<AdminDept> recordList = adminDeptService.lambdaQuery().list();
//        recordList.forEach(record -> {
//            LambdaQueryChainWrapper<AdminMenu> chainWrapper =  adminMenuService.lambdaQuery().eq(AdminMenu::getParentId, 0);
//            List<AdminMenu> adminMenuList = chainWrapper.list();
//            Map<String, List<Integer>> map = new HashMap<>();
//            for(AdminMenu adminMenu : adminMenuList){
//                List<Integer> rules = getBaseMapper().getDeptMenu(adminMenu.getMenuId(), record.getDeptId());
//                map.put(adminMenu.getRealm(), rules);
//            }
//            record.setRules(map);
//        });
        return recordList;
    }

    @Override
    public void addTenant(AdminDeptBO adminDeptBO) {
        Integer deptId = addDept(adminDeptBO);
        //2020-12-02追加: pid为1时即为新建租户，需复制必要数据项，根据Menu copyId更新pId;
        // 此处需在GlobalTransactional提交后执行
        System.out.println("===根据Menu copyId更新pId==1="+adminDeptBO.getPid()+"==deptId:"+deptId);
        if(adminDeptBO.getPid().equals(1) && deptId!=0){
            System.out.println("===根据Menu copyId更新pId==2=");
            adminMenuService.getBaseMapper().updatePidForNewTenant(deptId);
        }
    }

    @GlobalTransactional(rollbackFor = Exception.class,timeoutMills = 120000)
    public Integer addDept(AdminDeptBO adminDeptBO) {
        //pid为0的部门只能存在一个
        if (BaseStatusEnum.CLOSE.getStatus().equals(adminDeptBO.getPid())) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        AdminDept adminDept = BeanUtil.copyProperties(adminDeptBO, AdminDept.class);
        log.info( "新增部门-------start---------："+ JSONObject.toJSONString( adminDept ) );
        adminDeptService.save(adminDept);
        log.info( "新增部门--------END----------："+ adminDept.getDeptId() );
        //2020-12-02追加: pid未1时即为新建租户，需复制必要数据项
        if(adminDept.getPid().equals(1)){
            log.info( "新增租户--------START----------");
            copyDataOnNewTenant(adminDept.getDeptId(),adminDept.getName());
            log.info( "新增租户--------END----------");
        }
        return adminDept.getDeptId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTenantAdmin(AdminUserVO adminUser) {
        if (adminUser.getParentId() == null) {
            adminUser.setParentId(0L);
        }
        if (!ReUtil.isMatch(AdminConst.DEFAULT_PASSWORD_INTENSITY, adminUser.getPassword())) {
            throw new CrmException(AdminCodeEnum.ADMIN_PASSWORD_INTENSITY_ERROR);
        }
        //20210105:手机号为登录名,租户内唯一
        Integer count = adminUserService.query().eq("username", adminUser.getUsername()).eq("tenant_id", adminUser.getDeptId()).count();
        if (count > 0) {
            throw new CrmException(AdminCodeEnum.ADMIN_USER_EXIST_ERROR);
        }
        String salt = IdUtil.fastSimpleUUID();
        AdminUser adminUserPO = BeanUtil.copyProperties(adminUser, AdminUser.class);
        adminUserPO.setCreateTime(new Date());
        adminUserPO.setNum(RandomUtil.randomNumbers(15));
        adminUserPO.setMobile(adminUserPO.getUsername());
        adminUserPO.setSalt(salt);
        adminUserPO.setPassword(UserUtil.sign((adminUser.getUsername().trim() + adminUser.getPassword().trim()), salt));
        adminUserPO.setTenantId(adminUser.getDeptId());
        adminUserPO.setStatus(1);//TODO 暂时取消激活机制，添加后直接激活
        adminUserService.save(adminUserPO);
        adminUserConfigService.initUserConfig(adminUserPO.getUserId());
        if(StringUtils.isEmpty(adminUser.getRoleId())){
            //当新增租户管理员时，系统自动创建角色，并赋予租户下全部权限
            AdminRole adminRole = new AdminRole();
            adminRole.setRoleName("租户管理员");
            adminRole.setRoleType(1);
            adminRole.setStatus(1);
            adminRole.setDataType(5);
            adminRole.setIsHidden(1);
            adminRole.setLabel(1);//项目管理员，能够看到所有项目
            adminRole.setTenantId(adminUser.getDeptId());
            LambdaQueryChainWrapper<AdminRole> rList= adminRoleService.lambdaQuery().eq(AdminRole::getRoleName, adminRole.getRoleName())
                    .eq(AdminRole::getRoleType, adminRole.getRoleType())
                    .eq(AdminRole::getTenantId,adminRole.getTenantId());
            if(rList.list().size()>0){
                adminUser.setRoleId(rList.list().get(0).getRoleId()+"");
            }else{
                adminRoleService.save(adminRole);
                adminUser.setRoleId(adminRole.getRoleId()+"");
                //20201215:初始化CRM数据--新建租户管理员
                crmService.initDataForUser(adminUser.getDeptId(),adminUserPO.getUserId().intValue(),adminRole.getRoleId());
            }
            //==========复制租户权限
            QueryWrapper<AdminRoleMenu> wrapper = new QueryWrapper<>();
            wrapper.eq("tenant_id",adminUser.getDeptId());
            wrapper.eq("role_id",Integer.parseInt(adminUser.getRoleId()));
            adminRoleMenuMapper.delete(wrapper);
            try{
                log.warn("============insertFromDept====================，租户ID："+adminUser.getDeptId()+"====RoleId==="+adminUser.getRoleId());
                adminRoleMenuMapper.insertFromDept(Integer.parseInt(adminUser.getRoleId()),adminUser.getDeptId());
                log.warn(">>>>>>>>>>>>insertFromDept<<<<<<<<<<<<<<<<");
            }catch (Exception e){
                e.printStackTrace();
                log.warn("==复制租户管理员RoleMenu表数据失败，租户ID："+adminUser.getDeptId()+"无权限数据");
                throw new ServiceException("复制租户管理员RoleMenu表数据失败，租户ID："+adminUser.getDeptId()+"无权限数据");
            }
            //==========创建用户与角色关系
            AdminUserRole adminUserRole = new AdminUserRole();
            adminUserRole.setRoleId(Integer.parseInt(adminUser.getRoleId()));
            adminUserRole.setUserId(adminUserPO.getUserId());
            adminUserRole.setTenantId(adminUser.getDeptId());
            adminUserRoleService.save(adminUserRole);
        }else{
            adminUserRoleService.saveByUserId(adminUserPO.getUserId(), false, StrUtil.splitTrim(adminUser.getRoleId(), Const.SEPARATOR));
        }
        userCache.put(adminUserPO.getUserId(), adminUserPO.getRealname());
    }

    /**
    * @Description 新建租户时复制必要数据项
    * @Author PangZhen
    * @Date 2020-12-02 13:46
    */
    @Override
    public void copyDataOnNewTenant(Integer tenantId, String name){
        //1.菜单表
//        copyMenu(tenantId,0,0);
        log.info( "复制菜单--------START----------" );
        copyMenu(tenantId);

        log.info( "复制角色--------START----------" );
        //7.项目管理角色及菜单对应关系
        copyWorkRole(tenantId);
        log.info( "复制配置表--------START----------" );
        //2.配置表：admin_config
        ///tenantId = 14882;
        copyConfig(tenantId,name);
        log.info( "复制字典表--------START----------" );

        //5.字典表（记录源ID）
        copyDict(tenantId);
        log.info( "复制仪表盘--------START----------" );

        //6.首页仪表盘数
        copyInstrumentInfo(tenantId);
        log.info( "复制OA--------START----------" );
        //3.日志规则：oa_log_rule、日程类型oa_calendar_type
        //  办公审批：oa_examine_category, oa_examine_field, oa_examine_step
        copyOa(tenantId);
        //4.商机组：crm_business_type、crm_business_status、打印模板：crm_print_template、
        // （注意主子表关联）公海配置： crm_customer_pool,crm_customer_pool_field_setting,crm_customer_pool_field_sort,crm_customer_pool_field_style,crm_customer_pool_rule
        // 自定义字段：crm_field, crm_field_config, crm_field_sort
        // 业务审批：crm_examine, crm_examine_step
        log.info( "复制CRM--------START----------" );
        copyCrm(tenantId);
        log.info( "--------END----------" );
        //TODO 另需考虑新建后同步数据问题
        //throw new RuntimeException("战略异常^_^");
    }

    @Autowired
    private CrmService crmService;
    /**
     * 初始化copyCrm数据
     * @param tenantId
     */
    private boolean copyCrm(Integer tenantId){
        Result result = crmService.initData(tenantId);
        if(result.hasSuccess()){
            return true;
        }else{
            throw new RuntimeException(result.getMsg());
        }
    }
    @Autowired
    private OaService oaService;
    /**
     * 初始化oa数据
     * @param tenantId
     */
    private boolean copyOa(Integer tenantId){
        Result result = oaService.initData(tenantId);
        if(result.hasSuccess()){
            return true;
        }else{
            throw new RuntimeException(result.getMsg());
        }
    }
    @Autowired
    private AdminConfigServiceImpl adminConfigService;
    /**
     * copy admin_config
     * @param tenantId
     */
    private void copyConfig(Integer tenantId, String name){
        List<AdminConfig> list = adminConfigService.list();
        for(AdminConfig adminConfig:list){
            if(adminConfig.getName().equals("companyInfo")){
                adminConfig.setValue("{\"companyName\":\""+name+"\"}");
            }
            adminConfig.setTenantId(tenantId);
        }
        adminConfigService.saveBatch(list);
    }

    @Autowired
    private AdminDictTypeServiceImpl adminDictTypeService;
    @Autowired
    private AdminDictDataServiceImpl adminDictDataService;

    private void copyDict(Integer tenantId){
        List<AdminDictType> dictTypeList = adminDictTypeService.list();
        System.out.println("==dictTypeList.size()=="+dictTypeList.size());
        for(AdminDictType param:dictTypeList){
            param.setTenantId(tenantId.longValue());
            param.setCopyId(param.getDictId());
        }
        adminDictTypeService.saveBatch(dictTypeList);

        List<AdminDictData> dictDataList = adminDictDataService.list();
        System.out.println("==dictDataList.size()=="+dictDataList.size());
        for(AdminDictData param:dictDataList){
            param.setTenantId(tenantId.longValue());
            param.setCopyId(param.getDictCode());
        }
        adminDictDataService.saveBatch(dictDataList);
    }

    private void copyMenu(Integer tenantId) {
        //复制全部数据
        List<AdminMenu> menuList = adminMenuService.list();
        for (AdminMenu param : menuList) {
            param.setTenantId(tenantId);
            param.setCopyId(param.getMenuId());
        }
        adminMenuService.saveBatch(menuList);

        //获取menuList ID集合
        List<Integer> menuIds = adminMenuService.lambdaQuery().eq(AdminMenu::getTenantId,tenantId).list().stream().map(AdminMenu::getMenuId).collect(Collectors.toList());
        log.info( "copyMenu===>menuIds=="+menuIds );
        saveDeptMenu(tenantId,menuIds);
        log.info( "saveDeptMenu==================>END<===================" );
        //根据copyId更新pId==>改为全局事务提交后，在controller中处理
//        adminMenuService.getBaseMapper().updatePidForNewTenant(tenantId);
        //根据copyId更新pId
//        List<AdminMenu> menuListAdd = new ArrayList<>();
//        List<AdminMenu> menuListNew = adminMenuService.lambdaQuery().eq(AdminMenu::getTenantId,tenantId).list();
//        for(AdminMenu m1 : menuListNew){
//            for(AdminMenu m2 : menuListNew){
//                if(m1.getParentId().equals(m2.getCopyId())){
//                    AdminMenu newData = new AdminMenu();
//                    BeanUtil.copyProperties(m1,newData);
//                    newData.setParentId(m2.getMenuId());
//                    menuListAdd.add(newData);
//                    break;
//                }
//            }
//        }
//        adminMenuService.updateBatchById(menuListAdd);
    }
//    private void copyMenu(Integer tenantId, Integer oldPid, Integer newPid){
//        LambdaQueryChainWrapper<AdminMenu> chainWrapper =  adminMenuService.lambdaQuery().eq(AdminMenu::getParentId, oldPid).eq(AdminMenu::getIsDisabled, "0")
//                .eq(AdminMenu::getTenantId, 1) ;
//        if(oldPid==0 && chainWrapper.list().size()==0){
//            throw new RuntimeException("新建租户复制菜单数据失败：未获取到pid=0的菜单");
//        }
//        for(AdminMenu adminMenu : chainWrapper.list()){
//            if(adminMenu.getMenuName().contains("租户")){
//                continue;//租户授权中排除“租户管理”功能
//            }
//            AdminMenu newData = BeanUtil.copyProperties(adminMenu,AdminMenu.class);
//            newData.setMenuId(null);
//            newData.setParentId(newPid);
//            newData.setCopyId(adminMenu.getMenuId());
//            newData.setTenantId(tenantId);
//            adminMenuService.save(newData);
//            copyMenu(tenantId,adminMenu.getMenuId(),newData.getMenuId());
//        }
//    }

    private void copyWorkRole(Integer tenantId) {
        //复制角色数据
        List<AdminRole> list = adminRoleService.lambdaQuery().eq(AdminRole::getRoleType,5).list();
        for (AdminRole param : list) {
            param.setTenantId(tenantId);
        }
        adminRoleService.saveBatch(list);
        //复制新建角色、新建菜单对应关系
        adminRoleMenuService.getBaseMapper().copyWorkRoleMenu(tenantId);

    }
    @Autowired
    private IAdminInstrumentInfoService adminInstrumentInfoService;

    private void copyInstrumentInfo(Integer tenantId){
        List<AdminInstrumentInfo> list = adminInstrumentInfoService.list();
        for(AdminInstrumentInfo adminInstrumentInfo:list){
            adminInstrumentInfo.setTenantId(tenantId);
        }
        adminInstrumentInfoService.saveBatch(list);
    }
}
