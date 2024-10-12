package com.kakarote.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kakarote.admin.common.AdminCodeEnum;
import com.kakarote.admin.common.AdminModuleEnum;
import com.kakarote.admin.common.AdminRoleTypeEnum;
import com.kakarote.admin.entity.PO.*;
import com.kakarote.admin.entity.VO.AdminRoleVO;
import com.kakarote.admin.mapper.AdminMenuMapper;
import com.kakarote.admin.mapper.AdminRoleMapper;
import com.kakarote.admin.service.*;
import com.kakarote.core.common.Const;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@Service
public class AdminRoleServiceImpl extends BaseServiceImpl<AdminRoleMapper, AdminRole> implements IAdminRoleService {

    @Autowired
    private IAdminMenuService adminMenuService;

    @Autowired
    private IAdminConfigService adminConfigService;

    @Autowired
    private IAdminUserRoleService adminUserRoleService;

    @Autowired
    private IAdminRoleMenuService adminRoleMenuService;

    @Autowired
    private IAdminUserService adminUserService;

    @Autowired
    private IAdminRoleService adminRoleService;

    @Autowired
    private AdminMenuMapper adminMenuMapper;

    /**
     * 查询用户所属权限
     *
     * @return obj
     */
    @Override
    public JSONObject auth(Long userId) {
        List<AdminMenu> adminMenus = adminMenuService.queryMenuList(userId);
        List<AdminMenu> menus = adminMenuService.list();
        for (int i = 0; i < menus.size(); i++) {
            if (Objects.equals(0, menus.get(i).getParentId())) {
                adminMenus.add(menus.get(i));
                for (AdminMenu menu : menus) {
                    if (Objects.equals(menu.getParentId(), menus.get(i).getMenuId())) {
                        adminMenus.add(menu);
                    }
                }
            }
        }
        JSONObject jsonObject = createMenu(new HashSet<>(adminMenus), 0);
        /*
           是否有后台权限
         */
        if (ObjectUtil.isEmpty(jsonObject.get("manage"))) {
            jsonObject.remove("manage");
        }
        List<AdminConfig> adminConfigList = adminConfigService.queryConfigListByName((Object[]) AdminModuleEnum.getValues());
        /*
          循环模块配置，把禁用的模块菜单隐藏掉
         */
        adminConfigList.forEach(adminConfig -> {
            //是否开启该模块
            Integer status = adminConfig.getStatus();
            //需要特殊处理的模块
            if (AdminModuleEnum.CALL.getValue().equals(adminConfig.getName())) {
                JSONObject object = jsonObject.getJSONObject(AdminModuleEnum.BI.getValue());
                if (object != null && status != 1) {
                    object.remove(AdminModuleEnum.CALL.getValue());
                }
                return;
            }
            //需要特殊处理的模块
            if (AdminModuleEnum.CARD.getValue().equals(adminConfig.getName())) {
                JSONObject object = jsonObject.getJSONObject(AdminModuleEnum.CRM.getValue());
                if (object != null && status != 1) {
                    object.remove("card");
                }
                return;
            }
            //需要特殊处理的模块
            List<String> oaArray = Arrays.asList(AdminModuleEnum.TASK_EXAMINE.getValue(), AdminModuleEnum.LOG.getValue(), AdminModuleEnum.BOOK.getValue(), AdminModuleEnum.CALENDAR.getValue());
            if (oaArray.contains(adminConfig.getName())) {
                if (!jsonObject.containsKey(AdminModuleEnum.OA.getValue())) {
                    jsonObject.put(AdminModuleEnum.OA.getValue(), new JSONObject());
                }
                JSONObject object = jsonObject.getJSONObject(AdminModuleEnum.OA.getValue());
                if (status == 0) {
                    object.remove(adminConfig.getName());
                } else {
                    if (!AdminModuleEnum.BOOK.getValue().equals(adminConfig.getName())) {
                        object.put(adminConfig.getName(), new JSONObject());
                    }
                }
                return;
            }
            if (Objects.equals(0, adminConfig.getStatus())) {
                jsonObject.remove(adminConfig.getName());
            } else {
                if (!jsonObject.containsKey(adminConfig.getName())) {
                    jsonObject.put(adminConfig.getName(), new JSONObject());
                }
            }
        });

        //为crm模块根据用户权限添加公海权限
        if (jsonObject.containsKey(AdminModuleEnum.CRM.getValue())) {
            JSONObject authObject = new JSONObject();
            UserInfo userInfo = UserUtil.getUser();
            Map<String, Long> read = adminMenuService.queryPoolReadAuth(userInfo.getUserId(), userInfo.getDeptId());
            if (UserUtil.isAdmin() || read.get("adminNum") > 0 || read.get("userNum") > 0) {
                authObject.fluentPut("index", true).fluentPut("receive", true);
                if (UserUtil.isAdmin() || read.get("adminNum") > 0) {
                    authObject.fluentPut("distribute", true).fluentPut("excelexport", true).fluentPut("delete", true);
                }
            }
            jsonObject.getJSONObject(AdminModuleEnum.CRM.getValue()).put("pool", authObject);
        }
        jsonObject.remove("work");
        //TODO 暂时先去掉名片小程序
        if (jsonObject.containsKey("manage") && jsonObject.getJSONObject("manage").containsKey("card")){
            JSONObject manage = jsonObject.getJSONObject("manage");
            manage.remove("card");
        }
        return jsonObject;
    }

    /**
     * 查询用户所属权限
     *
     * @param userIds ids
     */
    @Override
    public void authInvalidate(List<Long> userIds) {

    }

    /**
     * 通过用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return data
     */
    @Override
    public List<AdminRole> queryRoleListByUserId(Long userId) {
        QueryWrapper<AdminUserRole> wrapper = new QueryWrapper<>();
        wrapper.select("role_id");
        wrapper.eq("user_id", userId);
        List<Integer> roleIdList = adminUserRoleService.list(wrapper).stream().map(AdminUserRole::getRoleId).collect(Collectors.toList());
        if (roleIdList.size() > 0) {
            return listByIds(roleIdList);
        }
        return new ArrayList<AdminRole>();

    }

    /**
     * 根据类型查询角色
     *
     * @param roleTypeEnum type
     * @return data
     */
    @Override
    public List<AdminRole> getRoleByType(AdminRoleTypeEnum roleTypeEnum) {
        List<AdminRole> recordList = lambdaQuery().eq(AdminRole::getRoleType, roleTypeEnum.getType()).list();
        String realm;
        switch (roleTypeEnum) {
            case CUSTOM:
                realm = "manage";
                break;
            case MANAGER:
                realm = "manage";
                break;
            case CUSTOMER_MANAGER:
                realm = "crm";
                break;
//            case OA:
//                realm = "oa";
//                break;
            case PROJECT:
                realm = "project";
                break;
            case HRM:
                realm = "hrm";
                break;
            case JXC:
                realm = "jxc";
                break;
            default:
                return new ArrayList<>();
        }
        LambdaQueryWrapper<AdminMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(AdminMenu::getMenuId);
        wrapper.eq(AdminMenu::getParentId, 0);
        wrapper.eq(AdminMenu::getRealm, realm);
        if(adminMenuService.getOne(wrapper)!=null){
            Integer pid = adminMenuService.getOne(wrapper).getMenuId();
            recordList.forEach(record -> {
                Map<String, List<Integer>> map = new HashMap<>();
                List<Integer> data = getBaseMapper().getRoleMenu(pid, record.getRoleId());
                List<Integer> bi = getBaseMapper().getRoleMenu(2, record.getRoleId());
                map.put("data", data);
                map.put("bi", bi);
                record.setRules(map);
            });
        }
        return recordList;
    }

    @Override
    public List<AdminRole> getRoleById() {
        List<AdminRole> recordList = lambdaQuery().eq(AdminRole::getTenantId, UserUtil.getUser().getTenantId())
                .ne(AdminRole::getRoleType,5).ne(AdminRole::getRoleType,6).list();//排除项目管理中新建的角色
        recordList.forEach(record -> {
            List<AdminMenu> adminMenuList = adminMenuService.query().eq("tenant_id", UserUtil.getUser().getTenantId())
                    .eq("parent_id", 0).list();
            Map<String, List<Integer>> map = new HashMap<>();
            for(AdminMenu adminMenu : adminMenuList){
                List<Integer> rules = recursionRoleMenu(adminMenu.getMenuId(), record.getRoleId());
                map.put(adminMenu.getRealm(), rules);
            }
            record.setRules(map);
        });
        return recordList;
    }
    @Override
    public List<AdminRole> getRoleByIdForRoleAuth() {
        List<AdminRole> recordList = lambdaQuery().eq(AdminRole::getTenantId, UserUtil.getUser().getTenantId())
                .ne(AdminRole::getRoleType,5).ne(AdminRole::getRoleType,6).list();//排除项目管理中新建的角色
        recordList.forEach(record -> {
            List<AdminMenu> adminMenuList = adminMenuService.query().eq("tenant_id", UserUtil.getUser().getTenantId())
                    .eq("parent_id", 0).list();
            Map<String, List<Integer>> map = new HashMap<>();
            List<Integer> rulesTop = new ArrayList();
            for(AdminMenu adminMenu : adminMenuList){
                List<Integer> rules = recursionRoleMenu(adminMenu.getMenuId(), record.getRoleId());
                if(adminMenu.getMenuType()==1||adminMenu.getMenuType()==5) {
                    map.put(adminMenu.getRealm(), rules);
                } else{
                    List<Integer> list = getBaseMapper().getRoleMenuByMenuId(adminMenu.getMenuId(),record.getRoleId());
                    if(list.size()>0){
                        rulesTop.add(adminMenu.getMenuId());
                        rulesTop.addAll(rules);
                    }
                }
            }
            //角色权限管理中不能展示一级页面的菜单，故将所有菜单加入
            if(rulesTop.size()>0){
                map.put("top", rulesTop);
            }
            record.setRules(map);
        });
        return recordList;
    }
    public List<Integer> recursionRoleMenu(Integer parentId,Integer roleId){
        List<Integer> list = new ArrayList<>();
        list = getBaseMapper().getRoleMenuByPId(parentId,roleId);
        if(list!= null){
            List<Integer> list2 = new ArrayList<>();
            for(Integer integer:list){
                list2.addAll(recursionRoleMenu(integer,roleId));
            }
            list.addAll(list2);
        }
        List<Integer> res = new ArrayList<>(new HashSet<>(list));
        return res;
    }

    /**
     * 查询角色
     *
     * @return data
     */
    @Override
    public List<AdminRole> getRoleList() {
        List<AdminRole> recordList = lambdaQuery().list();
        return recordList;
    }
    /**
     * 查询全部角色
     *
     * @return data
     */
    @Override
    public List<AdminRoleVO> getAllRoleList() {
        List<AdminRoleVO> records = new ArrayList<>();
        for (AdminRoleTypeEnum typeEnum : AdminRoleTypeEnum.values()) {
            if (Arrays.asList(3, 4, 5).contains(typeEnum.getType())) {
                continue;
            }
            AdminRoleVO record = new AdminRoleVO();
            record.setName(roleTypeCaseName(typeEnum.getType()));
            record.setPid(typeEnum.getType());
            List<AdminRole> recordList = getRoleByType(typeEnum);
            record.setList(recordList);
            records.add(record);
        }
        return records;
    }

    @Override
    public Integer queryDataType(Long userId, Integer menuId) {
        List<Integer> list = getBaseMapper().queryDataType(userId, menuId);
        return (list != null && list.size() > 0) ? list.get(0) : null;
    }

    @Override
    public Integer queryDataTypeByRealm(Long userId, String realm) {
        List<Integer> list = getBaseMapper().queryDataTypeByRealm(userId, realm);
        return (list != null && list.size() > 0) ? list.get(0) : null;
    }

    @Override
    public Integer queryMaxDataType(Long userId, Integer menuId) {
        List<Integer> list = getBaseMapper().queryMaxDataType(userId, menuId);
        return (list != null && list.size() > 0) ? list.get(0) : null;
    }

    @Override
    public List<Long> queryUserByAuth(Long userId, String realm) {
        if (ObjectUtil.equal("return_visit", realm)) {
            realm = "visit";
        }
        List<Long> adminUsers = new ArrayList<>();
        //查询用户数据权限，从高到低排序
        List<Integer> list = getBaseMapper().queryDataType(userId, null);
        if (list.size() == 0) {
            //无权限查询自己的数据
            adminUsers.add(userId);
            return adminUsers;
        }
        List<AdminMenu> userRoleList = getBaseMapper().queryUserRoleListByUserId(userId, realm);
        if (list.size() == 1 && userRoleList.size() == 1) {
            //如果为1的话 验证是否有最高权限，否则及有多个权限
            //拥有最高数据权限
            if (list.contains(5)) {
                return adminUserService.list().stream().map(AdminUser::getUserId).collect(Collectors.toList());
            } else {
                AdminUser adminUser = adminUserService.getById(userId);
                if (list.contains(4)) {
                    List<Integer> deptIds = ApplicationContextHolder.getBean(IAdminDeptService.class).queryChildDept(adminUser.getDeptId());
                    deptIds.add(adminUser.getDeptId());
                    List<Long> longList = adminUserService.queryUserByDeptIds(deptIds);
                    adminUsers.addAll(longList);
                } else if (list.contains(3)) {
                    List<Long> longList = adminUserService.queryUserByDeptIds(Collections.singletonList(adminUser.getDeptId()));
                    adminUsers.addAll(longList);
                }

                if (list.contains(2)) {
                    adminUsers.addAll(adminUserService.queryChildUserId(adminUser.getUserId()));
                }
                adminUsers.add(adminUser.getUserId());
            }
        } else {
            if (StrUtil.isNotEmpty(realm)) {
                AdminUser adminUser = adminUserService.getById(userId);
                for (AdminMenu adminMenu : userRoleList) {//如果有多个权限 验证当前用户是否对当前管理 是否为本人操作
                    if (adminMenu.getRealm().equals(realm) && adminMenu.getDataType() == 1) {//当前操作的管理链接地址
                        adminUsers.add(userId);
                        HashSet<Long> hashSet = new HashSet<>(adminUsers);
                        adminUsers.clear();
                        adminUsers.addAll(hashSet);
                        return adminUsers;
                    } else if (adminMenu.getRealm().equals(realm) && adminMenu.getDataType() == 2) {//本人及其
                        adminUsers.addAll(adminUserService.queryChildUserId(adminUser.getUserId()));
                        adminUsers.add(userId);
                        HashSet<Long> hashSet = new HashSet<>(adminUsers);
                        adminUsers.clear();
                        adminUsers.addAll(hashSet);
                        return adminUsers;
                    } else if (adminMenu.getRealm().equals(realm) && adminMenu.getDataType() == 3) {//本部门
                        List<Long> longList = adminUserService.queryUserByDeptIds(Collections.singletonList(adminUser.getDeptId()));
                        adminUsers.addAll(longList);
                        return adminUsers;
                    } else if (adminMenu.getRealm().equals(realm) && adminMenu.getDataType() == 4) {//本部门及下属部门
                        List<Integer> deptIds = ApplicationContextHolder.getBean(IAdminDeptService.class).queryChildDept(adminUser.getDeptId());
                        deptIds.add(adminUser.getDeptId());
                        List<Long> longList = adminUserService.queryUserByDeptIds(deptIds);
                        adminUsers.addAll(longList);
                        return adminUsers;
                    } else if (adminMenu.getRealm().equals(realm) && adminMenu.getDataType() == 5) {//全部
                        return adminUserService.list().stream().map(AdminUser::getUserId).collect(Collectors.toList());
                    }
                }
            } else {
                if (list.contains(5)) {
                    return adminUserService.list().stream().map(AdminUser::getUserId).collect(Collectors.toList());
                } else {
                    AdminUser adminUser = adminUserService.getById(userId);
                    if (list.contains(4)) {
                        List<Integer> deptIds = ApplicationContextHolder.getBean(IAdminDeptService.class).queryChildDept(adminUser.getDeptId());
                        deptIds.add(adminUser.getDeptId());
                        List<Long> longList = adminUserService.queryUserByDeptIds(deptIds);
                        adminUsers.addAll(longList);
                    } else if (list.contains(3)) {
                        List<Long> longList = adminUserService.queryUserByDeptIds(Collections.singletonList(adminUser.getDeptId()));
                        adminUsers.addAll(longList);
                    }

                    if (list.contains(2)) {
                        adminUsers.addAll(adminUserService.queryChildUserId(adminUser.getUserId()));
                    }
                    adminUsers.add(adminUser.getUserId());
                }
            }
        }

        adminUsers.add(userId);
        HashSet<Long> hashSet = new HashSet<>(adminUsers);
        adminUsers.clear();
        adminUsers.addAll(hashSet);
        return adminUsers;
    }

    /**
     * 保存角色
     *
     * @param adminRole role
     */
    @Override
    public void add(AdminRole adminRole) {
        Integer count = lambdaQuery().eq(AdminRole::getRoleName, adminRole.getRoleName()).eq(AdminRole::getRoleType, adminRole.getRoleType()).count();
        if (count > 0) {
            throw new CrmException(AdminCodeEnum.ADMIN_ROLE_NAME_EXIST_ERROR);
        }
        if (adminRole.getRoleId() != null) {
            updateById(adminRole);
        } else {
            adminRole.setRoleId(null);
            adminRole.setStatus(1);
            adminRole.setRemark(null);
            save(adminRole);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer roleId) {
        removeById(roleId);
        JSONObject object = new JSONObject().fluentPut("role_id", roleId);
        adminUserRoleService.removeByMap(object);
        adminRoleMenuService.removeByMap(object);
    }

    /**
     * @author zhangzhiwei
     * 复制
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void copy(Integer roleId) {
        AdminRole adminRole = getById(roleId);
        LambdaQueryWrapper<AdminRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRoleMenu::getRoleId, roleId);
        List<Integer> menuIdsList = adminRoleMenuService.listObjs(wrapper, obj -> Integer.valueOf(obj.toString()));
        String roleName = adminRole.getRoleName().trim();
        String pre = ReUtil.delFirst("[(]\\d+[)]$", roleName);
        List<AdminRole> adminRoleList;
        if (!ReUtil.contains("^[(]\\d+[)]$", roleName)) {
            adminRoleList = lambdaQuery().likeRight(AdminRole::getRoleName, pre).list();
        } else {
            adminRoleList = lambdaQuery().last(" role_name regexp '^[(]\\d+[)]$'").list();
        }
        StringBuilder numberSb = new StringBuilder();
        for (AdminRole dbAdminRole : adminRoleList) {
            String endCode = ReUtil.get("[(]\\d+[)]$", dbAdminRole.getRoleName(), 0);
            if (endCode != null) {
                numberSb.append(endCode);
            }
        }
        int i = 1;
        if (numberSb.length() == 0) {
            while (numberSb.toString().contains("(" + i + ")")) {
                i++;
            }
        }
        adminRole.setRoleName(pre + "(" + i + ")");
        adminRole.setRoleId(null);
        save(adminRole);
        adminRoleMenuService.saveRoleMenu(adminRole.getRoleId(), menuIdsList);
    }

    /**
     * 用户关联角色
     *
     * @param userIds 用户列表
     * @param roleIds 角色列表
     */
    @Override
    public void relatedUser(List<String> userIds, List<String> roleIds) {
        List<AdminUserRole> adminUserRoleList = new ArrayList<>();
        for (String userId : userIds) {
            for (String roleId : roleIds) {
                Integer count = adminUserRoleService.lambdaQuery().eq(AdminUserRole::getRoleId, roleId).eq(AdminUserRole::getUserId, userId).count();
                if (count == 0) {
                    AdminUserRole userRole = new AdminUserRole();
                    userRole.setUserId(Long.valueOf(userId));
                    userRole.setRoleId(Integer.valueOf(roleId));
                    adminUserRoleList.add(userRole);
                }
            }
        }
        adminUserRoleService.saveBatch(adminUserRoleList, Const.BATCH_SAVE_SIZE);
    }

    /**
     * 取消用户关联角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    @Override
    public void unbindingUser(Long userId, Integer roleId) {
        adminUserRoleService.lambdaUpdate().eq(AdminUserRole::getRoleId,roleId).eq(AdminUserRole::getUserId,userId).remove();
    }

    /**
     * 修改角色菜单关系
     *
     * @param adminRole adminrole
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRoleMenu(AdminRole adminRole) {
        updateById(adminRole);
        JSONObject object = new JSONObject().fluentPut("role_id", adminRole.getRoleId());
        adminRoleMenuService.removeByMap(object);
        adminRoleMenuService.saveRoleMenu(adminRole.getRoleId(), adminRole.getMenuIds());
    }

    /**
     * 查询项目管理的角色
     *
     * @param label label
     * @return roleId
     */
    @Override
    public Integer queryWorkRole(Integer label) {
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(AdminRole::getRoleId);
        if (label == null) {
            wrapper.eq(AdminRole::getRemark, "project");
        } else {
            wrapper.eq(AdminRole::getLabel, label);
        }
        AdminRole adminRole = getOne(wrapper);
        return adminRole != null ? adminRole.getRoleId() : 1;
    }

    /**
     * 保存项目管理角色
     *
     * @param object obj
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setWorkRole(JSONObject object) {
        Integer roleId = object.getInteger("roleId");
        String roleName = object.getString("roleName");
        String remark = object.getString("remark");
        JSONArray menuIds = object.getJSONArray("menuIds");
        AdminRole adminRole;
        if (roleId == null) {
            adminRole = new AdminRole();
            adminRole.setRoleName(roleName);
            adminRole.setRemark(remark);
            adminRole.setRoleType(6);
            save(adminRole);
        } else {
            adminRole = getById(roleId);
            adminRole.setRoleName(roleName);
            adminRole.setRemark(remark);
            adminRole.setRoleId(roleId);
            updateById(adminRole);
            adminRoleMenuService.removeByMap(new JSONObject().fluentPut("role_id", roleId));
        }
        adminRoleMenuService.saveRoleMenu(adminRole.getRoleId(), menuIds.toJavaList(Integer.class));
    }

    /**
     * 删除项目管理角色
     *
     * @param roleId roleId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWorkRole(Integer roleId) {
        removeById(roleId);
        adminRoleMenuService.removeByMap(new JSONObject().fluentPut("role_id",roleId));
        getBaseMapper().deleteWorkRole(queryWorkRole(3),roleId);
    }

    /**
     * 查询项目管理角色
     *
     * @return
     */
    @Override
    public List<AdminRole> queryProjectRoleList() {
        List<AdminRole> roleList = lambdaQuery().in(AdminRole::getRoleType, Arrays.asList(5, 6)).eq(AdminRole::getIsHidden, 1).list();
        roleList.forEach(record -> {
            LambdaQueryChainWrapper<AdminRoleMenu> chainWrapper = adminRoleMenuService.lambdaQuery().select(AdminRoleMenu::getMenuId).eq(AdminRoleMenu::getRoleId, record.getRoleId());
            List<Integer> rules = chainWrapper.list().stream().map(AdminRoleMenu::getMenuId).collect(Collectors.toList());
            record.setMenuIds(rules);
        });
        return roleList;
    }


    /**
     * 角色类型转换名称
     *
     * @param type 类型
     * @return 角色名称
     */
    private String roleTypeCaseName(Integer type) {
        String name;
        switch (type) {
            case 1:
                name = "系统管理角色";
                break;
            case 2:
                name = "客户管理角色";
                break;
            case 7:
                name = "办公管理角色";
                break;
            case 8:
                name = "项目管理角色";
                break;
            case 9:
                name = "人力资源管理角色";
                break;
            case 10:
                name = "进销存管理角色";
                break;
            default:
                name = "自定义角色";
        }
        return name;
    }

    private JSONObject createMenu(Set<AdminMenu> adminMenuList, Integer parentId) {
        JSONObject jsonObject = new JSONObject();
        adminMenuList.forEach(adminMenu -> {
            if (Objects.equals(parentId, adminMenu.getParentId())) {
                if (Objects.equals(1, adminMenu.getMenuType())) {
                    JSONObject object = createMenu(adminMenuList, adminMenu.getMenuId());
                    if (!object.isEmpty()) {
                        jsonObject.put(adminMenu.getRealm(), object);
                    }
                } else {
                    jsonObject.put(adminMenu.getRealm(), Boolean.TRUE);
                }
            }
        });
        return jsonObject;
    }

    @Override
    public List<AdminRole> queryRoleList() {
        return  lambdaQuery().eq(AdminRole::getStatus,1).in(AdminRole::getRoleType,Arrays.asList(5,6)).list();

    }

    @Override
    public List<String> queryNoAuthMenu(Long userId) {
        UserInfo user = UserUtil.getUser();
        Long superUserId = user.getSuperUserId();
        Integer superRoleId = user.getSuperRoleId();
        List<Integer> roles = adminUserService.queryUserRoleIds(userId);
        List<String> noAuthMenuUrls = new ArrayList<>();
        if (userId.equals(superUserId) || roles.contains(superRoleId)) {
            return noAuthMenuUrls;
        }

        List<AdminMenu> adminMenus = adminMenuMapper.queryMenuList(userId);
        if (adminMenus.isEmpty()){
            noAuthMenuUrls.add("/*/**");
            return noAuthMenuUrls;
        }

        List<Integer> menuIdList = adminMenus.stream().map(AdminMenu::getMenuId).collect(Collectors.toList());
        LambdaQueryWrapper<AdminMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //非空
        lambdaQueryWrapper.notIn(AdminMenu::getMenuId,menuIdList);
        lambdaQueryWrapper.ne(AdminMenu::getTenantId,0);
        List<AdminMenu> noAuthMenus = adminMenuService.list(lambdaQueryWrapper);
        noAuthMenus.removeIf(node -> StringUtils.isEmpty(node.getRealmUrl()));
        if(!noAuthMenus.isEmpty()){
            return noAuthMenus.stream().map(AdminMenu::getRealmUrl).collect(Collectors.toList());
        }
        return noAuthMenuUrls;
    }


}
