package com.kakarote.build.labour.service.impl;

import com.kakarote.build.labour.entity.BO.LabourSpeworktypeBO;
import com.kakarote.build.labour.entity.BO.LabourSubconPeopleCerBO;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.entity.PO.LabourSubconPeopleCer;
import com.kakarote.build.labour.mapper.LabourSubconPeopleCerMapper;
import com.kakarote.build.labour.service.ILabourInfocollectionService;
import com.kakarote.build.labour.service.ILabourSubconPeopleCerService;
import com.kakarote.build.project.entity.PO.ProjectUser;
import com.kakarote.build.project.service.IProjectUserService;
import com.kakarote.build.wxapp.util.StringUtil;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.feign.admin.entity.AdminUserVO;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 证件管理---分包管理人员证件台账 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-03-26
 */
@Service
public class LabourSubconPeopleCerServiceImpl extends BaseServiceImpl<LabourSubconPeopleCerMapper, LabourSubconPeopleCer> implements ILabourSubconPeopleCerService {

    @Autowired
    private AdminService adminUser;
    @Autowired
    private ILabourInfocollectionService infocollectionService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private IProjectUserService projectUserService;

    @Override
    public BasePage<LabourSubconPeopleCer> selectList(LabourSubconPeopleCerBO labourSubconPeopleCerBO) {
        return getBaseMapper().selectList(labourSubconPeopleCerBO.parse(),labourSubconPeopleCerBO);
    }

    @Override
    public BasePage<Map> selectTabsByInfo(LabourSpeworktypeBO labourSpeworktypeBO) {
        return getBaseMapper().selectTabsByInfo(labourSpeworktypeBO.parse(),labourSpeworktypeBO);
    }

    @Override
    public BasePage<Map> selectTabsByInfoNew(LabourSpeworktypeBO labourSpeworktypeBO) {
        return getBaseMapper().selectTabsByInfoNew(labourSpeworktypeBO.parse(),labourSpeworktypeBO);
    }

    @Override
    public Result addUser(Integer pid, Integer roleId) {
        AdminUserVO adminUserVO = new AdminUserVO();
        LabourInfocollection infocollection = infocollectionService.getById(pid);
        UserInfo user = UserUtil.getUser();
        adminUserVO.setUsername(infocollection.getPhone());
        adminUserVO.setPassword("qdoner"+(infocollection.getIdCard().substring((infocollection.getIdCard().length()-4),infocollection.getIdCard().length())));
        System.out.println("qdoner"+(infocollection.getIdCard().substring((infocollection.getIdCard().length()-4),infocollection.getIdCard().length())));
        adminUserVO.setMobile(infocollection.getPhone());
        adminUserVO.setSex(Integer.valueOf(infocollection.getSex()));
        adminUserVO.setDeptId(user.getDeptId());
        //Result<List<Integer>> role =  adminService.queryRoleByRoleType(type);
        adminUserVO.setRoleId(roleId+"");
        adminUserVO.setTenantId(user.getTenantId().intValue());
        adminUserVO.setRealname(infocollection.getName());
        adminUserVO.setProjectId(infocollection.getProjectId());
        Result res = adminUser.addUser(adminUserVO);
        if(res.getCode() == 0){
            ProjectUser projectUser = new ProjectUser();
            Long userId = (Long)res.getData();
            projectUser.setId(UUID.randomUUID().toString().trim().replaceAll("-", ""));
            projectUser.setUserId(userId.intValue());
            projectUser.setProjectId(user.getProjectId().intValue());
            projectUser.setTenantId(user.getTenantId().intValue());
            projectUserService.save(projectUser);
            Map mapr = new HashMap();
            mapr.put("username",infocollection.getPhone());
            mapr.put("password","qdoner"+(infocollection.getIdCard().substring((infocollection.getIdCard().length()-4),infocollection.getIdCard().length())));
            return Result.ok(mapr);
        }else {
            return res;
        }
    }
}
