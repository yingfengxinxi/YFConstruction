package com.qdoner.datarecept.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.core.entity.BasePage;
import com.qdoner.datarecept.entity.BO.MonitorPointAlertBO;
import com.qdoner.datarecept.entity.PO.BMonitorPointAlert;
import com.kakarote.core.servlet.BaseMapper;
import com.qdoner.datarecept.entity.PO.MonitorPointAlert;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 监测点警报 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
public interface BMonitorPointAlertMapper extends BaseMapper<BMonitorPointAlert> {
    BasePage<BMonitorPointAlert> selectPage(BasePage<Object> parse, @Param("data") MonitorPointAlertBO monitorPointAlertBO);

    BMonitorPointAlert selectById(Integer id);

    BMonitorPointAlert selectByIdInApp(Integer id);

    Map<String,Object> AlarmsNum_project(Map<String, Object> map);

    Map<String,Object> AlarmsNum(Map<String, Object> map);

    List<Map<String,Object>> EarlyWarning(@Param("data") Map<String, Object> map);

    List<Map<String,Object>> MonitSituation(@Param("data") Map<String, Object> map);

    List<Map<String,Object>> MonitSituationPM(@Param("data") Map<String, Object> param);

    List<Map<String,Object>> ProjectAlarmNum(Map<String, Object> map);

    Map<String,Object> alarmsStatistics(@Param("data") Map<String, Object> params);

    List<Map<String,Object>> alarmsTrend(@Param("data") Map<String, Object> map);

    List<Map<String,Object>> realTimeData(@Param("data") Map<String, Object> map);

    BasePage<MonitorPointAlert> selectPageByEnv(BasePage<Object> parse, @Param("data") MonitorPointAlertBO monitorPointAlertBO);

    List<Map<String,Object>> hisAirQualityAnalysis(@Param("data") Map<String, Object> params);

    List<Map<String,Object>> latelyAirQualityAnalysis(@Param("data") Map<String, Object> map);

    Map<String,Object> todayAirQualityAnalysis(@Param("data") Map<String, Object> map);

    List<Map<String,Object>> overView(@Param("data") Map<String, Object> params);

    Map<String, Object> selectAlertNumber(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectAlertList();

    List<Map<String, Object>> selectTypeProportion();

    List<Map<String, Object>> selectAlertTrend(@Param("data") Map<String, Object> map);

    List<Map<String, Object>> selectRaiseDustAlert(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectPMRank(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectProjectAlertNumber(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectNoiseAlert(@Param("data") Map<String, Object> params);

    List<Map<String,Object>> realTimeData2(@Param("data") Map<String, Object> map);

    List<Map<String,Object>> realTimeDataZs(@Param("data") Map<String, Object> map);

    @SqlParser(filter = true)
    List<Map<String,Object>> realTimeDataTemperature(@Param("data") Map<String, Object> map);
    @SqlParser(filter = true)
    List<Map<String,Object>> realTimeDataAll(@Param("data") Map<String, Object> map);

    BasePage<MonitorPointAlert> selectAlarmPage(BasePage<Object> parse, @Param("data") MonitorPointAlertBO monitorPointAlertBO);

    List<Map<String, Object>> selectTypeProportionByParent();

    List<Map<String, Object>> selectAlertListNew();

    MonitorPointAlert selectByPointIdAndMonitorAlertType(@Param("data") Map<String, Object> params);

    int updateByIdAndTenantId(@Param("data") MonitorPointAlert monitorPointAlert);

    List<Integer> getCountBarByPro(@Param("type") String type, @Param("dateList") List<String> dateList,
                                   @Param("eqIdList") List<Integer> eqIdList, @Param("data") Map<String, Object> params);

    List<Map<String, Object>> getCountPieByPro(@Param("dictType") String dictType,
                                               @Param("eqIdList") List<Integer> eqIdList);

    Map<String, Object> selectPM10AndNoiseAlarmQuantity(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectEnvList(@Param("data") Map<String, Object> params);

    List<Map<String,Object>> selectMonitorDataStatistics(Map<String, Object> params);

    @SqlParser(filter = true)
    List<Map<String, Object>> selectExcessiveDust(@Param("value") Double value);

    @SqlParser(filter = true)
    Integer selectExcessiveDustNum(Integer pointId);

    @SqlParser(filter = true)
    List<Map<String, Object>> selectConstantValue();

    @SqlParser(filter = true)
    List<Map<String, Object>> selectConstantLine();

    @SqlParser(filter = true)
    Integer selectConstantLineNum(Integer pointId);

    Map<String,Object> selectDustAlarmNum(Map<String, Object> params);

    Map<String, Object> selectAlertNumberAndTotal(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectUntreatedDataList(@Param("data") MonitorPointAlertBO monitorPointAlertBO);

    Map<String, Object> selectUntreatedData(@Param("data") Map<String, Object> params);

    Map<String, Object> selectEarlyAndAlarmNumber(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectAlertEquiInfo(@Param("data") Map<String, Object> params);
    @SqlParser(filter = true)
    int saveAlertByList(List<BMonitorPointAlert> list);
    @SqlParser(filter = true)
    int insertAlert(@Param("data") BMonitorPointAlert alert);

    List<Map<String, Object>> dayAndNightAlarmNumber(@Param("data") Map<String, Object> param);

    List<Map<String, Object>> selectAlertListByEquiId(@Param("data") Map<String, Object> params);
    // 2022-04-15 新增大屏报警数据总量统计查询
    List<Map<String, Object>> getCountPieByScreenPolice();
    // 2022-04-15  新增大屏报警处理效率统计查询
    List<Map<String, Object>> getCountByPoliceEfficiency();
    // 2022-04-15  新增大屏报警处理日均效率统计查询
    List<Map<String, Object>> getCountByPoliceEfficiencyAverage();

    // 2022-04-18  查询PM2.5、PM10、噪音的数据列表
    List<Map<String, Object>> selectStateList(@Param("data") Map<String, Object> param);
    // 2022-04-18  查询PM2.5、PM10、噪音的数据列表总数
    Map<String, Object> selectStateListCount(@Param("data") Map<String, Object> param);

    BasePage<Map<String,Object>> selectDataList(BasePage<Object> parse, @Param("data") MonitorPointAlertBO monitorPointAlertBO);

    BasePage<Map<String,Object>> selectMonitorDataList(BasePage<Object> parse, @Param("data") MonitorPointAlertBO monitorPointAlertBO);

    Map<String, Object> alarmNumberByDay();

    List<Map<String, Object>> projectAlarmNumNew();

    BasePage<Map<String, Object>> selectPageByDay(BasePage<Object> parse, @Param("data") MonitorPointAlertBO monitorPointAlertBO);

    Map<String, Object> alarmsStatisticsNew(@Param("data") Map<String, Object> params);

    BasePage<MonitorPointAlert> selectAlarmPageNew(BasePage<Object> parse, @Param("data") MonitorPointAlertBO monitorPointAlertBO);

    List<Map<String, Object>> selectAlertTrendNew(@Param("data") Map<String, Object> map);

    List<Map<String, Object>> selectAllAlert(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectAlertListZz();

    List<Map<String, Object>> selectProjectAlertNumberAll(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectProjectAlertNumberTop10(@Param("data") Map<String, Object> params);

    Map<String, Object> todayAirQualityAnalysisNew(@Param("data") Map<String, Object> map);

    List<Map<String, Object>> hisAirQualityAnalysisNew(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> latelyAirQualityAnalysisNew(@Param("data") Map<String, Object> map);

    Map<String, Object> selectAlarmNumByMonitorType(@Param("data") Map<String, Object> params);

    Map<String, Object> selectProjectAndEquiNum(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectProejctNumberRank(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectAlarmListByPro(@Param("data") Map<String, Object> params);
}
