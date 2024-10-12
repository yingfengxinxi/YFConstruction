package com.kakarote.build.labour.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.kakarote.build.env.entity.PO.EnvPostLog;
import com.kakarote.build.labour.entity.PO.BLabourInspection;
import com.kakarote.build.labour.entity.PO.BLabourInspectionCheck;
import com.kakarote.build.labour.entity.PO.BLabourInspectionCheckDetails;
import com.kakarote.build.labour.mapper.BLabourInspectionCheckMapper;
import com.kakarote.build.labour.service.IBLabourInspectionCheckDetailsService;
import com.kakarote.build.labour.service.IBLabourInspectionCheckService;
import com.kakarote.build.labour.service.IBLabourInspectionService;
import com.kakarote.build.safety.entity.PO.SafetyInspectionInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 企业报验检查表 服务实现类
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
@Service
public class BLabourInspectionCheckServiceImpl extends BaseServiceImpl<BLabourInspectionCheckMapper, BLabourInspectionCheck> implements IBLabourInspectionCheckService {

    @Autowired
    private IBLabourInspectionService inspectionService;

    @Autowired
    private IBLabourInspectionCheckDetailsService inspectionCheckDetailsService;

    @Override
    public Map<String, Object> selectByInspectionId(String inspectionId) {

        Map<String, Object> data = new HashMap<>();
        // 查询企业报验申请数据 和项目信息
        Map<String, Object> inspection = inspectionService.selectApplyAndProjectInfo(inspectionId);
        data.put("inspection", inspection);

        // 查询检查主表数据
        BLabourInspectionCheck inspectionCheck = getBaseMapper().selectByInspectionId(inspectionId);
        data.put("inspectionCheck",inspectionCheck);
        // 查询检查明细数据
        if(inspectionCheck!=null){
            List<BLabourInspectionCheckDetails> inspectionCheckDetails = inspectionCheckDetailsService.selectItemList(inspectionCheck.getId());
            data.put("inspectionCheckDetails",inspectionCheckDetails);
        }

        return data;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveCheckData(Map<String, Object> params) {

        if (params.get("inspectionCheck") == null || params.get("inspectionCheck").equals("")) {
            throw new CrmException(500, "监督检查数据不能为空");
        }

        if (params.get("inspectionCheckDetails") == null || params.get("inspectionCheckDetails").equals("")) {
            throw new CrmException(500, "检查内容不能为空");
        }

        List<Map<String,Object>> detailsList = (List<Map<String, Object>>) params.get("inspectionCheckDetails");
        if (detailsList.size() <= 0) {
            throw new CrmException(500, "检查内容不能为空");
        }

        Map<String, Object> checkMap = (Map<String, Object>) params.get("inspectionCheck");
        BLabourInspectionCheck inspectionCheck = JSON.parseObject(JSON.toJSONString(checkMap), BLabourInspectionCheck.class);
        String checkId = UuidUtils.generateUuid().replace("-","");
        inspectionCheck.setId(checkId);
        inspectionCheck.setCreateBy(UserUtil.getUserId());
        inspectionCheck.setCreateTime(new Date());
        inspectionCheck.setCheckPeople(UserUtil.getUser().getRealname());


        // 保存数据
        boolean checkSave = this.save(inspectionCheck);

        if (!checkSave) {
            throw new CrmException(500, "监督检查信息保存失败！");
        }

        BLabourInspection inspection = new BLabourInspection();
        // 保存明细数据
        List<BLabourInspectionCheckDetails> dataList = new ArrayList<>();
        for (int i = 0; i<detailsList.size(); i++) {
            Map<String, Object> map = detailsList.get(i);
            BLabourInspectionCheckDetails details = JSON.parseObject(JSON.toJSONString(map), BLabourInspectionCheckDetails.class);
            // 如果明细中存在需要整改或者需要停工的，更新申请数据为需要整改
            if ("2".equals(details.getCheckDisposal()) || "3".equals(details.getCheckDisposal())) {
                inspection.setRectificationInfo("1");
            }
            String uuid = UuidUtils.generateUuid().replace("-","");
            details.setId(uuid);
            details.setCheckId(checkId);
            details.setProjectId(inspectionCheck.getProjectId());
            details.setCreateBy(UserUtil.getUserId());
            details.setCreateTime(new Date());
            details.setSort(i);
            dataList.add(details);
        }

        inspectionCheckDetailsService.saveBatch(dataList);

        // 更新申请数据
        inspection.setId(inspectionCheck.getInspectionId());
        inspection.setInspectionType("1"); // 状态更新为已检查
        inspectionService.updateById(inspection);

        return true;
    }
}
