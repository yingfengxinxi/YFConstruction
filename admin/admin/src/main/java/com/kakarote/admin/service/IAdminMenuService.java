package com.kakarote.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.admin.common.AdminRoleTypeEnum;
import com.kakarote.admin.entity.PO.AdminMenu;
import com.kakarote.admin.entity.VO.AdminMenuVO;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台菜单表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface IAdminMenuService extends BaseService<AdminMenu> {
    /**
     * 登录：查询用户所拥有的菜单权限
     *
     * @param userInfo 用户
     * @return 菜单权限的并集
     */
    public List<AdminMenu> queryMenuListForLogin(UserInfo userInfo);
    public List<AdminMenu> queryMenuListForAppLogin(UserInfo userInfo);
    /**
     * 查询用户所拥有的菜单权限
     *
     * @param userId 用户列表
     * @return 菜单权限的并集
     */
    public List<AdminMenu> queryMenuList(Long userId);

    /**
     * 查询公海菜单权限
     *
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return data
     */
    public Map<String, Long> queryPoolReadAuth(Long userId, Integer deptId);

    /**
     * 根据类型查询菜单
     * @param typeEnum type
     * @return data
     */
    public JSONObject getMenuListByType(AdminRoleTypeEnum typeEnum);


    Integer queryMenuId(String realm1, String realm2, String realm3);


    List<AdminMenu> queryByParentId(Integer parentId);

    /**
     * 租户
     * @param parentId
     * @return
     */
    List<AdminMenu> queryByParentIdTenant(Integer parentId);

    int saveMenu(AdminMenu adminMenu);

    int updateMenu(AdminMenu adminMenu);

    Result deleteMenu(String menuId);

    Result getMenuById(Integer menuId);

    /**
     * 租户/管理员：查询用户置顶的菜单列表
     *
     * @param userId 用户列表
     * @return 菜单列表
     */
    List<AdminMenu> queryHeaderMenuList(Long userId);

    /**
     * 租户/管理员：查询用户全部的应用列表（已启用）
     *
     * @return 菜单列表
     */
    List<AdminMenu> queryAllAppList();
    /**
     * 租户：查询用户全部的应用列表（已启用）
     *
     * @return 菜单列表
     */
    List<AdminMenu> queryAppList();
    /**
     * 查询用户全部的应用列表
     *
     * @return 应用列表
     */
    List<AdminMenu> queryTenantAppList();
    /**
     * 查询全部菜单
     * @return data
     */
    public JSONObject getAllMenuList(Integer tenantId);

    /**
     * 查询全部菜单--租户权限分配用
     * @return data
     */
    public JSONObject getMenuListByTenant();

    /**
     * 查询菜单列表
     * @param adminMenu
     * @return
     */
    List<AdminMenu> queryMenuListByPath(AdminMenu adminMenu);
}
