package com.kakarote.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.admin.common.AdminCodeEnum;
import com.kakarote.admin.entity.BO.AdminDeptBO;
import com.kakarote.admin.entity.BO.AdminDeptQueryBO;
import com.kakarote.admin.entity.BO.DeptVO;
import com.kakarote.admin.entity.PO.AdminDept;
import com.kakarote.admin.entity.PO.AdminDeptMenu;
import com.kakarote.admin.entity.PO.AdminMenu;
import com.kakarote.admin.entity.PO.AdminRole;
import com.kakarote.admin.entity.VO.AdminDeptVO;
import com.kakarote.admin.mapper.AdminDeptMapper;
import com.kakarote.admin.service.*;
import com.kakarote.core.common.BaseStatusEnum;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.RecursionUtil;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@Service
public class AdminDeptServiceImpl extends BaseServiceImpl<AdminDeptMapper, AdminDept> implements IAdminDeptService {

    @Autowired
    private IAdminUserService adminUserService;
    @Autowired
    private IAdminMenuService adminMenuService;
    @Autowired
    private IAdminTenantService adminTenantService;
    @Autowired
    private IAdminRoleService adminRoleService;

    @CreateCache(name = Const.ADMIN_DEPT_NAME_CACHE_NAME, expire = 3, timeUnit = TimeUnit.DAYS,cacheType = CacheType.LOCAL)
    private Cache<Integer, String> deptCache;

    /**
     * 查询部门树
     *
     * @param queryBO 上级ID
     * @return data
     */
    @Override
    public List<AdminDeptVO> queryDeptTree(AdminDeptQueryBO queryBO) {
        if (queryBO.getId() == null) {
            queryBO.setId(0);
        }
        List<AdminDept> adminDeptList =  lambdaQuery().eq(AdminDept::getDeptId, UserUtil.getUser().getTenantId())
                .or(or-> or.eq(AdminDept::getTenantId, UserUtil.getUser().getTenantId())).list();
        int id = queryBO.getId();

        if(id==0 && UserUtil.getUser().getTenantId()!=1L){
            id = 1;
        }
        //根据数据权限获取部门信息2021-12-24 邵化振
        List<AdminRole> roleList = adminRoleService.queryRoleListByUserId(UserUtil.getUser().getUserId());
        if(roleList.stream().filter(w->w.getDataType() == 5).findAny().isPresent()){
            id = 1;
        }else if(roleList.stream().filter(w->w.getDataType() == 4||w.getDataType() == 3).findAny().isPresent()){
            AdminDept adminDept = getById(UserUtil.getUser().getDeptId()) ;
            id = adminDept.getPid();
        }if(roleList.stream().filter(w->w.getDataType() == 2||w.getDataType() == 1).findAny().isPresent()){
            id = UserUtil.getUser().getDeptId();
        }

        if ("tree".equals(queryBO.getType())) {
            return RecursionUtil.getChildListTree(adminDeptList, "pid", id, "deptId", "children", AdminDeptVO.class);
        }
        if ("update".equals(queryBO.getType())) {
            List<Integer> ids = RecursionUtil.getChildList(adminDeptList, "pid",id, "deptId", "deptId");
            adminDeptList.removeIf(dept -> ids.contains(dept.getDeptId()));
        }
        return adminDeptList.stream().map(obj -> BeanUtil.copyProperties(obj, AdminDeptVO.class)).collect(Collectors.toList());
    }

    /**
     * 查询部门下属部门
     *
     * @param parentId 上级ID
     * @return data
     */
    @Override
    public List<Integer> queryChildDept(Integer parentId) {
        return RecursionUtil.getChildList(list(), "pid", parentId, "deptId", "deptId");
    }

    /**
     * 新增部门
     *
     * @param adminDeptBO 部门信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addDept(AdminDeptBO adminDeptBO) {
        //pid为0的部门只能存在一个
        if (BaseStatusEnum.CLOSE.getStatus().equals(adminDeptBO.getPid())) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        AdminDept adminDept = BeanUtil.copyProperties(adminDeptBO, AdminDept.class);
        save(adminDept);
        return adminDept.getDeptId();
    }

    /**
     * 修改部门
     *
     * @param adminDeptBO 部门信息
     */
    @Override
    public void setDept(AdminDeptBO adminDeptBO) {
        List<Integer> deptIdList = queryChildDept(adminDeptBO.getDeptId());
        deptIdList.add(adminDeptBO.getDeptId());
        if (deptIdList.contains(adminDeptBO.getPid())) {
            throw new CrmException(AdminCodeEnum.ADMIN_PARENT_DEPT_ERROR);
        }
        AdminDept adminDept = getById(adminDeptBO.getDeptId());
        //pid为0的部门只能存在一个
        if ((!adminDept.getPid().equals(0)) && adminDeptBO.getPid().equals(0)) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        adminDept.setName(adminDeptBO.getName());
        adminDept.setPid(adminDeptBO.getPid());
        adminDept.setStatus(adminDeptBO.getStatus());
        adminDept.setEffectiveStartDate(adminDeptBO.getEffectiveStartDate());
        adminDept.setEffectiveEndDate(adminDeptBO.getEffectiveEndDate());
        updateById(adminDept);
    }

    /**
     * 删除部门
     *
     * @param deptId 部门ID
     */
    @Override
    public void deleteDept(Integer deptId) {
        Integer userCount = adminUserService.query().eq("dept_id", deptId).count();
        if (userCount > 0) {
            throw new CrmException(AdminCodeEnum.ADMIN_DEPT_REMOVE_EXIST_USER_ERROR);
        }
        Integer deptCount = query().eq("pid", deptId).count();
        if (deptCount > 0) {
            throw new CrmException(AdminCodeEnum.ADMIN_DEPT_REMOVE_EXIST_DEPT_ERROR);
        }
        removeById(deptId);
        //同步删除租户下的菜单、权限
        QueryWrapper<AdminMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_id",deptId);
        adminMenuService.getBaseMapper().delete(wrapper);
        QueryWrapper<AdminDeptMenu> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("tenant_id",deptId);
        wrapper2.eq("dept_id",deptId);
        adminTenantService.getBaseMapper().delete(wrapper2);
    }

    /**
     * 根据部门ID获取部门名称
     *
     * @param deptId 部门ID
     * @return data
     */
    @Override
    public String getNameByDeptId(Integer deptId) {
        AdminDept adminDept = query().select("name").eq("dept_id", deptId).one();
        if (adminDept == null) {
            return "";
        }
        deptCache.put(deptId, adminDept.getName());
        return adminDept.getName();
    }

    @Override
    public Integer getPIdByUser() {
        AdminDept adminDept = query().select("pid").eq("dept_id", UserUtil.getUser().getDeptId()).one();
        if (adminDept == null) {
            return 0;
        }
        return adminDept.getPid();
    }

    /**
     * 根据ids查询用户信息
     *
     * @param ids id列表
     * @return data
     */
    @Override
    public List<SimpleDept> queryDeptByIds(List<Integer> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<AdminDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(AdminDept::getDeptId, AdminDept::getName).in(AdminDept::getDeptId, ids);
        return list(queryWrapper).stream().map(obj -> {
            SimpleDept simpleDept = new SimpleDept();
            simpleDept.setId(obj.getDeptId());
            simpleDept.setName(obj.getName());
            return simpleDept;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DeptVO> queryDeptUserList() {
        return getBaseMapper().queryDeptUserList();
    }
}
