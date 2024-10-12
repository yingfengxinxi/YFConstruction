package com.kakarote.build.equipment.job;

import com.kakarote.build.equipment.entity.PO.EquipmentPlan;
import com.kakarote.build.equipment.entity.PO.EquipmentSet;
import com.kakarote.build.equipment.service.IEquipmentSetService;
import com.kakarote.build.equipment.service.impl.EquipmentPlanServiceImpl;
import com.kakarote.build.utils.CodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
public class PlanTask {

    @Autowired
    private EquipmentPlanServiceImpl planService;

    @Autowired
    private IEquipmentSetService setService;

    /**
     * 当前日期 + 执行周期 >= 下次执行日期
     */
//    @Scheduled(cron = "0/50 * * * * ?")
//    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void processHour() {
        log.info("后台定时任务：设备预防性维护计划执行 ==> 开始（执行周期：每日凌晨零点）");
        try {
            List<EquipmentPlan> planList = planService.lambdaQuery().eq(EquipmentPlan::getEnable, "1")
                    .ne(EquipmentPlan::getTenantId, "0").ne(EquipmentPlan::getProjectId, "0").
                    orderByAsc(EquipmentPlan::getLevel).orderByAsc(EquipmentPlan::getCreateTime).list();
            //遍历
            List<EquipmentPlan> plans = new ArrayList<>();
            List<EquipmentSet> assets = new ArrayList<>();
            //
            String setCode = null;
            for (int i = 0; i < planList.size(); i++) {
                EquipmentPlan plan = planList.get(i);
                // 获取当前日期
                Date today = new Date();
                today = parseDate(today);
                //考虑为空
                Date nextDate = plan.getNextDate();
                if(nextDate == null){
                    //以启动时间为基础，下次执行日期
                    Date date = getNextDate(plan.getStartDate(), plan.getCycle(), plan.getUnit());//获取新的时间
                    nextDate = parseDate(date);
                }else{
                    nextDate = parseDate(plan.getNextDate());
                }

                // 如果当前日期等于下次执行日期或者在下次执行日期之前 执行
                if (today.equals(nextDate) || today.after(nextDate)) {//周期内应执行
                    //组装维护记录
                    EquipmentSet set = new EquipmentSet();
                    setCode = CodeUtil.getCode(setCode);//编码
                    set.setSetCode(setCode);
                    set.setPlanId(plan.getPlanId());
                    set.setPlanName(plan.getPlanName());
                    set.setPlanType(plan.getPlanType());
                    set.setCycle(plan.getCycle());
                    set.setLevel(plan.getLevel());
                    set.setDeptId(plan.getDeptId());
                    set.setUnit(plan.getUnit());
                    set.setSetDate(parseDate(new Date()));
                    //
                    set.setTenantId(plan.getTenantId());
                    set.setProjectId(plan.getProjectId());
                    assets.add(set);
                    //更新下次执行
                    plan.setNextDate(getNextDate(set.getSetDate(), plan.getCycle(), plan.getUnit()));
                    plans.add(plan);
                }
            }
            //保存记录
            if (assets.size() > 0){
                setService.saveBatch(assets);
            }
            //更新计划
            if (plans.size() > 0){
                planService.getBaseMapper().batchUpdatePlan(plans);
            }
            log.info("维护计划执行成功 ==> 完成");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("维护计划执行失败：" + e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键

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
