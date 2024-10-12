package com.qdoner.datarecept.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.utils.UserUtil;
import com.qdoner.datarecept.entity.BO.MonitorPointAlertBO;
import com.qdoner.datarecept.entity.PO.BMonitorPointAlert;
import com.qdoner.datarecept.entity.PO.MonitorPointAlert;
import com.qdoner.datarecept.mapper.BMonitorPointAlertMapper;
import com.qdoner.datarecept.service.EqStatusCommonService;
import com.qdoner.datarecept.service.IBMonitorPointAlertService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 监测点警报 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
@Service
public class BMonitorPointAlertServiceImpl extends BaseServiceImpl<BMonitorPointAlertMapper, BMonitorPointAlert> implements IBMonitorPointAlertService {

    @Autowired
    private EqStatusCommonService eqStatusCommonService;

    @Override
    public BasePage<BMonitorPointAlert> selectPage(MonitorPointAlertBO monitorPointAlertBO) {

        return getBaseMapper().selectPage(monitorPointAlertBO.parse(), monitorPointAlertBO);
    }

    @Override
    public BMonitorPointAlert selectById(Integer id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    public BMonitorPointAlert selectByIdInApp(Integer id) {
        return null;
    }

    @Override
    public int handleSave(BMonitorPointAlert MonitorPointAlert) {
        return getBaseMapper().updateById(MonitorPointAlert);
    }

    @Override
    public Map<String, Object> AlarmsNum_project(Map<String, Object> map) {
        return getBaseMapper().AlarmsNum_project(map);
    }

    @Override
    public Map<String, Object> AlarmsNum(Map<String, Object> map) {
        return getBaseMapper().AlarmsNum(map);
    }

    @Override
    public List<Map<String, Object>> EarlyWarning(Map<String, Object> map) {
        return getBaseMapper().EarlyWarning(map);
    }

    @Override
    public List<Map<String, Object>> MonitSituation(Map<String, Object> map) {
        return getBaseMapper().MonitSituation(map);
    }

    @Override
    public List<Map<String, Object>> MonitSituationPM(Map<String, Object> param) {
        return getBaseMapper().MonitSituationPM(param);
    }

    @Override
    public List<Map<String, Object>> ProjectAlarmNum(Map<String, Object> map) {
        return getBaseMapper().ProjectAlarmNum(map);
    }

    @Override
    public Map<String, Object> alarmsStatistics(Map<String, Object> params) {
        return getBaseMapper().alarmsStatistics(params);
    }

    @Override
    public List<Map<String, Object>> alarmsTrend(Map<String, Object> map) {
        return getBaseMapper().alarmsTrend(map);
    }

    @Override
    public List<Map<String, Object>> realTimeData(Map<String, Object> map) {
        return getBaseMapper().realTimeData(map);
    }

    @Override
    public BasePage<MonitorPointAlert> selectPageByEnv(MonitorPointAlertBO monitorPointAlertBO) {
        return getBaseMapper().selectPageByEnv(monitorPointAlertBO.parse(), monitorPointAlertBO);
    }

    @Override
    public List<Map<String, Object>> hisAirQualityAnalysis(Map<String, Object> params) {
        return getBaseMapper().hisAirQualityAnalysis(params);
    }

    @Override
    public List<Map<String, Object>> latelyAirQualityAnalysis(Map<String, Object> map) {
        return getBaseMapper().latelyAirQualityAnalysis(map);
    }

    @Override
    public Map<String, Object> todayAirQualityAnalysis(Map<String, Object> map) {
        return getBaseMapper().todayAirQualityAnalysis(map);
    }

    @Override
    public List<Map<String, Object>> overView(Map<String, Object> params) {
        return getBaseMapper().overView(params);
    }

    @Override
    public Map<String, Object> selectAlertNumber(Map<String,Object> params) {
        return getBaseMapper().selectAlertNumber(params);
    }

    @Override
    public List<Map<String, Object>> selectAlertList() {
        return getBaseMapper().selectAlertList();
    }

    @Override
    public List<Map<String, Object>> selectTypeProportion() {
        return getBaseMapper().selectTypeProportion();
    }

    @Override
    public List<Map<String, Object>> selectAlertTrend(Map<String, Object> params) {
        if (params.get("type") == null) {
            throw new CrmException(3001, "type值不可为空");
        }
        Map<String, Object> map = null;
        if ("1".equals(String.valueOf(params.get("type")))) {//七天
            map = dateMap("1");
        } else if ("2".equals(String.valueOf(params.get("type")))) {//30天
            map = dateMap("2");
        }
        if (params.containsKey("projectId") && params.get("projectId") != null) {
            map.put("projectId", params.get("projectId"));
        }
        map.put("monitorType", params.get("monitorType"));
        return getBaseMapper().selectAlertTrend(map);
    }

    @Override
    public List<Map<String, Object>> selectRaiseDustAlert(Map<String, Object> params) {
        if (params.get("type") == null || "".equals(params.get("type"))) {
            throw new CrmException(3001, "type值不可为空");
        }

        List<Map<String, Object>> list = new ArrayList<>();
        // 24小时时段数据查询 type 1:全部数据，type 3:查询最近24小时实时数据, 2:查询所传日期的当日数据
        if ("1".equals(String.valueOf(params.get("type")))) {
//            String date = DateUtil.format(new Date(), "yyyy-MM-dd HH");
            params.put("date", null);//取总数
            list = getBaseMapper().selectRaiseDustAlert(params);
        } else if("3".equals(String.valueOf(params.get("type")))){
            String date = DateUtil.format(new Date(), "yyyy-MM-dd HH");
            params.put("date", date);
            list = getBaseMapper().selectRaiseDustAlert(params);
        } else if ("2".equals(String.valueOf(params.get("type")))) {
            if (params.get("date") == null || "".equals(params.get("date"))) {
                throw new CrmException(3001, "date值不可为空");
            }
            String date = (String) params.get("date");
            date += " 23";
            params.put("date", date);
            list = getBaseMapper().selectRaiseDustAlert(params);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> selectPMRank(Map<String, Object> params) {
        if (params.get("type") == null || "".equals(params.get("type"))) {
            throw new CrmException(3001, "type值不可为空");
        }
        return getBaseMapper().selectPMRank(params);
    }

    @Override
    public List<Map<String, Object>> selectProjectAlertNumber(Map<String, Object> params) {
        return getBaseMapper().selectProjectAlertNumber(params);
    }

    @Override
    public List<Map<String, Object>> selectNoiseAlert(Map<String, Object> params) {
        if (params.get("type") == null || "".equals(params.get("type"))) {
            throw new CrmException(3001, "type值不可为空");
        }

        List<Map<String, Object>> list = new ArrayList<>();
        // 24小时时段数据查询 type 3:查询最近24小时实时数据, 2:查询所传日期的当日数据 ， 1:查全部
        if ("1".equals(String.valueOf(params.get("type")))) {
            String date = DateUtil.format(new Date(), "yyyy-MM-dd HH");
            params.put("date", null);//取总数
            list = getBaseMapper().selectNoiseAlert(params);
        }else   if ("3".equals(String.valueOf(params.get("type")))) {
            String date = DateUtil.format(new Date(), "yyyy-MM-dd HH");
            params.put("date", date);//取24小时
            list = getBaseMapper().selectNoiseAlert(params);
        } else if ("2".equals(String.valueOf(params.get("type")))) {
            if (params.get("date") == null || "".equals(params.get("date"))) {
                throw new CrmException(3001, "date值不可为空");
            }
            String date = (String) params.get("date");
            date += " 23";
            params.put("date", date);
            list = getBaseMapper().selectNoiseAlert(params);
        }

        return list;
    }

    @Override
    public List<Map<String, Object>> realTimeData2(Map<String, Object> map) {
        return getBaseMapper().realTimeData2(map);
    }

    @Override
    public BasePage<MonitorPointAlert> selectAlarmPage(MonitorPointAlertBO monitorPointAlertBO) {
        return getBaseMapper().selectAlarmPage(monitorPointAlertBO.parse(), monitorPointAlertBO);
    }

    @Override
    public List<Map<String, Object>> selectTypeProportionByParent() {
        return getBaseMapper().selectTypeProportionByParent();
    }

    @Override
    public List<Map<String, Object>> selectAlertListNew() {
        return getBaseMapper().selectAlertListNew();
    }


    @Override
    public List<Map<String, Object>> selectMonitorDataStatistics(Map<String, Object> params) {
        return getBaseMapper().selectMonitorDataStatistics(params);
    }

    @Override
    public List<Map<String, Object>> realTimeDataZs(Map<String, Object> map) {
        return getBaseMapper().realTimeDataZs(map);
    }

    @Override
    public List<Map<String, Object>> realTimeDataTemperature(Map<String, Object> map) {
        return getBaseMapper().realTimeDataTemperature(map);
    }

    @Override
    public List<Map<String, Object>> realTimeDataAll(Map<String, Object> map) {
        return getBaseMapper().realTimeDataAll(map);
    }

    /**
     * @param type type=1 查询前一周的时间（包括今天）type=2 查询前一月的时间（包括今天）type=3 查询前一年的月份（包括当月）
     * @return
     */
    private Map<String, Object> dateMap(String type) {
        Map<String, Object> params = new HashMap<>();
        if (type.equals("1")) {
            params.put("format", "%Y-%m-%d");
            params.put("num", "7");
            params.put("dateType", "day");
        } else if (type.equals("2")) {
            params.put("format", "%Y-%m-%d");
            params.put("num", "30");
            params.put("dateType", "day");
        } else if (type.equals("3")) {
            params.put("format", "%Y-%m");
            params.put("num", "12");
            params.put("dateType", "month");
        }
        return params;
    }

    /**
     *
     * @param envList 用来存储每个项目的相关数据
     * @param envListData 需要处理的数据
     * @param type 1：环境报告相关数据；2：AI智能分析相关数据
     */
    private void generateList(List<Map<String, Object>> envList, List<Map<String, Object>> envListData, String type) {
        if (envListData == null || envListData.size() <= 0) {
            return;
        }

        // 当前项目数据
        Map<String, Object> proData = new HashMap<>();
        Integer projectId = (Integer) envListData.get(0).get("projectId"); // 项目id
        String projectName = (String) envListData.get(0).get("projectName"); // 项目名称
        List<Map<String, Object>> detailList = new ArrayList<>(); // 当前项目的报警日期

        // 其他项目数据
        List<Map<String, Object>> otherDataList = new ArrayList<>();

        if (type.equals("1")) {
            // 遍历的数据
            for (int i = 0; i < envListData.size(); i++) {
                // 如果项目id相等 存入时间数据 否则该数据属于其他项目数据
                if (projectId.equals(envListData.get(i).get("projectId"))) {
                    Map<String, Object> detailObj = new HashMap<>();
                    detailObj.put("time", envListData.get(i).get("time"));
                    detailObj.put("value", envListData.get(i).get("value"));
                    detailObj.put("unit", envListData.get(i).get("unit"));
                    detailList.add(detailObj);
                } else {
                    otherDataList.add(envListData.get(i));
                }
            }
        } else if (type.equals("2")) {
            // 遍历的数据
            for (int i = 0; i < envListData.size(); i++) {
                // 如果项目id相等 存入时间数据 否则该数据属于其他项目数据
                if (projectId.equals(envListData.get(i).get("projectId"))) {
                    Map<String, Object> detailObj = new HashMap<>();
                    detailObj.put("time", envListData.get(i).get("time"));
                    detailObj.put("content", envListData.get(i).get("content"));
                    detailObj.put("dataType", envListData.get(i).get("dataType"));
                    detailList.add(detailObj);
                } else {
                    otherDataList.add(envListData.get(i));
                }
            }
        }
        // 将当前项目数据封装到Map中
        proData.put("projectName",projectName);
        proData.put("projectId",projectId);
        proData.put("detailList",detailList);
        proData.put("alarmNumber",detailList.size());
        // 环境数据封装
        envList.add(proData);

        // 递归调用 遍历其他项目数据
        this.generateList(envList, otherDataList, type);
    }

    @Override
    public Map<String, Object> selectDustAlarmNum(Map<String, Object> params) {
        return getBaseMapper().selectDustAlarmNum(params);
    }

    @Override
    public Map<String, Object> selectNumber(Map<String, Object> params) {

        Map<String, Object> result = new HashMap<>();

        // 由于在from 上上使用子查询 不会拼接项目和租户id 所以手动传入数据
        Long tenantId = UserUtil.getUser().getTenantId();
        Long projectId = UserUtil.getUser().getProjectId();

        // 塔机
        params.put("monitorType", "2");
        params.put("tenantId", tenantId);
        params.put("projectId", projectId);
        Map<String, Object> data = this.selectAlertNumberAndTotal(params);
        result.put("towerCrane", data);

        // 升降机
        params.put("monitorType", "3");
        data = this.selectAlertNumberAndTotal(params);
        result.put("lift", data);

        // 烟感
        params.put("monitorType", "13");
        data = this.selectAlertNumberAndTotal(params);
        result.put("somkeDetector", data);

        return result;
    }

    @Override
    public Map<String, Object> selectAlertNumberAndTotal(Map<String, Object> params) {
        // monitorType 对应监测项类型
        if (params.get("monitorType") == null || params.get("monitorType").equals("")) {
            throw new CrmException(500, "参数monitorType不能为空");
        }

        // 查询类型 selectType 1：需要关联查询模型表；2：不需要关联查询模型表
        if (params.get("selectType") == null || params.get("selectType").equals("")) {
            params.put("selectType", "1");
        }

        Map<String, Object> result = getBaseMapper().selectAlertNumberAndTotal(params);

        return result;
    }

    @Override
    public Map<String, Object> selectState() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = new HashMap<>();

        // pm2.5
        params.put("monitorSonType", "1");
        Map<String, Object> data = this.selectUntreatedData(params);
        result.put("pm25", data != null ? data.get("ifOverweight") : null);
        result.put("pm25Value", data != null ? data.get("value")+(String)data.get("unit") : null);
        // pm10
        params.put("monitorSonType", "2");
        data = this.selectUntreatedData(params);
        result.put("pm10", data != null ? data.get("ifOverweight") : null);
        result.put("pm10Value", data!= null ? data.get("value")+(String)data.get("unit") : null);

        // 噪音
        params.put("monitorSonType", "3");
        data = this.selectUntreatedData(params);
        result.put("noise", data!= null ? data.get("ifOverweight") : null);
        result.put("noiseValue", data != null ? data.get("value")+(String)data.get("unit") : null);

        return result;
    }

    @Override
    public Map<String, Object> selectUntreatedData(Map<String, Object> params) {
        // monitorType 对应监测项类型
        if (params.get("monitorSonType") == null || params.get("monitorSonType").equals("")) {
            throw new CrmException(500, "参数monitorType不能为空");
        }

        Map<String, Object> result = getBaseMapper().selectUntreatedData(params);

        return result;
    }

    @Override
    public List<Map<String, Object>> selectUntreatedDataList(MonitorPointAlertBO monitorPointAlertBO) {
        return getBaseMapper().selectUntreatedDataList(monitorPointAlertBO);
    }

    @Override
    public Map<String, Object> selectEarlyAndAlarmNumber(Map<String, Object> params) {
        return getBaseMapper().selectEarlyAndAlarmNumber(params);
    }

    @Override
    public List<Map<String, Object>> selectAlertEquiInfo(String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("iconType", type);
        if (type.equals("1")) {
            params.put("alertType", "0");
        } else if (type.equals("2")) {
            params.put("alertType", "1");
        }

        return getBaseMapper().selectAlertEquiInfo(params);
    }

    @Transactional
    public void saveAlertByList(List<BMonitorPointAlert> list){
        List<String> notIn = new ArrayList<>();
        for(BMonitorPointAlert pointAlert:list){
            BMonitorPointAlert selectPointAlert = new BMonitorPointAlert();
            selectPointAlert.setProjectId(pointAlert.getProjectId());
            selectPointAlert.setTenantId(pointAlert.getTenantId());
            selectPointAlert.setEquipmentId(pointAlert.getEquipmentId());
            selectPointAlert.setContent(pointAlert.getContent());
            selectPointAlert.setHandleState("0");
            QueryWrapper<BMonitorPointAlert> addWrapper =  new QueryWrapper<>(selectPointAlert);
            List<BMonitorPointAlert> notHandleList =  list(addWrapper);
            if(notHandleList == null||notHandleList.size() == 0){
                getBaseMapper().insertAlert(pointAlert);
                eqStatusCommonService.sendMsgEquipment(pointAlert);
            }
            notIn.add(pointAlert.getContent());
        }
        BMonitorPointAlert upPointAlert = new BMonitorPointAlert();
        upPointAlert.setEquipmentId(list.get(0).getEquipmentId());
        upPointAlert.setTenantId(list.get(0).getTenantId());
        upPointAlert.setProjectId(list.get(0).getProjectId());
        upPointAlert.setHandleState("0");
        QueryWrapper<BMonitorPointAlert> upWrapper =  new QueryWrapper<>(upPointAlert);
        upWrapper.notIn("content",notIn);

        List<BMonitorPointAlert> notHandleList =  list(upWrapper);
        for(BMonitorPointAlert pointAlert : notHandleList){
            pointAlert.setHandleState("1");
            pointAlert.setHandleTime(new Date());
            pointAlert.setHandleBy("admin");
            pointAlert.setHandleMeasures("报警解除");
            pointAlert.setHandleConclu("监测值已恢复");
            getBaseMapper().updateById(pointAlert);
        }

        return;
    }

    @Transactional
    public void saveRoomAlertByList(List<BMonitorPointAlert> list){
        List<Integer> idList = new ArrayList<>();
        Integer i = 0;
        for (BMonitorPointAlert pointAlert:list) {
            i++;
            // 更新或插入数据id
            Integer alertId = null;
            BMonitorPointAlert selectPointAlert = new BMonitorPointAlert();
            selectPointAlert.setProjectId(pointAlert.getProjectId());
            selectPointAlert.setTenantId(pointAlert.getTenantId());
            selectPointAlert.setEquipmentId(pointAlert.getEquipmentId());
            selectPointAlert.setHandleState("0");
            selectPointAlert.setContent(pointAlert.getContent());
            /*if (pointAlert.getContent() != null && pointAlert.getContent().contains(content1)) {
                //addWrapper.likeLeft("content", content1);
                selectPointAlert.setContent(content1);
            } else if (pointAlert.getContent() != null && pointAlert.getContent().contains(content2)) {
                //addWrapper.likeLeft("content", content2);
            }*/
            QueryWrapper<BMonitorPointAlert> addWrapper =  new QueryWrapper<>(selectPointAlert);
            addWrapper.orderBy(true,true,"create_time");
            List<BMonitorPointAlert> selectList = this.list(addWrapper);
            if (selectList != null && selectList.size() == 0) {
                // 保存报警数据
                this.baseMapper.insertAlert(pointAlert);
                eqStatusCommonService.sendMsgEquipment(pointAlert);
                idList.add(pointAlert.getId());
            } else {
                // 更新最新一条相同类型的，报警数据的报警值和报警内容，为当前的报警值和报警内容。
                BMonitorPointAlert upPointAlert = selectList.get(selectList.size() - 1);
                upPointAlert.setContent(pointAlert.getContent());
                upPointAlert.setMonitorData(pointAlert.getMonitorData());
                this.baseMapper.updateById(upPointAlert);
                idList.add(upPointAlert.getId());
            }
        }

        // 恢复方法
        BMonitorPointAlert recoverObj = new BMonitorPointAlert();
        recoverObj.setEquipmentId(list.get(0).getEquipmentId());
        recoverObj.setTenantId(list.get(0).getTenantId());
        recoverObj.setProjectId(list.get(0).getProjectId());
        recoverObj.setHandleState("0");
        QueryWrapper<BMonitorPointAlert> upWrapper =  new QueryWrapper<>(recoverObj);
        upWrapper.notIn("id",idList);
        upWrapper.notLike("content","已离线");

        List<BMonitorPointAlert> notHandleList =  list(upWrapper);
        for(BMonitorPointAlert updateAlert : notHandleList){
            updateAlert.setHandleState("1");
            updateAlert.setHandleTime(new Date());
            updateAlert.setHandleBy("admin");
            updateAlert.setHandleMeasures("报警解除");
            updateAlert.setHandleConclu("监测值已恢复");
            getBaseMapper().updateById(updateAlert);
        }
        return;
    }

    @Override
    public void updataAlert(BMonitorPointAlert monitorPointAlert) {
        monitorPointAlert.setHandleState("0");
        QueryWrapper<BMonitorPointAlert> upWrapper =  new QueryWrapper<>(monitorPointAlert);
        List<BMonitorPointAlert> notHandleList =  list(upWrapper);
        for(BMonitorPointAlert pointAlert : notHandleList){
            pointAlert.setHandleState("1");
            pointAlert.setHandleTime(new Date());
            pointAlert.setHandleBy("admin");
            pointAlert.setHandleMeasures("报警解除");
            pointAlert.setHandleConclu("监测值已恢复");
            getBaseMapper().updateById(pointAlert);
        }
    }


    public BMonitorPointAlert copyPastry(BMonitorPointAlert copyObj){
        BMonitorPointAlert mMonitorPointAlert = new BMonitorPointAlert();
        mMonitorPointAlert.setProjectId(copyObj.getProjectId());
        mMonitorPointAlert.setTenantId(copyObj.getTenantId());
        mMonitorPointAlert.setEquipmentId(copyObj.getEquipmentId());
        mMonitorPointAlert.setMonitorPointId(copyObj.getMonitorPointId());
        mMonitorPointAlert.setCreateTime(new Date());
        mMonitorPointAlert.setHandleState("0");
        return mMonitorPointAlert;
    }

    @Override
    public List<Map<String, Object>> dayAndNightAlarmNumber(Map<String, Object> param) {
        return getBaseMapper().dayAndNightAlarmNumber(param);
    }

    @Override
    public List<Map<String, Object>> getCountPieByEquiId(Integer equipmentId, String type) {
        //mapper 参数
        List<Integer> eqIdList = new ArrayList<>();
        eqIdList.add(equipmentId);

        //监测子项
        String dictType = "";

        if (type.equals("10")) {
            dictType = "b_equipment_monitor_type_1"; // 基坑子监测项
        } else {
            dictType = "b_equipment_monitor_type_11"; // 高支模子监测项
        }

        //List<AdminDictData> dictList = adminService.dictType(dictType).getData();
        /*if (dictList == null) {
            log.error("Eorror => Monitor son type is null");
            return null;
        }*/
        List<Map<String, Object>> result = this.getBaseMapper().getCountPieByPro(dictType, eqIdList);
        return result;
    }

    @Override
    public List<Map<String, Object>> selectAlertListByEquiId(Map<String, Object> params) {
        return getBaseMapper().selectAlertListByEquiId(params);
    }
    // 2022-04-15 新增大屏报警数据总量统计查询
    @Override
    public List<Map<String, Object>> getCountPieByScreenPolice() {
        List<Map<String, Object>> result = this.getBaseMapper().getCountPieByScreenPolice();
        if (result == null || result.size() == 0) {
            String[] labelList = {"视频监控", "环境监测", "AI危险源识别", "车辆智能分析", "塔机监测", "卸料平台监测"};
            for (int i = 0; i<labelList.length; i++) {
                Map<String, Object> obj = new HashMap<>();
                obj.put("name", labelList[i]);
                obj.put("value", 0);
                result.add(obj);
            }
        }
        return result;
    }

    // 2022-04-15 新增大屏报警处理效率统计查询
    @Override
    public Map<String, Object> getCountByPoliceEfficiency() {
        Map <String, Object> map=new HashMap<String, Object>();
        List<Map<String, Object>> result = this.getBaseMapper().getCountByPoliceEfficiency();
        double zs=0;
        for(int i=0;i<result.size();i++){
            Map<String, Object> data=result.get(i);
            Object ob = data.get("xffz");
            String str= String.valueOf(ob);
            if(str== null || str==""){
                zs =zs+0;
            }else{
                zs =zs+Double.valueOf(str);
            }
        }
        double pjs= 0;
        if(zs!=0){
            pjs=zs/result.size();
        }
        map.put("pjs",String.format("%.2f", pjs));

        List<Map<String, Object>> averageList = this.getBaseMapper().getCountByPoliceEfficiencyAverage();
        double averageZs=0;
        for(int i=0;i<averageList.size();i++){
            Map<String, Object> data=averageList.get(i);
            Object ob = data.get("zs");
            String str= String.valueOf(ob);
            int zts= Integer.valueOf(str);
            ob = data.get("cls");
            str= String.valueOf(ob);
            int cls= Integer.valueOf(str);
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            String percentage = numberFormat.format((float) cls/(float) zts * 100);
            averageZs=averageZs+Double.valueOf(percentage);
        }
        double averagepjs=0;
        if(averageZs!=0){
            averagepjs=averageZs/averageList.size();
        }
        map.put("jcl",String.format("%.2f", averagepjs));
        return map;
    }

    // 2022-04-18  新增查询PM2.5、PM10、噪音的数据列表
    @Override
    public Map<String, Object> selectStateList(Map<String, Object> param) {
        Map <String, Object> map=new HashMap<String, Object>();
        int page =0;
        int limit =0;
        // monitorType 对应监测项类型
        if (param.get("monitorSonType") == null || param.get("monitorSonType").equals("")) {
            throw new CrmException(500, "参数monitorType不能为空");
        }
        Object type =param.get("monitorSonType");
        String monitorSonType =String.valueOf(type);
        if("0000".equals(monitorSonType)){
            param.put("monitorSonType","");
        }
        if (param.get("page") == null || param.get("page").equals("")) {
            page=1;
        }else{
            Object conversion= param.get("page");
            page=Integer.valueOf(String.valueOf(conversion));

        }
        if (param.get("limit") == null || param.get("limit").equals("")) {
            limit =10;
        }else{
            Object conversion= param.get("limit");
            limit=Integer.valueOf(String.valueOf(conversion));
        }
        if (param.get("niandu") != null && !param.get("niandu").equals("")) {
            Object conversion= param.get("niandu");
            String niandu=String.valueOf(conversion);
            if("1".equals(niandu)){
                param.put("date",stringDate(new Date(),"yyyy-MM-dd"));
            }else if("2".equals(niandu)){
                Map dateList =getWeekDate();
                param.put("dateState",dateList.get("mondayDate"));
                param.put("dateEnd",dateList.get("sundayDate"));
            }else if("3".equals(niandu)){
                param.put("mouth",stringDate(new Date(),"yyyy-MM"));
            }else if("4".equals(niandu)){
                param.put("year",stringDate(new Date(),"yyyy"));
            }
        }
        param.put("page",(page-1)*limit);
        param.put("limit",limit);
        List<Map<String, Object>> result = this.getBaseMapper().selectStateList(param);// 列表数据
        Map<String, Object>  zs = this.getBaseMapper().selectStateListCount(param);// 分页用总数
        map.put("list",result);
        map.put("totalRow",zs.get("zs"));
        return map;
    }

    @Override
    public BasePage<Map<String, Object>> selectDataList(MonitorPointAlertBO monitorPointAlertBO) {
        return getBaseMapper().selectDataList(monitorPointAlertBO.parse(),monitorPointAlertBO);
    }

    @Override
    public BasePage<Map<String, Object>> selectMonitorDataList(MonitorPointAlertBO monitorPointAlertBO) {
        return getBaseMapper().selectMonitorDataList(monitorPointAlertBO.parse(),monitorPointAlertBO);
    }

    @Override
    public Map<String, Object> alarmNumberByDay() {
        return getBaseMapper().alarmNumberByDay();
    }

    @Override
    public List<Map<String, Object>> projectAlarmNumNew() {
        return getBaseMapper().projectAlarmNumNew();
    }

    @Override
    public BasePage<Map<String, Object>> selectPageByDay(MonitorPointAlertBO monitorPointAlertBO) {
        return getBaseMapper().selectPageByDay(monitorPointAlertBO.parse(),monitorPointAlertBO);
    }

    @Override
    public Map<String, Object> alarmsStatisticsNew(Map<String, Object> params) {
        return getBaseMapper().alarmsStatisticsNew(params);
    }

    @Override
    public BasePage<MonitorPointAlert> selectAlarmPageNew(MonitorPointAlertBO monitorPointAlertBO) {
        return getBaseMapper().selectAlarmPageNew(monitorPointAlertBO.parse(), monitorPointAlertBO);
    }

    @Override
    public List<Map<String, Object>> selectAlertTrendNew(Map<String, Object> params) {
        if (params.get("type") == null) {
            throw new CrmException(3001, "type值不可为空");
        }
        Map<String, Object> map = null;
        if ("1".equals(String.valueOf(params.get("type")))) {//七天
            map = dateMap("1");
        } else if ("2".equals(String.valueOf(params.get("type")))) {//30天
            map = dateMap("2");
        }
        if (params.containsKey("projectId") && params.get("projectId") != null) {
            map.put("projectId", params.get("projectId"));
        }
        return getBaseMapper().selectAlertTrendNew(map);
    }

    public static String stringDate(Date date,String format){
        try {
            DateFormat dft = new SimpleDateFormat(format);
            String d = dft.format(date);
            return d;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前时间所在周的周一和周日的日期时间
     * @return
     */
    public static Map<String,String> getWeekDate() {
        Map<String,String> map = new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if(dayWeek==1){
            dayWeek = 8;
        }

        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        Date mondayDate = cal.getTime();
        String weekBegin = sdf.format(mondayDate);

        cal.add(Calendar.DATE, 4 +cal.getFirstDayOfWeek());
        Date sundayDate = cal.getTime();
        String weekEnd = sdf.format(sundayDate);

        map.put("mondayDate", weekBegin);
        map.put("sundayDate", weekEnd);
        return map;
    }

    @Override
    public List<Map<String, Object>> selectAllAlert(Map<String, Object> params) {
        if (params.get("type") == null || "".equals(params.get("type"))) {
            throw new CrmException(3001, "type值不可为空");
        }

        List<Map<String, Object>> list = new ArrayList<>();
        // 24小时时段数据查询 type 1:查询最近24小时实时数据, 2:查询所传日期的当日数据
        if ("1".equals(String.valueOf(params.get("type")))) {
            String date = DateUtil.format(new Date(), "yyyy-MM-dd HH");
            params.put("date", null);//取总数
            list = getBaseMapper().selectAllAlert(params);
        } else if ("2".equals(String.valueOf(params.get("type")))) {
            if (params.get("date") == null || "".equals(params.get("date"))) {
                throw new CrmException(3001, "date值不可为空");
            }
            String date = (String) params.get("date");
            date += " 23";
            params.put("date", date);
            list = getBaseMapper().selectAllAlert(params);
        }

        return list;
    }

    @Override
    public List<Map<String, Object>> selectAlertListZz() {
        return getBaseMapper().selectAlertListZz();
    }

    @Override
    public List<Map<String, Object>> selectProjectAlertNumberAll(Map<String, Object> params) {
        return getBaseMapper().selectProjectAlertNumberAll(params);
    }

    @Override
    public List<Map<String, Object>> selectProjectAlertNumberTop10(Map<String, Object> params) {
        return getBaseMapper().selectProjectAlertNumberTop10(params);
    }

    @Override
    public Map<String, Object> todayAirQualityAnalysisNew(Map<String, Object> map) {
        return getBaseMapper().todayAirQualityAnalysisNew(map);
    }

    @Override
    public List<Map<String, Object>> hisAirQualityAnalysisNew(Map<String, Object> params) {
        return getBaseMapper().hisAirQualityAnalysisNew(params);
    }

    @Override
    public List<Map<String, Object>> latelyAirQualityAnalysisNew(Map<String, Object> map) {
        return getBaseMapper().latelyAirQualityAnalysisNew(map);
    }

    @Override
    public Map<String, Object> selectAlarmNumByMonitorType(Map<String, Object> params) {
        return getBaseMapper().selectAlarmNumByMonitorType(params);
    }

    @Override
    public Map<String, Object> selectProjectAndEquiNum(Map<String, Object> params) {
        return getBaseMapper().selectProjectAndEquiNum(params);
    }

    @Override
    public List<Map<String, Object>> selectProejctNumberRank(Map<String, Object> params) {
        return getBaseMapper().selectProejctNumberRank(params);
    }

    @Override
    public List<Map<String, Object>> selectAlarmListByPro(Map<String, Object> params) {
        return getBaseMapper().selectAlarmListByPro(params);
    }

}
