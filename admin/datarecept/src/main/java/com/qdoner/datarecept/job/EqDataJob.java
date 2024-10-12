package com.qdoner.datarecept.job;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qdoner.datarecept.entity.PO.BEquipmentElevatorDataAt;
import com.qdoner.datarecept.entity.PO.BEquipmentTowerDataAt;
import com.qdoner.datarecept.service.IBEquipmentElevatorDataAtService;
import com.qdoner.datarecept.service.IBEquipmentTowerDataAtService;
import com.qdoner.datarecept.utils.DateUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 处理数据处理
 */
@Component
@Slf4j
public class EqDataJob {

    /**
     * 塔基默认保留天数
     */
    private int DefTowerDay = 31;
    /**
     * 塔基最小保留天数，防止恶意传参
     */
    private int MinTowerDay = 7;
    private int DefElevatorDay = 31;
    private int MinElevatorDay = 7;


    @Autowired
    private IBEquipmentTowerDataAtService towerDataAtService;

    @Autowired
    private IBEquipmentElevatorDataAtService elevatorDataAtService;

    /**
     * 删除塔机数据
     * b_equipment_tower_data_at
     */
    @XxlJob("DelTowerData")
    public void delTowerData() {
        Integer day = DefTowerDay;
        String param = XxlJobHelper.getJobParam();
        if(!StringUtils.isEmpty(param)){
            JSONObject paramJson = JSONObject.parseObject(param);
            Integer p = paramJson.getInteger("day");
            if(p != null){
                day = p;
            }
        }
        if (day < MinTowerDay) {
            day = MinTowerDay;
        }
        //当前日期
        String nowDateStr = DateUtil.formatDateStr(new Date(), DateUtil.YMD);
        try {
            String endDateStr = DateUtil.addDay(nowDateStr + " 00:00:00", day * -1);
            QueryWrapper<BEquipmentTowerDataAt> queryWrapper = new QueryWrapper<BEquipmentTowerDataAt>();
            queryWrapper.lt("monitor_time", endDateStr);
            queryWrapper.isNotNull("tenant_id");
            queryWrapper.isNotNull("project_id");
            towerDataAtService.remove(queryWrapper);
            log.debug(">>>>>>>>>[JOB]DelTowerData =>delete lt {}", endDateStr);
        } catch (Exception e) {
            log.error(">>>>>>>>>[JOB]DelTowerData =>{}", e);
        }

    }

    /**
     * 删除升降机数据
     * b_equipment_elevator_data_at
     */
    @XxlJob("DelElevatorData")
    public void delElevatorData() {
        Integer day = DefElevatorDay;
        String param = XxlJobHelper.getJobParam();
        if(!StringUtils.isEmpty(param)){
            JSONObject paramJson = JSONObject.parseObject(param);
            Integer p = paramJson.getInteger("day");
            if(p != null){
                day = p;
            }
        }
        if (day < MinElevatorDay) {
            day = MinElevatorDay;
        }
        //当前日期
        String nowDateStr = DateUtil.formatDateStr(new Date(), DateUtil.YMD);
        try {
            String endDateStr = DateUtil.addDay(nowDateStr + " 00:00:00", day * -1);
            QueryWrapper<BEquipmentElevatorDataAt> queryWrapper = new QueryWrapper<BEquipmentElevatorDataAt>();
            queryWrapper.lt("monitor_time", endDateStr);
            queryWrapper.isNotNull("tenant_id");
            queryWrapper.isNotNull("project_id");
            elevatorDataAtService.remove(queryWrapper);
            log.debug(">>>>>>>>>[JOB]DelElevatorData =>delete lt {}", endDateStr);
        } catch (Exception e) {
            log.error(">>>>>>>>>[JOB]DelElevatorData => {}", e);
        }
    }
}
