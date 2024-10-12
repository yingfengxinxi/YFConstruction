package com.kakarote.build.equipment.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.equipment.entity.PO.BEquipmentPitFormwork;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorDataDay;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorPoint;
import com.kakarote.build.equipment.mapper.EquipmentMonitorDataDayMapper;
import com.kakarote.build.equipment.service.IBEquipmentPitFormworkService;
import com.kakarote.build.equipment.service.IEquipmentMonitorDataDayService;
import com.kakarote.build.equipment.service.IEquipmentMonitorPointService;
import com.kakarote.build.utils.DateUtil;
import com.kakarote.core.servlet.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 设备监测-监测项日数据表 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-10-28
 */
@Slf4j
@Service
public class EquipmentMonitorDataDayServiceImpl extends BaseServiceImpl<EquipmentMonitorDataDayMapper, EquipmentMonitorDataDay> implements IEquipmentMonitorDataDayService {

    @Autowired
    private IEquipmentMonitorPointService pointService;

    @Autowired
    private IBEquipmentPitFormworkService pitFormworkService;

    @Override
    public JSONObject getDayDataByPoint(Integer proId) {
        //固定 近24小时
        String p = "yyyy-MM-dd HH:00:00";
        String endDate = DateUtil.stringDate(new Date(), p);
        List<String> hoursList = new ArrayList<>();
        String startDate = null;
        try {
            startDate = DateUtil.addDate(endDate, -1, p);
            hoursList = DateUtil.getHoursStrList(startDate, endDate, p);
            hoursList.add(endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //查询设备ID
        List<BEquipmentPitFormwork> pitFormworks = pitFormworkService.lambdaQuery().eq(BEquipmentPitFormwork::getProId, proId).list();
        if (pitFormworks.size() < 1) {
            log.error("Error => No device is associated with the project");
            return null;
        }

        //查询结果集
        JSONObject result = new JSONObject();

        List<JSONObject> list = new ArrayList<>();
        for (BEquipmentPitFormwork pF : pitFormworks) {
            //查询点位
            List<EquipmentMonitorPoint> pointList = pointService.lambdaQuery().eq(EquipmentMonitorPoint::getEquipmentId,pF.getEquipmentId())
                    .eq(EquipmentMonitorPoint::getStatus,"1")
                    .orderByAsc(EquipmentMonitorPoint::getSort).list();
            //点位日数据
            for(EquipmentMonitorPoint point : pointList){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name",point.getPointName());
                //数据
                List<String > values = this.getBaseMapper().getDayDataByPoint(point.getId(),hoursList);
                jsonObject.put("value",values);
                list.add(jsonObject);
            }
        }
        result.put("list",list);
        result.put("xdata",hoursList);
        return result;
    }

    @Override
    public JSONObject getDayDataByEquiId(Integer equipmentId) {
        //固定 近24小时
        String p = "yyyy-MM-dd HH:00:00";
        String endDate = DateUtil.stringDate(new Date(), p);
        List<String> hoursList = new ArrayList<>();
        String startDate = null;

        // 格式化日期
        String format = "yyyy-MM-dd HH";
        String formatEndDate = DateUtil.stringDate(new Date(), format);
        List<String> formatHoursList = new ArrayList<>();
        try {
            startDate = DateUtil.addDate(endDate, -1, p);
            hoursList = DateUtil.getHoursStrList(startDate, endDate, p);
            hoursList.add(endDate);

            // 格式化日期
            formatHoursList = DateUtil.getHoursStrList(startDate, endDate, format);
            formatHoursList.add(formatEndDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //查询结果集
        JSONObject result = new JSONObject();

        List<JSONObject> list = new ArrayList<>();
        //查询点位
        List<EquipmentMonitorPoint> pointList = pointService.lambdaQuery().eq(EquipmentMonitorPoint::getEquipmentId,equipmentId)
                .eq(EquipmentMonitorPoint::getStatus,"1")
                .orderByAsc(EquipmentMonitorPoint::getSort).list();
        //点位日数据
        for(EquipmentMonitorPoint point : pointList){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",point.getPointName());
            //数据
            List<String > values = this.getBaseMapper().getDayDataByPoint(point.getId(),hoursList);
            jsonObject.put("value",values);
            list.add(jsonObject);
        }
        result.put("list",list);
        result.put("xdata",formatHoursList);
        return result;
    }
}
