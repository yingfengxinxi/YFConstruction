package com.kakarote.build.labour.apipm.service.impl;

import com.kakarote.build.labour.apipm.service.SafetyTrainingService;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceRecord;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.safety.entity.PO.BSafetySafetyTraining;
import com.kakarote.build.safety.mapper.BSafetySafetyTrainingMapper;
import com.kakarote.build.utils.ApiUtils;
import com.kakarote.build.utils.DateUtil;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class SafetyTrainingServiceImpl extends BaseServiceImpl<BSafetySafetyTrainingMapper, BSafetySafetyTraining> implements SafetyTrainingService {

    @Autowired
    private ApiUtils apiUtils;

    @Override
    public Result postSafetyTraining(Map<String, Object> parpam) {
        Map<String, Object> data;
        ProjectInfo projectInfo= apiUtils.dataValidation(parpam);
        if(parpam.get("data") != null && !"".equals(parpam.get("data"))){
            data = (Map<String, Object>) parpam.get("data");
            List<Map> list = (List)data.get("workerList");
            if (data.get("workerList") == null || list.size()<=0) {
                throw new CrmException(500, "人员列表数据不能为空");
            }
            if (data.get("noteTitle") == null || "".equals(data.get("noteTitle"))) {
                throw new CrmException(500, "主题名称不能为空");
            }
            if (data.get("trainingPlace") == null || "".equals(data.get("trainingPlace"))) {
                throw new CrmException(500, "培训地点不能为空");
            }
            if (data.get("trainingType") == null || "".equals(data.get("trainingType"))) {
                throw new CrmException(500, "培训方式不能为空");
            }
            if (data.get("trainingWay") == null || "".equals(data.get("trainingWay"))) {
                throw new CrmException(500, "培训类型不能为空");
            }

            if (data.get("signStartBeforeTraining") == null || "".equals(data.get("signStartBeforeTraining"))) {
                throw new CrmException(500, "培训开始前签到不能为空");
            }
            if (data.get("signEndAfterTraining") == null || "".equals(data.get("signEndAfterTraining"))) {
                throw new CrmException(500, "培训结束后签到不能为空");
            }
            if (data.get("startTime") == null || "".equals(data.get("startTime"))) {
                throw new CrmException(500, "培训开始时间不能为空");
            }
            if (data.get("endTime") == null || "".equals(data.get("endTime"))) {
                throw new CrmException(500, "培训结束时间不能为空");
            }
            if (data.get("trainPersonList") == null || "".equals(data.get("trainPersonList"))) {
                throw new CrmException(500, "培训人员列表不能为空");
            }
            for(Map map:list){
                BLabourAttendanceRecord labourAttendanceRecord = new BLabourAttendanceRecord();
                labourAttendanceRecord.setProjectId(projectInfo.getProjectId());
                labourAttendanceRecord.setTenantId(projectInfo.getTenantId());
                labourAttendanceRecord.setAttendanceRecordTeamName((String)data.get("teamId"));
                if (map.get("idCardNumber") == null || "".equals(map.get("idCardNumber"))) {
                    throw new CrmException(500, "工人身份证号不能为空");
                }

            }
        }else{
            throw new CrmException(500, "您未传输数据主体！");
        }
        return Result.ok();
    }

    @Override
    public Result safetyTrainingPeople(Map<String, Object> parpam) {
        return null;
    }

    @Override
    public Result syncSafetyTraining(Map<String, Object> parpam) {
        return null;
    }
}
