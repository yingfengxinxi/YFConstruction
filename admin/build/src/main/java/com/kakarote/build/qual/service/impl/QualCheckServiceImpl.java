package com.kakarote.build.qual.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kakarote.build.qual.entity.BO.QualCheckBO;
import com.kakarote.build.qual.entity.PO.QualCheck;
import com.kakarote.build.qual.entity.PO.QualHiddenTrouble;
import com.kakarote.build.qual.mapper.QualCheckMapper;
import com.kakarote.build.qual.service.IQualCheckService;
import com.kakarote.build.qual.service.IQualHiddenTroubleService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.TagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工质量--质量检查管理 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-03-23
 */
@Service
public class QualCheckServiceImpl extends BaseServiceImpl<QualCheckMapper, QualCheck> implements IQualCheckService {

    @Autowired
    private IQualHiddenTroubleService hiddenTroubleService;

    @Autowired
    private AdminService adminService;

    @Override
    public BasePage<QualCheck> selectByPage(QualCheckBO qualCheckBO) {
        return this.getBaseMapper().selectByPage(qualCheckBO.parse(), qualCheckBO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer saveOrUpdateInfo(Map<String, Object> params) {

        Map<String, Object> checkForm = (Map<String, Object>) params.get("checkForm");
        List<Map<String, Object>> hiddenTroubleList = (List<Map<String, Object>>) params.get("hiddenTroubleList");

        if (checkForm == null) {
            throw new CrmException(1005, "checkForm参数不存在");
        }

        if (hiddenTroubleList == null || hiddenTroubleList.size() == 0) {
            throw new CrmException(1005, "hiddenTroubleListL参数不存在");
        }

        Integer checkId = null;

        // 保存质量检查基本信息
        QualCheck qualCheck = JSON.parseObject(JSON.toJSONString(checkForm), QualCheck.class);
        if (qualCheck.getCheckId() != null) {
            qualCheck.setUpdateTime(new Date());
            checkId = qualCheck.getCheckId();

            // 巡检日期和整改期限可以保存为null
            LambdaUpdateWrapper<QualCheck> updateWrapper = new LambdaUpdateWrapper();
            updateWrapper.set(QualCheck::getQualRepairTimeLimit, qualCheck.getQualRepairTimeLimit());
            updateWrapper.set(QualCheck::getQualCheckDate, qualCheck.getQualCheckDate());
            updateWrapper.eq(QualCheck::getCheckId, qualCheck.getCheckId());

            getBaseMapper().update(qualCheck, updateWrapper);
        } else {
            qualCheck.setCreateTime(new Date());
            getBaseMapper().saveInfo(qualCheck);
            checkId = qualCheck.getCheckId();
        }

        // 先删除在更新
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("check_id", checkId);
        hiddenTroubleService.remove(queryWrapper);
        // 保存隐患信息
        for (Map<String, Object> hiddenTroubleMap:hiddenTroubleList) {
            QualHiddenTrouble hiddenTrouble = JSON.parseObject(JSON.toJSONString(hiddenTroubleMap), QualHiddenTrouble.class);
            hiddenTrouble.setCheckId(checkId);

            if (hiddenTrouble.getCreateTime() == null) {
                hiddenTrouble.setCreateTime(new Date());
            }

            hiddenTrouble.setUpdateTime(new Date());
            hiddenTroubleService.save(hiddenTrouble);
        }

        return checkId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer receiptInfo(Map<String, Object> params) {
        Map<String, Object> checkForm = (Map<String, Object>) params.get("checkForm");
        List<Map<String, Object>> hiddenTroubleList = (List<Map<String, Object>>) params.get("hiddenTroubleList");

        if (checkForm == null) {
            throw new CrmException(1005, "checkForm参数不存在");
        }

        if (hiddenTroubleList == null || hiddenTroubleList.size() == 0) {
            throw new CrmException(1005, "hiddenTroubleListL参数不存在");
        }

        Integer checkId = null;

        // 保存质量检查基本信息
        QualCheck qualCheck = JSON.parseObject(JSON.toJSONString(checkForm), QualCheck.class);
        qualCheck.setUpdateTime(new Date());
        // 设置回执状态
        qualCheck.setQualReceiptStatus("1");
        checkId = qualCheck.getCheckId();
        if (checkId == null) {
            throw new CrmException(1006, "保存失败");
        }
        getBaseMapper().updateById(qualCheck);

        // 保存隐患信息
        for (Map<String, Object> hiddenTroubleMap:hiddenTroubleList) {
            QualHiddenTrouble hiddenTrouble = JSON.parseObject(JSON.toJSONString(hiddenTroubleMap), QualHiddenTrouble.class);
            hiddenTrouble.setCheckId(checkId);
            hiddenTrouble.setUpdateTime(new Date());
            hiddenTroubleService.updateById(hiddenTrouble);
        }

        return checkId;
    }

    @Override
    public Map<String, Object> seletcCheckInfoById(Integer checkId) {

        Map<String, Object> result = new HashMap<>();
        // 查询 质量检查基本信息
        QualCheck qualCheck = getBaseMapper().selectById(checkId);
        List<QualHiddenTrouble> hiddenTroubleList  = hiddenTroubleService.lambdaQuery().eq(QualHiddenTrouble::getCheckId, checkId).list();

        // 查询用户对象
        List<SimpleUser> userList = adminService.queryUserByIds(TagUtil.toLongSet(qualCheck.getQualCheckPersonId())).getData();
        qualCheck.setOwnerUserList(userList);

        result.put("qualCheck", qualCheck);
        result.put("hiddenTroubleList", hiddenTroubleList);

        return result;
    }

    @Override
    public Map<String, Object> getPrintData(Integer id) {
        // 查询 质量检查基本信息
        Map<String, Object> qualCheck = getBaseMapper().getPrintData(id);
        // 查询隐患信息
        List<QualHiddenTrouble> hiddenTroubleList  = hiddenTroubleService.lambdaQuery().eq(QualHiddenTrouble::getCheckId, id).list();

        qualCheck.put("detailList", hiddenTroubleList);

        return qualCheck;
    }
}
