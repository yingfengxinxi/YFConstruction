package com.kakarote.build.safety.job;

import com.kakarote.build.safety.entity.PO.SafetyInspectionInfo;
import com.kakarote.build.safety.entity.PO.SafetyInspectionPlan;
import com.kakarote.build.safety.service.impl.SafetyInspectionInfoServiceImpl;
import com.kakarote.build.safety.service.impl.SafetyInspectionPlanServiceImpl;
import com.kakarote.build.utils.CodeUtil;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.service.ISequenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 维护计划
 */
@Component
@Slf4j
public class inspectionPlanTask {

    @Autowired
    private SafetyInspectionPlanServiceImpl inspectionPlanService;

    @Autowired
    private SafetyInspectionInfoServiceImpl inspectionInfoService;

    @Autowired
    private ISequenceService sequenceService;

    /**
     * 当前日期 + 执行周期 >= 下次执行日期
     */
    //@Scheduled(cron = "0/5 * * * * ?")
    //@Scheduled(cron = "0 0 0 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void processHour() {
        log.info("后台定时任务：安全巡检计划执行 ==> 开始（执行周期：每日凌晨零点）");
        try {
            List<SafetyInspectionPlan> inspectionPlanList = inspectionPlanService.getBaseMapper().selectPlanList();
            //遍历
            List<SafetyInspectionPlan> inspectionPlans = new ArrayList<>();
            List<SafetyInspectionInfo> inspectionInfos = new ArrayList<>();

            for (int i = 0; i < inspectionPlanList.size(); i++) {
                SafetyInspectionPlan inspectionPlan = inspectionPlanList.get(i);
                // 获取当前日期
                Date today = new Date();
                today = parseDate(today);
                //考虑为空
                Date nextDate = inspectionPlan.getNextDate();
                if(nextDate == null){
                    //以启动时间为基础，下次执行日期
                    Date date = getNextDate(inspectionPlan.getStartDate(), inspectionPlan.getCycle(), inspectionPlan.getCycleUnit());//获取新的时间
                    nextDate = parseDate(date);
                }else{
                    nextDate = parseDate(inspectionPlan.getNextDate());
                }

                // 如果当前日期等于下次执行日期或者在下次执行日期之前 执行
                if (today.equals(nextDate) || today.after(nextDate)) {//周期内应执行
                    //组装维护记录
                    SafetyInspectionInfo inspectionInfo = new SafetyInspectionInfo();
                    inspectionInfo.setCheckCode(sequenceService.getDateSeq("3", inspectionPlan.getTenantId(), inspectionPlan.getProjectId()));
                    inspectionInfo.setPlanId(inspectionPlan.getId());
                    inspectionInfo.setExecuteDate(parseDate(new Date()));
                    inspectionInfo.setPlanName(inspectionPlan.getPlanName());
                    inspectionInfo.setPlanLevel(inspectionPlan.getPlanLevel());
                    inspectionInfo.setDeptId(inspectionPlan.getDeptId());
                    inspectionInfo.setTenantId(inspectionPlan.getTenantId());
                    inspectionInfo.setProjectId(inspectionPlan.getProjectId());

                    inspectionInfos.add(inspectionInfo);
                    //更新下次执行
                    inspectionPlan.setNextDate(getNextDate(inspectionInfo.getExecuteDate(), inspectionPlan.getCycle(), inspectionPlan.getCycleUnit()));
                    inspectionPlans.add(inspectionPlan);
                }
            }
            //保存记录
            if (inspectionInfos.size() > 0){
                inspectionInfoService.getBaseMapper().batchSaveInfo(inspectionInfos);
            }
            //更新计划
            if (inspectionPlans.size() > 0){
                inspectionPlanService.getBaseMapper().batchUpdatePlan(inspectionPlans);
            }
            log.info("维护计划执行成功 ==> 完成");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("维护计划执行失败：", e);
            throw new CrmException(1000, "维护计划执行失败");
        }
    }

    /**
     * 获取下周期日期
     *
     * @param date
     * @param unit
     * @return
     */
    private Date getNextDate(Date date, String cycle, String unit) {
        Calendar curr = Calendar.getInstance(); //获取当前时间
        curr.setTime(date);
        //周期单位
        if (unit.equals("2")) {//天
            curr.set(Calendar.DAY_OF_MONTH, curr.get(Calendar.DAY_OF_MONTH) + Integer.parseInt(cycle)); //给当前时间增加一周
        }
        if (unit.equals("3")) {//周
            curr.set(Calendar.DAY_OF_MONTH, curr.get(Calendar.DAY_OF_MONTH) + 7 * Integer.parseInt(cycle)); //给当前时间增加一周
        }
        if (unit.equals("4")) {//月
            curr.set(Calendar.MONTH, curr.get(Calendar.MONTH) + Integer.parseInt(cycle)); //增加一月
        }
        if (unit.equals("5")) {//年
            curr.set(Calendar.YEAR, curr.get(Calendar.YEAR) + Integer.parseInt(cycle)); //增加一年
        }
        return curr.getTime();
    }

    /**
     * 时间格式化
     *
     * @param date
     * @return
     */
    private Date parseDate(Date date) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(format.format(date));
    }
}
