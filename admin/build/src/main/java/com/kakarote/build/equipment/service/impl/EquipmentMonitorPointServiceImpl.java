package com.kakarote.build.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorPointBO;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorData;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorPoint;
import com.kakarote.build.equipment.mapper.EquipmentMonitorPointMapper;
import com.kakarote.build.equipment.service.IEquipmentMonitorDataService;
import com.kakarote.build.equipment.service.IEquipmentMonitorPointService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测点设置 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-05-18
 */
@Service
public class EquipmentMonitorPointServiceImpl extends BaseServiceImpl<EquipmentMonitorPointMapper, EquipmentMonitorPoint> implements IEquipmentMonitorPointService {

    @Autowired
    private IEquipmentMonitorDataService monitorDataService;

    @Override
    public BasePage<EquipmentMonitorPoint> selectByPage(EquipmentMonitorPointBO monitorPointBO) {

        BasePage<EquipmentMonitorPoint> list = getBaseMapper().selectByPage(monitorPointBO.parse(), monitorPointBO);
        return list;
    }

    @Override
    public BasePage<EquipmentMonitorPoint> listAllProject(EquipmentMonitorPointBO monitorPointBO) {
        BasePage<EquipmentMonitorPoint> list = getBaseMapper().listAllProject(monitorPointBO.parse(), monitorPointBO);
        return list;
    }

    /**
     * 基坑监测历史数据曲线图---监测点查询
     * 监测项类型 字典项 1：基坑监测、2：塔机监测、3；升降机监测、9：环境监测
     * @return
     */
    @Override
    public List<Map> selectPointsInfo(Map<String,Object>map) {
        String type = (String)map.get("monitorType");

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("id pointId" ,"point_name", "point_code").eq("monitor_type",type);
        queryWrapper.orderByAsc("sort");
        List<Map> list = getBaseMapper().selectMaps(queryWrapper);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectRaiseDustData() {

        List<Map<String, Object>> list = new ArrayList<>();

        // 查询监测点名称
        /*String[] pointCodes = {"HJ-008", "HJ-009", "HJ-002", "HJ-003", "HJ-004", "HJ-005"};

        // 根据监测点编号查询最新的历史数据
        for (int i = 0; i<pointCodes.length; i++) {
            String pointCode = pointCodes[i];
            Map<String, Object> map = getBaseMapper().selectRaiseDustDataByPointCode(pointCode);
            list.add(map);
        }*/

        Map<String, Object> params = new HashMap<>();
        params.put("monitorType", "9");
        String[] monitorSonTypes = {"1", "2", "3", "4", "5", "7"};

        // 根据监测项查询最新的历史数据
        for (int i = 0; i<monitorSonTypes.length; i++) {
            String monitorSonType = monitorSonTypes[i];
            params.put("monitorSonType", monitorSonType);
            Map<String, Object> map = getBaseMapper().selectEnvironmentalData(params);
            list.add(map);
        }

        return list;
    }

    public List<EquipmentMonitorPoint> selectAll(){
        return getBaseMapper().selectAll();
    }

    public List<EquipmentMonitorPoint> getMonitorList (String type){

        List<EquipmentMonitorPoint>  list  = getBaseMapper().getMonitorList(type);
        for(int i=0;i<list.size();i++){
            EquipmentMonitorData EquipmentMonitorData = monitorDataService.selectEquipmentMonitorDataOne(list.get(i).getId().toString());
            if(EquipmentMonitorData!=null){
                String value=EquipmentMonitorData.getValue();
                if(value!=null){
                    list.get(i).setControlsValue(value);
                    // 判断是否超过报警阈值若已经超过则展示值为1
                    double val =Double.valueOf(value) ;
                    if(list.get(i).getAlarmUpper()!=null&&!"".equals(list.get(i).getAlarmUpper())){
                        double AlarmUpper = list.get(i).getAlarmUpper().doubleValue();
                        if(val>AlarmUpper){
                            list.get(i).setControlsState("1");
                        }
                    }
                    if(list.get(i).getAlarmLower()!=null&&!"".equals(list.get(i).getAlarmLower())){
                        double AlarmLower = list.get(i).getAlarmLower().doubleValue();
                        if(val<AlarmLower){
                            list.get(i).setControlsState("1");
                        }
                    }
                }else{
                    list.get(i).setControlsState("2");
                }
            }else{
                list.get(i).setControlsState("2");
            }
            if(list.get(i).getControlsId()==null
                    ||list.get(i).getControlsId()==""||list.get(i).getControlsCoordinates()==""
                    ||list.get(i).getControlsCoordinates()==null){

                list.get(i).setControlsState("3");
            }
        }

        return list;
    }

    @Override
    public Map<String, Object> EnvMonitPont(Map<String, Object> map) {
        return getBaseMapper().EnvMonitPont(map);
    }

    @Override
    public List<Map<String, Object>> EarlyWarningType(Map<String, Object> map) {
        return getBaseMapper().EarlyWarningType(map);
    }

    @Override
    public List<EquipmentMonitorPoint> getPointListByIds(List<Integer> ids) {
        return getBaseMapper().getPointListByIds(ids);
    }
    @Override
    public Result getPointAndAtvalueByAssetId(String assetId){
        return Result.ok(getBaseMapper().getPointAndAtvalueByAssetId(assetId));
    }

    @Override
    public EquipmentMonitorPoint getByIdInApi(Integer id) {
        return getBaseMapper().getByIdInApi(id);
    }

}
