package com.kakarote.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kakarote.admin.common.AdminRoleTypeEnum;
import com.kakarote.admin.entity.PO.AdminDeptMenu;
import com.kakarote.admin.entity.PO.AdminMenu;
import com.kakarote.admin.entity.VO.AdminMenuVO;
import com.kakarote.admin.mapper.AdminDeptMenuMapper;
import com.kakarote.admin.mapper.AdminMenuMapper;
import com.kakarote.admin.service.IAdminMenuService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.RecursionUtil;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 后台菜单表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@Service
public class AdminMenuServiceImpl extends BaseServiceImpl<AdminMenuMapper, AdminMenu> implements IAdminMenuService {

    @Autowired
    private AdminDeptMenuMapper adminDeptMenuMapper;

    /**
     * 登录：查询用户所拥有的菜单权限
     *如果为管理员，则获取所有菜单列表
     * @param userInfo 用户
     * @return 菜单权限的并集
     */
    @Override
    public List<AdminMenu> queryMenuListForLogin(UserInfo userInfo) {
        //如果是管理员
        if (userInfo.getUserId().equals(userInfo.getSuperUserId()) || userInfo.getRoles().contains(userInfo.getSuperRoleId())) {
            return query().ne("menu_type",5).eq("status",1).eq("tenant_id",userInfo.getTenantId())
                    .orderByAsc("parent_id").orderByAsc("sort").list();
        }
        return getBaseMapper().queryMenuList(userInfo.getUserId());
    }
    @Override
    public List<AdminMenu> queryMenuListForAppLogin(UserInfo userInfo) {
        //如果是管理员
        if (userInfo.getUserId().equals(userInfo.getSuperUserId()) || userInfo.getRoles().contains(userInfo.getSuperRoleId())) {
            return query().eq("menu_type",5).eq("status",1).eq("tenant_id",userInfo.getTenantId())
                    .orderByAsc("parent_id").orderByAsc("sort").list();
        }
        return getBaseMapper().queryAppMenuList(userInfo.getUserId());
    }
    /**
     * 查询用户所拥有的菜单权限
     *如果为管理员，则获取所有菜单列表
     * @param userId 用户列表
     * @return 菜单权限的并集
     */
    @Override
    public List<AdminMenu> queryMenuList(Long userId) {
        if (UserUtil.isAdmin()) {
            return query().eq("status",1).list();
        }
        return getBaseMapper().queryMenuList(userId);
    }

    /**
     * 查询公海菜单权限
     *
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return data
     */
    @Override
    public Map<String, Long> queryPoolReadAuth(Long userId, Integer deptId) {
        return getBaseMapper().queryPoolReadAuth(userId, deptId);
    }

    /**
     * 根据类型查询菜单
     * @param typeEnum type
     * @return data
     */
    @Override
    public JSONObject getMenuListByType(AdminRoleTypeEnum typeEnum) {
        JSONObject object = new JSONObject();
        String realm;
        switch (typeEnum) {
            case MANAGER:
                realm = "manage";
                break;
            case CUSTOMER_MANAGER: {
                realm = "crm";
                AdminMenuVO bi = queryMenuListByRealm("bi");
                List<AdminMenuVO> biList = getMenuList(UserUtil.getUser().getTenantId().intValue(),bi.getMenuId(), "oa");
                bi.setChildMenu(biList);
                object.put("bi", bi);
                break;
            }
            case WORK:
                realm = "work";
                break;
//            case OA: {
//                realm = "oa";
//                AdminMenuVO bi = queryMenuListByRealm("bi");
//                String[] realmArr = new String[]{"achievement", "business", "customer", "contract", "product", "portrait", "ranking", "call"};
//                List<AdminMenuVO> biList = getMenuList(bi.getMenuId(),realmArr);
//                bi.setChildMenu(biList);
//                object.put("bi", bi);
//                break;
//            }
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
                realm = "";
                break;
        }
        AdminMenuVO data = queryMenuListByRealm(realm);
        data.setChildMenu(getMenuList(UserUtil.getUser().getTenantId().intValue(),data.getMenuId()));
        object.put("data",data);
        return object;
    }

    /**
     * 通过parentId和realm查询菜单
     *
     * @return 菜单列表
     * @author zhangzhiwei
     */
    private AdminMenuVO queryMenuListByRealm(String realm) {
        AdminMenu adminMenu = lambdaQuery().eq(AdminMenu::getParentId, 0).eq(AdminMenu::getRealm, realm).one();
        return BeanUtil.copyProperties(adminMenu, AdminMenuVO.class);
    }

    private List<AdminMenuVO> getMenuList(Integer tenantId, Integer parentId, String... notRealm) {
        LambdaQueryChainWrapper<AdminMenu> chainWrapper = lambdaQuery().eq(AdminMenu::getTenantId, tenantId).eq(AdminMenu::getStatus,1).orderByAsc(AdminMenu::getSort);
        if (notRealm.length > 0) {
            chainWrapper.notIn(AdminMenu::getRealm, Arrays.asList(notRealm));
        }
        List<AdminMenu> list = chainWrapper.list();
        return RecursionUtil.getChildListTree(list, "parentId", parentId, "menuId", "childMenu", AdminMenuVO.class);
    }

    @Override
    public Integer queryMenuId(String realm1, String realm2, String realm3) {
        return getBaseMapper().queryMenuId(realm1,realm2,realm3);
    }

    /**
     * 根据上级查询菜单列表
     * @param parentId
     * @return
     */
    public List<AdminMenu> queryByParentId(Integer parentId){
        List<AdminMenu> list = getBaseMapper().queryByParentId(parentId);
        return list;
    }
    /**
     * 租户：根据上级查询菜单列表
     * @param parentId
     * @return
     */
    public List<AdminMenu> queryByParentIdTenant(Integer parentId){
        List<AdminMenu> list = getBaseMapper().queryByParentIdTenant(parentId);
        return list;
    }
    @Override
    public int saveMenu(AdminMenu adminMenu){
        int i = getBaseMapper().insert(adminMenu);

        if (i > 0) {
            // 插入租户菜单
            AdminDeptMenu adminDeptMenu = new AdminDeptMenu();
            adminDeptMenu.setMenuId(adminMenu.getMenuId());
            adminDeptMenu.setDeptId(Integer.valueOf(String.valueOf(UserUtil.getUser().getTenantId())));
            adminDeptMenu.setTenantId(Integer.valueOf(String.valueOf(UserUtil.getUser().getTenantId())));
            adminDeptMenuMapper.insert(adminDeptMenu);
    }
        return i;
    }

    @Override
    public int updateMenu(AdminMenu adminMenu) {
        return getBaseMapper().updateById(adminMenu);
    }

    @Override
    public Result deleteMenu(String menuId) {
        //TODO 存在下级不得删除
        QueryWrapper<AdminMenu> qw = new QueryWrapper<>();
        qw.eq("parent_id", menuId);
        List<AdminMenu> list = getBaseMapper().selectList(qw);
        if(list.size()>0){
            return Result.error(-1,"存在下级菜单，不得删除");
        }
        getBaseMapper().deleteById(menuId);
        // 删除租户菜单
        Map<String,Object> adminDeptMenu = new HashMap<>();
        adminDeptMenu.put("menu_id", menuId);
        adminDeptMenu.put("tenant_id", UserUtil.getUser().getTenantId());
        adminDeptMenuMapper.deleteByMap(adminDeptMenu);
        return Result.ok();
    }

    @Override
    public Result getMenuById(Integer menuId){
        AdminMenu adminMenu = getBaseMapper().getMenuById(menuId);
        return Result.ok(adminMenu);
    }

    @Override
    public List<AdminMenu> queryHeaderMenuList(Long userId){
        UserInfo userInfo = UserUtil.getUser();
        //如果是管理员
        if (userInfo.getUserId().equals(userInfo.getSuperUserId()) || userInfo.getRoles().contains(userInfo.getSuperRoleId())) {
            return getBaseMapper().queryAdminHeaderMenuList(userId);
        }
        return getBaseMapper().queryHeaderMenuList(userId);
    }

    @Override
    public List<AdminMenu> queryAllAppList(){
        UserInfo userInfo = UserUtil.getUser();
        //如果是管理员
        if (userInfo.getUserId().equals(userInfo.getSuperUserId()) || userInfo.getRoles().contains(userInfo.getSuperRoleId())) {
            return getBaseMapper().queryAdminAllAppList();
        }
        return getBaseMapper().queryAllAppList(userInfo.getUserId());
    }

    /**
     * 租户：应用管理
     * @return
     */
    @Override
    public List<AdminMenu> queryAppList(){
        return getBaseMapper().queryAppList(UserUtil.getUserId());
    }
    /**
     * 租户：
     * @return
     */
    @Override
    public List<AdminMenu> queryTenantAppList(){
        UserInfo userInfo = UserUtil.getUser();
        QueryWrapper<AdminMenu> qw = new QueryWrapper<>();
        qw.eq("parent_id", 0);
        qw.eq("tenant_id", 1);
        qw.orderByAsc("sort");
        List<AdminMenu> list = getBaseMapper().selectList(qw);
        return list;
    }

    /**
     * 查询全部菜单
     * @return data
     */
    @Override
    public JSONObject getAllMenuList(Integer tenantId) {
        JSONObject object = new JSONObject();
        LambdaQueryChainWrapper<AdminMenu> chainWrapper = lambdaQuery().eq(AdminMenu::getTenantId, tenantId).eq(AdminMenu::getParentId, 0).eq(AdminMenu::getStatus, 1).orderByAsc(AdminMenu::getSort);
        List<AdminMenu> adminMenuList = chainWrapper.list();
        List<AdminMenuVO> allList = new ArrayList<>();
        //角色权限管理中不能展示一级页面的菜单，故将所有菜单加入
        List<AdminMenuVO> topList = new ArrayList<>();
        for(AdminMenu adminMenu : adminMenuList){
//            if(adminMenu.getMenuName().contains("租户")){
//                continue;//租户授权中排除“租户管理”功能
//            }
            AdminMenuVO data = BeanUtil.copyProperties(adminMenu, AdminMenuVO.class);
            List<AdminMenuVO> childMenu = getMenuList(tenantId,data.getMenuId());
            if(childMenu.size()>0){
                data.setChildMenu(childMenu);
            }
            if(data.getMenuType()==1||data.getMenuType()==5){
                allList.add(data);
            }
            else{
                topList.add(data);
            }
        }
        AdminMenuVO top = new  AdminMenuVO();
        top.setMenuId(0);
        top.setMenuName("一级菜单");
        top.setChildMenu(topList);
        top.setMenuType(1);
        top.setRealm("top");
        allList.add(0,top);
        object.put("all",allList);
        return object;
    }

    @Override
    public JSONObject getMenuListByTenant() {
        if(UserUtil.getUser().getTenantId()==1L){
            return getAllMenuList(1);//顶级租户返回全部菜单
        }
        JSONObject object = new JSONObject();
        LambdaQueryChainWrapper<AdminMenu> chainWrapper = lambdaQuery().eq(AdminMenu::getParentId, 0).eq(AdminMenu::getStatus, 1).orderByAsc(AdminMenu::getSort);
        List<AdminMenu> adminMenuList = chainWrapper.list();
        List<AdminMenuVO> allList = new ArrayList<>();
        //角色权限管理中不能展示一级页面的菜单，故将所有菜单加入
        List<AdminMenuVO> topList = new ArrayList<>();
        for(AdminMenu adminMenu : adminMenuList){
            AdminMenuVO data = BeanUtil.copyProperties(adminMenu, AdminMenuVO.class);
            List<AdminMenu> list =  getBaseMapper().getMenuListByTenant(UserUtil.getUser().getTenantId());
            List<AdminMenuVO> childMenu = RecursionUtil.getChildListTree(list,
                    "parentId", data.getMenuId(), "menuId", "childMenu", AdminMenuVO.class);
            if(childMenu.size()>0){
                data.setChildMenu(childMenu);
            }
            if(data.getMenuType()==1||data.getMenuType()==5){
                allList.add(data);
            }
            else{
                topList.add(data);
            }
        }
        AdminMenuVO top = new  AdminMenuVO();
        top.setMenuId(0);
        top.setMenuName("一级菜单");
        top.setChildMenu(topList);
        top.setMenuType(1);
        top.setRealm("top");
        allList.add(0,top);
        object.put("all",allList);
        return object;
    }

    @Override
    public List<AdminMenu> queryMenuListByPath(AdminMenu adminMenu) {
        return getBaseMapper().queryMenuListByPath(adminMenu, UserUtil.getUser().getRoles());
    }
}
