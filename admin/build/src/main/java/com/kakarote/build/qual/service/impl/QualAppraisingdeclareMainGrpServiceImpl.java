package com.kakarote.build.qual.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.qual.entity.BO.QualAppraisingdeclareMainGrpBO;
import com.kakarote.build.qual.entity.PO.*;
import com.kakarote.build.qual.mapper.QualAppraisingdeclareMainGrpMapper;
import com.kakarote.build.qual.service.IQualAppraisingdeclareMainGrpService;
import com.kakarote.build.qual.service.IQualAppraisingdeclareSonGrpService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工质量--创优评优申报--主表 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-04-20
 */
@Service
public class QualAppraisingdeclareMainGrpServiceImpl extends BaseServiceImpl<QualAppraisingdeclareMainGrpMapper, QualAppraisingdeclareMainGrp> implements IQualAppraisingdeclareMainGrpService {
    @Autowired
    private IQualAppraisingdeclareSonGrpService IQualAppraisingdeclareSonGrpService;
    @Autowired
    private AdminService adminService;
    @Override
    public BasePage<QualAppraisingdeclareMainGrp> selectList(QualAppraisingdeclareMainGrpBO qualAppraisingdeclareMainGrpBO) {
        qualAppraisingdeclareMainGrpBO.setOrdersBd("review_time",false);
        BasePage<QualAppraisingdeclareMainGrp> qualEnterprisecheckfillMainGrp = getBaseMapper().selectDataList(qualAppraisingdeclareMainGrpBO.parse(), qualAppraisingdeclareMainGrpBO);
        return qualEnterprisecheckfillMainGrp;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int add(Map<String, Object> params) {
        Map<String, Object> checkForm = (Map<String, Object>) params.get("checkForm");
        List<Map<String, Object>> hiddenDeclareList = (List<Map<String, Object>>) params.get("hiddenDeclareList");
        // 保存质量检查基本信息
        QualAppraisingdeclareMainGrp qualAppraisingdeclareMainGrp = JSON.parseObject(JSON.toJSONString(checkForm), QualAppraisingdeclareMainGrp.class);
        qualAppraisingdeclareMainGrp.setCreateBy(String.valueOf(UserUtil.getUser().getUserId()));
        qualAppraisingdeclareMainGrp.setCreateTime(new Date());
        qualAppraisingdeclareMainGrp.setCheckStatus(0);
        qualAppraisingdeclareMainGrp.setOwnerUserId(UserUtil.getUser().getUserId());
        qualAppraisingdeclareMainGrp.setReviewLeader(Integer.valueOf(qualAppraisingdeclareMainGrp.getReviewLeader()));
        boolean mian = save(qualAppraisingdeclareMainGrp);
        if (mian) {
            params.put("id",qualAppraisingdeclareMainGrp.getId());
            // 保存申报信息
            for (Map<String, Object> hiddenDeclareMap : hiddenDeclareList) {
                QualAppraisingdeclareSonGrp qualAppraisingdeclareSonGrp = JSON.parseObject(JSON.toJSONString(hiddenDeclareMap), QualAppraisingdeclareSonGrp.class);
                qualAppraisingdeclareSonGrp.setPid(qualAppraisingdeclareMainGrp.getId());
                qualAppraisingdeclareSonGrp.setCreateBy(String.valueOf(UserUtil.getUser().getUserId()));
                qualAppraisingdeclareSonGrp.setCreateTime(new Date());
                qualAppraisingdeclareSonGrp.setCheckStatus(0);
                qualAppraisingdeclareSonGrp.setOwnerUserId(UserUtil.getUser().getUserId());
                qualAppraisingdeclareSonGrp.setTenantId(null);
                boolean son = IQualAppraisingdeclareSonGrpService.save(qualAppraisingdeclareSonGrp);
                if (!son) {
                    throw new CrmException(500, "数据库子表修改数据失败");
                }
            }
        } else {
            throw new CrmException(500, "主表数据保存失败");
        }


        return 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int edit(Map<String, Object> params) {
        Map<String, Object> checkForm = (Map<String, Object>) params.get("checkForm");
        List<Map<String, Object>> hiddenDeclareList = (List<Map<String, Object>>) params.get("hiddenDeclareList");
        Integer pid = null;
        // 保存质量检查基本信息
        QualAppraisingdeclareMainGrp qualAppraisingdeclareMainGrp = JSON.parseObject(JSON.toJSONString(checkForm), QualAppraisingdeclareMainGrp.class);
        qualAppraisingdeclareMainGrp.setUpdateBy(String.valueOf(UserUtil.getUser().getUserId()));
        qualAppraisingdeclareMainGrp.setUpdateTime(new Date());
        qualAppraisingdeclareMainGrp.setOwnerUserId(UserUtil.getUser().getUserId());
        qualAppraisingdeclareMainGrp.setReviewLeader(Integer.valueOf(qualAppraisingdeclareMainGrp.getReviewLeader()));
        int i = getBaseMapper().updateById(qualAppraisingdeclareMainGrp);
        params.put("id",qualAppraisingdeclareMainGrp.getId());
        if (i > 0) {
            // 先删除在更新
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("pid", qualAppraisingdeclareMainGrp.getId());
            IQualAppraisingdeclareSonGrpService.remove(queryWrapper);
            // 保存申报信息
            for (Map<String, Object> hiddenDeclareMap : hiddenDeclareList) {
                QualAppraisingdeclareSonGrp qualAppraisingdeclareSonGrp = JSON.parseObject(JSON.toJSONString(hiddenDeclareMap), QualAppraisingdeclareSonGrp.class);
                qualAppraisingdeclareSonGrp.setPid(qualAppraisingdeclareMainGrp.getId());
                qualAppraisingdeclareSonGrp.setUpdateBy(String.valueOf(UserUtil.getUser().getUserId()));
                qualAppraisingdeclareSonGrp.setUpdateTime(new Date());
                qualAppraisingdeclareSonGrp.setTenantId(null);
                qualAppraisingdeclareSonGrp.setCheckStatus(0);
                boolean son = IQualAppraisingdeclareSonGrpService.save(qualAppraisingdeclareSonGrp);
                if (!son) {
                    throw new CrmException(500, "数据库子表修改数据失败");
                }
            }
        } else {
            throw new CrmException(500, "数据库主表修改数据失败");
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(String ids) {
        String strs[] = ids.split(",");
        for (String id : strs) {
            //删除主表数据
            boolean yz = removeById(id);
            //删除子表数据
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("pid", id);
            IQualAppraisingdeclareSonGrpService.remove(queryWrapper);
        }
        int row = strs.length;
        return row;
    }

    @Override

    public Map<String, Object> selectById(String id) {
        QualAppraisingdeclareMainGrp qualAppraisingdeclareMainGrp = getById(id);
        List<QualAppraisingdeclareSonGrp> hiddenDeclareList = IQualAppraisingdeclareSonGrpService.lambdaQuery().eq(QualAppraisingdeclareSonGrp::getPid, id).list();
        // 查询用户对象
        List<SimpleUser> userList = adminService.queryUserByIds(TagUtil.toLongSet(qualAppraisingdeclareMainGrp.getReviewMember())).getData();
        qualAppraisingdeclareMainGrp.setOwnerUserList(userList);
        Map<String, Object> map = new HashMap<>();
        map.put("appraisingDeclare", qualAppraisingdeclareMainGrp);
        map.put("hiddenDeclareList", hiddenDeclareList);
        return map;
    }
}
