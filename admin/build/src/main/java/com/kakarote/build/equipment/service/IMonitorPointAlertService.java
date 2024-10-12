package com.kakarote.build.equipment.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.equipment.entity.BO.MonitorPointAlertBO;
import com.kakarote.build.equipment.entity.PO.MonitorPointAlert;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 监测点警报 服务类
 * </p>
 *
 * @author shao
 * @since 2021-05-20
 */
public interface IMonitorPointAlertService extends BaseService<MonitorPointAlert> {

    BasePage<MonitorPointAlert> selectPage(MonitorPointAlertBO monitorPointAlertBO);

    MonitorPointAlert selectById(Integer id);

    MonitorPointAlert selectByIdInApp(Integer id);

    int handleSave(MonitorPointAlert MonitorPointAlert);

    Map<String,Object> AlarmsNum_project(Map<String,Object> map);

    Map<String,Object> AlarmsNum(Map<String,Object> map);

    List<Map<String,Object>> EarlyWarning(Map<String,Object> map);

    List<Map<String,Object>> MonitSituation(Map<String,Object> map);

    List<Map<String,Object>> MonitSituationPM(Map<String,Object> param);

    List<Map<String,Object>> ProjectAlarmNum(Map<String,Object> map);

    Map<String,Object> alarmsStatistics(Map<String,Object> params);

    List<Map<String,Object>> alarmsTrend(Map<String,Object> map);

    List<Map<String,Object>> realTimeData(Map<String,Object> map);

    BasePage<MonitorPointAlert> selectPageByEnv(MonitorPointAlertBO monitorPointAlertBO);

    List<Map<String,Object>> hisAirQualityAnalysis(Map<String,Object> params);

    List<Map<String,Object>> latelyAirQualityAnalysis(Map<String,Object> map);

    Map<String,Object> todayAirQualityAnalysis(Map<String,Object> map);

    List<Map<String,Object>> overView(Map<String,Object> params);

    Map<String,Object> selectAlertNumber(Map<String,Object> params);

    List<Map<String,Object>> selectAlertList();

    List<Map<String, Object>> selectTypeProportion();

    List<Map<String,Object>> selectAlertTrend(Map<String, Object> params);

    List<Map<String,Object>> selectRaiseDustAlert(Map<String, Object> params);

    List<Map<String,Object>> selectPMRank(Map<String, Object> params);

    List<Map<String,Object>> selectProjectAlertNumber(Map<String, Object> params);

    List<Map<String,Object>> selectProjectAlertNumberNew(Map<String, Object> params);

    List<Map<String,Object>> selectNoiseAlert(Map<String, Object> params);

    List<Map<String,Object>> realTimeData2(Map<String,Object> map);

    List<Map<String,Object>> realTimeDataZs(Map<String,Object> map);

    List<Map<String,Object>> realTimeDataTemperature(Map<String,Object> map);

    List<Map<String,Object>> realTimeDataAll(Map<String,Object> map);
    
    BasePage<MonitorPointAlert>  selectAlarmPage(MonitorPointAlertBO monitorPointAlertBO);

    List<Map<String, Object>> selectTypeProportionByParent(Map<String, Object> params);

    List<Map<String, Object>> selectAlertListNew();

    JSONObject getCountBarByPro(String proId);

    List<Map<String, Object>> getCountPieByPro(String proId);

    Map<String, Object> selectMonitorDataReport(Map<String, Object> params);

    List<Map<String, Object>> selectMonitorDataStatistics(Map<String,Object> params);

    void appletExcessiveDust();

    Map<String, Object> selectDustAlarmNum(Map<String,Object> params);

    Map<String, Object> selectNumber(Map<String, Object> params);

    /**
     * 根据监测类型查询报警数和总数
     * @param params    monitorType 对应监测项类型
     * @return
     */
    Map<String, Object> selectAlertNumberAndTotal(Map<String, Object> params);

    Map<String, Object> selectState();

    /**
     * 根据监测类型查询未处理的报警数据
     * @param params    monitorType 对应监测项类型
     * @return
     */
    Map<String, Object> selectUntreatedData(Map<String, Object> params);

    List<Map<String, Object>> selectUntreatedDataList(MonitorPointAlertBO monitorPointAlertBO);

    Map<String, Object> selectEarlyAndAlarmNumber(Map<String, Object> params);

    /**
     * 根据type类型查询 未处理报警或预警数据的设备位置信息
     * @param type 1:预警数据 2:报警数据
     * @return
     */
    List<Map<String, Object>> selectAlertEquiInfo(String type);

    /**
    * @Description:品茗数据接口批量添加报警数据
    * @author shz
    * @date 2022-4-7 11:12
    * @param list
    * @return 
    */
    int saveAlertByList(List<MonitorPointAlert> list);
    //工具方法
    MonitorPointAlert copyPastry(MonitorPointAlert copyObj);

    List<Map<String, Object>> dayAndNightAlarmNumber(Map<String, Object> param);

    JSONObject getCountBarByEquiId(Integer equipmentId);

    List<Map<String, Object>> getCountPieByEquiId(Integer equipmentId, String type);

    List<Map<String, Object>> selectAlertListByEquiId(Map<String, Object> params);
	
	// 2022-04-15 新增大屏报警数据总量统计查询
    List<Map<String, Object>> getCountPieByScreenPolice();

    // 2022-04-15  新增大屏报警处理效率统计查询
    Map<String, Object> getCountByPoliceEfficiency();

    // 2022-04-18  新增查询PM2.5、PM10、噪音的数据列表
    Map<String, Object> selectStateList(Map<String, Object> param);

    BasePage<Map<String, Object>> selectDataList(MonitorPointAlertBO monitorPointAlertBO);

    BasePage<Map<String, Object>> selectMonitorDataList(MonitorPointAlertBO monitorPointAlertBO);

    Map<String, Object> alarmNumberByDay();

    List<Map<String, Object>> projectAlarmNumNew();

    BasePage<Map<String, Object>> selectPageByDay(MonitorPointAlertBO monitorPointAlertBO);

    Map<String, Object> alarmsStatisticsNew(Map<String, Object> params);

    BasePage<MonitorPointAlert> selectAlarmPageNew(MonitorPointAlertBO monitorPointAlertBO);

    List<Map<String, Object>> selectAlertTrendNew(Map<String, Object> params);

    List<Map<String,Object>> selectAllAlert(Map<String, Object> params);

    List<Map<String,Object>> selectAlertListZz();

    List<Map<String,Object>> selectProjectAlertNumberAll(Map<String, Object> params);

    List<Map<String,Object>> selectProjectAlertNumberTop10(Map<String, Object> params);

    Map<String, Object> todayAirQualityAnalysisNew(Map<String, Object> map);

    List<Map<String, Object>> hisAirQualityAnalysisNew(Map<String, Object> params);

    List<Map<String, Object>> latelyAirQualityAnalysisNew(Map<String, Object> map);

    Map<String, Object> selectAlarmNumByMonitorType(Map<String, Object> params);

    Map<String, Object> selectProjectAndEquiNum(Map<String, Object> params);

    List<Map<String, Object>> selectProejctNumberRank(Map<String, Object> params);

    List<Map<String, Object>> selectEquipmentNumberRank(Map<String, Object> params);

    List<Map<String, Object>> selectAlarmListByPro(Map<String, Object> params);

    BasePage<Map<String, Object>> selectRealTimeDataList(MonitorPointAlertBO monitorPointAlertBO);

    List<Map<String, Object>> selectMonitorTypeAlertNumber(Map<String, Object> params);

    BasePage<MonitorPointAlert> selectFullAlarmPage(MonitorPointAlertBO monitorPointAlertBO);
}