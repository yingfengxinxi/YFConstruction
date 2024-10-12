package com.kakarote.build.equipment.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataBO;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataEchartsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorData;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorPoint;
import com.kakarote.build.equipment.mapper.EquipmentMonitorDataDayMapper;
import com.kakarote.build.equipment.mapper.EquipmentMonitorDataMapper;
import com.kakarote.build.equipment.mapper.EquipmentMonitorDataMonthMapper;
import com.kakarote.build.equipment.service.IEquipmentMonitorDataService;
import com.kakarote.build.equipment.service.IMonitorPointAlertService;
import com.kakarote.build.utils.CaseUtil;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 设备监测-监测项历史数据表 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-05-20
 */
@Service
public class EquipmentMonitorDataServiceImpl extends BaseServiceImpl<EquipmentMonitorDataMapper, EquipmentMonitorData> implements IEquipmentMonitorDataService {

    @Autowired
    private EquipmentMonitorPointServiceImpl monitorPointService;

    @Autowired
    private IMonitorPointAlertService iMonitorPointAlertService;

    @Autowired
    private EquipmentMonitorDataDayMapper equipmentMonitorDataDayMapper;

    @Autowired
    private EquipmentMonitorDataMonthMapper equipmentMonitorDataMonthMapper;

    @Override
    public BasePage<Map<String, Object>> selectByPage(EquipmentMonitorDataBO monitorDataBO) {

        BasePage<Map<String, Object>> list = getBaseMapper().selectByPage(monitorDataBO.parse(), monitorDataBO);
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject excelImport(MultipartFile file) {
        List<List<Object>> errList = new ArrayList<>();
        String filePath = getFilePath(file);
        ExcelReader reader = ExcelUtil.getReader(filePath);
        List<Map<String, Object>> list = reader.read(2, 3, Integer.MAX_VALUE);

        String message = "";
        Integer errSize = 0;
        List<EquipmentMonitorData> monitorDataList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            boolean isError = false;
            EquipmentMonitorData monitorData = new EquipmentMonitorData();
            EquipmentMonitorPoint monitorPoint = null;

            Map<String, Object> map = list.get(i);

            // 监测项名称
            String pointCode = (String) map.get("pointCode");
            if (pointCode != null && !pointCode.equals("")) {
                // 根据监测项名称查询对应监测点
                LambdaQueryWrapper<EquipmentMonitorPoint> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(EquipmentMonitorPoint::getPointCode, pointCode);
                queryWrapper.last("limit 1");
                monitorPoint = monitorPointService.getOne(queryWrapper);

                // 如果检测点不存在，抛出异常
                if (monitorPoint == null) {
                    message += "第" + (i + 4) + "行监测点编号：" + pointCode + "不存在,请检查数据；";
                    isError = true;
                }
            } else {
                message += "第" + (i + 4) + "行监测点编号不能为空,请检查数据；";
                isError = true;
            }

            // 监测值
            String value = map.get("value").toString();
            if (value == null || value.equals("")) {
                message += "第" + (i + 4) + "行监测值不能为空,请检查数据；";
                isError = true;
            }

            // 监测时间
            String time = String.valueOf(map.get("monitorTime"));
            if (time == null || time.equals("")) {
                message += "第" + (i + 4) + "行监测时间不能为空,请检查数据；";
                isError = true;
            }

            Date monitorTime = null;
            try {
                monitorTime = DateUtil.parseDateTime(time);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("行监测时间格式不规范：" + e);
                message += "第" + (i + 4) + "行监测时间格式不规范,请检查数据；";
                isError = true;
            }

            // 如果当前行的数据不存在错误 设置保存数据
            if (!isError) {


                Date date = new Date();// 获取当前时间
                Long userId = UserUtil.getUser().getUserId();

                monitorData.setPointId(monitorPoint.getId());
                monitorData.setValue(value);
                monitorData.setMonitorTime(monitorTime);
                monitorData.setCreateTime(date);
                monitorData.setCreateUserId(userId);
                monitorData.setTenantId(monitorPoint.getTenantId());


                // 将当前行需要保存的bean对象添加到列表中
                monitorDataList.add(monitorData);
            } else {
                // 否则数据存在错误
                errSize += 1;
            }
        }

        // 删除文件
        FileUtil.del(filePath);
        // 判断是否存在错误数据 存在则不插入到数据库
        JSONObject result = null;
        if (errSize > 0) {
            result = new JSONObject().fluentPut("message", message).fluentPut("errSize", errSize);

        } else {

            // 批量插入
            int num = getBaseMapper().batchSaveData(monitorDataList);
            result = new JSONObject().fluentPut("totalSize", monitorDataList.size()).fluentPut("errSize", 0);
        }

        /*if (errList.size() > 0) {
            BigExcelWriter writer = null;
            try {
                String token = IdUtil.simpleUUID();
                writer = ExcelUtil.getBigWriter(FileUtil.getTmpDirPath() + "/" + token);
                writer.merge(6, "系统用户导入模板(*)为必填项");
                for (int i = 0; i < 7; i++) {
                    writer.setColumnWidth(i, 20);
                }
                writer.setDefaultRowHeight(20);
                Cell cell = writer.getCell(0, 0);
                CellStyle cellStyle = cell.getCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font font = writer.createFont();
                font.setBold(true);
                font.setFontHeightInPoints((short) 16);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                writer.writeHeadRow(Arrays.asList("错误信息", "姓名(*)", "身份证号(*)", "考勤卡号(*)", "打卡方向", "打卡时间"));
                writer.write(errList);
                result.fluentPut("errSize", errList.size()).fluentPut("token", token);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }*/
        return result;
    }

    /**
     * 基坑监测历史数据曲线图
     * 监测项类型 字典项 1：基坑监测、2：塔机监测、3；升降机监测、9：环境监测
     *
     * @param map
     * @return
     */
    @Override
    public Map<String, Object> selectInfo(Map<String, Object> map) {

        Map<String, Object> map1 = getMap(map);

        return map1;
    }

    @Override
    public BasePage<Map<String, Object>> selectAppListByPage(EquipmentMonitorDataBO monitorDataBO) {
        monitorDataBO.setTenantId(UserUtil.getUser().getTenantId());
        monitorDataBO.setProjectId(UserUtil.getUser().getProjectId());
        return getBaseMapper().selectAppListByPage(monitorDataBO.parse(), monitorDataBO);
    }


    public Map<String, Object> getMap(Map<String, Object> map) {
        Map<String, Object> mapAll = new HashMap<>();

        List<Map> maps = getBaseMapper().selectInfo(map);
        List serieData = new ArrayList<>();
        List xData = new ArrayList<>();

        for (Map map1 : maps) {

            serieData.add(map1.get("value"));
            xData.add(map1.get("monitorTime"));
        }

        mapAll.put("serieData", serieData);
        mapAll.put("xData", xData);
        if (maps.size() > 0) {
            mapAll.put("legendData", maps.get(0).get("pointName"));
            mapAll.put("yUnit", maps.get(0).get("unit"));//Y轴单位
        } else {
            mapAll.put("yUnit", null);//Y轴单位
        }
        return mapAll;
    }

    private String getFilePath(MultipartFile file) {
        String dirPath = FileUtil.getTmpDirPath();
        try {
            InputStream inputStream = file.getInputStream();
            File fromStream = FileUtil.writeFromStream(inputStream, dirPath + "/" + IdUtil.simpleUUID() + file.getOriginalFilename());
            return fromStream.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件上传失败：" + e);
            throw new CrmException(SystemCodeEnum.SYSTEM_UPLOAD_FILE_ERROR);
        }
    }

    @Override
    public EquipmentMonitorData selectEquipmentMonitorDataOne(String pointId) {
        EquipmentMonitorData EquipmentMonitorData = getBaseMapper().selectEquipmentMonitorDataOne(pointId);

        return EquipmentMonitorData;
    }

    @Override
    public BasePage<Map<String, Object>> selectListByProject(EquipmentMonitorDataBO monitorDataBO) {
        BasePage<Map<String, Object>> list = null;
        if("1".equals(monitorDataBO.getDataType())){
            monitorDataBO.setOrdersBd("d.monitor_time",false);
            monitorDataBO.setOrdersBd("p.sort",true);
            list = getBaseMapper().selectListByProject(monitorDataBO.parse(), monitorDataBO);
        }else if("2".equals(monitorDataBO.getDataType())){
            monitorDataBO.setOrdersBd("d.start_time",false);
            monitorDataBO.setOrdersBd("p.sort",true);
            list = equipmentMonitorDataDayMapper.selectListByProject(monitorDataBO.parse(), monitorDataBO);
        }else if("3".equals(monitorDataBO.getDataType())){
            monitorDataBO.setOrdersBd("d.start_time",false);
            monitorDataBO.setOrdersBd("p.sort",true);
            list = equipmentMonitorDataMonthMapper.selectListByProject(monitorDataBO.parse(), monitorDataBO);
        }
        return list;
    }

    @Override
    public BasePage<Map<String, Object>> selectMonitorDataList(EquipmentMonitorDataBO monitorDataBO) {
        BasePage<Map<String, Object>> list = getBaseMapper().selectMonitorDataList(monitorDataBO.parse(), monitorDataBO);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectMonitorDataListEcharts(EquipmentMonitorDataEchartsBO monitorDataEchartsBO) {

        List<Map<String, Object>> resultList = new ArrayList<>();
        if (monitorDataEchartsBO.getPointIds() == null || monitorDataEchartsBO.getPointIds().size() <= 0) {
            return resultList;
        }

        // 查询监测点信息
        List<EquipmentMonitorPoint> pointList = monitorPointService.getPointListByIds(monitorDataEchartsBO.getPointIds());

        for (EquipmentMonitorPoint monitorPoint : pointList) {


            Map<String, Object> data = new HashMap<>();
            data.put("name", monitorPoint.getPointCode());
            data.put("type", "line");

            // 查询小时表数据
            List<Map<String, Object>> dataList = getBaseMapper().selectMonitorDataByPointId(monitorPoint.getId(), monitorDataEchartsBO);
            List<List<Object>> list = this.generateListData(dataList);
            data.put("data", list);

            resultList.add(data);
        }

        return resultList;
    }

    /**
     * 生成列表数据  echarts图使用
     *
     * @param dataList
     * @return
     */
    private List<List<Object>> generateListData(List<Map<String, Object>> dataList) {
        List<List<Object>> result = new ArrayList<>();

        for (Map<String, Object> map : dataList) {
            List<Object> list = new ArrayList<>();
            list.add(map.get("monitorTime"));
            list.add(map.get("value"));

            result.add(list);
        }

        return result;
    }


    @Override
    public List<Map<String, Object>> realTimeDataZs(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();

        if (params.get("type") == null) {
            throw new CrmException(3001, "type值不可为空");
        }
        map.put("monitorType", "9");
        if ("1".equals(String.valueOf(params.get("type")))) {//噪音
            map.put("monitorSonType", "3");
            list = iMonitorPointAlertService.realTimeDataZs(map);
//            list = dataMerge(list);
        } else if ("2".equals(String.valueOf(params.get("type")))) {//空气质量
            //PM2.5
            map.put("monitorSonType", "1");
            List<Map<String, Object>> temp1 = iMonitorPointAlertService.realTimeDataZs(map);
//            temp1 = dataMerge(temp1);
            //PM10
            map.put("monitorSonType", "2");
            List<Map<String, Object>> temp2 = iMonitorPointAlertService.realTimeDataZs(map);
//            temp2 = dataMerge(temp2);
            for (int i = 0; i < temp1.size(); i++) {
                Map<String, Object> tempMap1 = temp1.get(i);
                Map<String, Object> tempMap2 = temp2.get(i);
                Map<String, Object> mapp = new HashMap<>();
                mapp.put("date", tempMap1.get("date"));
                mapp.put("hourNumber", tempMap1.get("hourNumber"));
                mapp.put("PM2Count", tempMap1.get("dateCount"));
                mapp.put("PM10Count", tempMap2.get("dateCount"));
                list.add(mapp);
            }
        } else if ("3".equals(String.valueOf(params.get("type")))) {//温湿度
            //温度
            map.put("monitorSonType", "4");
            List<Map<String, Object>> temp1 = iMonitorPointAlertService.realTimeDataZs(map);
//            temp1 = dataMerge(temp1);
            //湿度
            map.put("monitorSonType", "5");
            List<Map<String, Object>> temp2 = iMonitorPointAlertService.realTimeDataZs(map);
//            temp2 = dataMerge(temp2);
            for (int i = 0; i < temp1.size(); i++) {
                Map<String, Object> tempMap1 = temp1.get(i);
                Map<String, Object> tempMap2 = temp2.get(i);
                Map<String, Object> mapp = new HashMap<>();
                mapp.put("date", tempMap1.get("date"));
                mapp.put("hourNumber", tempMap1.get("hourNumber"));
                mapp.put("temperatureCount", tempMap1.get("dateCount"));//温度
                mapp.put("humidityCount", tempMap2.get("dateCount"));//湿度
                list.add(mapp);
            }
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> realTimeDataTemperature(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        map.put("monitorType", "9");
        map.put("monitorSonType", "4");
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
        //获取当前时间String类型
        String time_string = format.format(date);


        map.put("NowDate", time_string);

        map.put("projectid", UserUtil.getUser().getProjectId());
        map.put("tenantid", UserUtil.getUser().getTenantId());
        for (int i = 6; i >= 0; i--) {
            map.put("size", i);
            List<Map<String, Object>> temp1 = iMonitorPointAlertService.realTimeDataTemperature(map);
            if (temp1.size() > 0) {
                Map<String, Object> tempMap1 = temp1.get(0);
                Map<String, Object> mapp = new HashMap<>();
                if (tempMap1.get("zg") != null && !"".equals(tempMap1.get("zg"))) {
                    mapp.put("zg", tempMap1.get("zg"));
                } else {
                    mapp.put("zg", "0");
                }
                if (tempMap1.get("zd") != null && !"".equals(tempMap1.get("zd"))) {
                    mapp.put("zd", tempMap1.get("zd"));
                } else {
                    mapp.put("zd", "0");
                }
                mapp.put("rq", tempMap1.get("rq"));
                list.add(mapp);
            }
        }


        return list;
    }

    @Override
    public List<Map<String, Object>> realTimeDataAll(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        map.put("projectid", UserUtil.getUser().getProjectId());
        map.put("tenantid", UserUtil.getUser().getTenantId());
        List<Map<String, Object>> temp1 = iMonitorPointAlertService.realTimeDataAll(map);
        for (int i = 0; i < temp1.size(); i++) {
            Map<String, Object> tempMap1 = temp1.get(i);
            Map<String, Object> mapp = new HashMap<>();
            if(tempMap1 == null){
                continue;
            }
            if (tempMap1.get("pm2") != null && !"".equals(tempMap1.get("pm2"))) {
                mapp.put("pm2", tempMap1.get("pm2"));
            } else {
                mapp.put("pm2", "0");
            }
            if (tempMap1.get("pm10") != null && !"".equals(tempMap1.get("pm10"))) {
                mapp.put("pm10", tempMap1.get("pm10"));
            } else {
                mapp.put("pm10", "0");
            }
            if (tempMap1.get("zy") != null && !"".equals(tempMap1.get("zy"))) {
                mapp.put("zy", tempMap1.get("zy"));
            } else {
                mapp.put("zy", "0");
            }
            if (tempMap1.get("wd") != null && !"".equals(tempMap1.get("wd"))) {
                mapp.put("wd", tempMap1.get("wd"));
            } else {
                mapp.put("wd", "0");
            }
            if (tempMap1.get("sd") != null && !"".equals(tempMap1.get("sd"))) {
                mapp.put("sd", tempMap1.get("sd"));
            } else {
                mapp.put("sd", "0");
            }
            if (tempMap1.get("fl") != null && !"".equals(tempMap1.get("fl"))) {
                mapp.put("fl", tempMap1.get("fl"));
            } else {
                mapp.put("fl", "暂无数据");
            }
            if (tempMap1.get("fx") != null && !"".equals(tempMap1.get("fx"))) {
                mapp.put("fx", tempMap1.get("fx"));
            } else {
                mapp.put("fx", "0");
            }
            if (tempMap1.get("fs") != null && !"".equals(tempMap1.get("fs"))) {
                mapp.put("fs", tempMap1.get("fs"));
            } else {
                mapp.put("fs", "暂无数据");
            }
            if (tempMap1.get("qy") != null && !"".equals(tempMap1.get("qy"))) {
                mapp.put("qy", tempMap1.get("qy") + "KPa");
            } else {
                mapp.put("qy", "暂无数据");
            }
            list.add(mapp);
        }

        if (list.size()<=0) {
            Map<String, Object> map3 = new HashMap<>();
            map3.put("pm2", "0");
            map3.put("pm10", "0");
            map3.put("zy", "0");
            map3.put("wd", "0");
            map3.put("sd", "0");
            map3.put("fl", "暂无数据");
            map3.put("fx", "0");
            map3.put("fs", "暂无数据");
            map3.put("qy", "暂无数据");
            list.add(map3);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getTowerCraneData(Integer id) {
        return getBaseMapper().getTowerCraneData(id);
    }

    @Override
    public Result getMDataByEq(EquipmentMonitorDataBO monitorDataBO) {
        //获取设备有效点位 拼接sql
        List<EquipmentMonitorPoint> pointList = monitorPointService.lambdaQuery().eq(EquipmentMonitorPoint::getEquipmentId, monitorDataBO.getEquipmentId())
                .eq(EquipmentMonitorPoint::getStatus, "1").orderByAsc(EquipmentMonitorPoint::getSort)
                .list();
        //避免异常
        if (pointList.size() < 1) {
            return Result.error(-1,"设备未配置监测点");
        }
        //拼接
        List<Map<String, String>> colsMap = new ArrayList<>();
        //监测时间
        Map<String, String> mTime = new HashMap<>();
        mTime.put("name", "监测时间");
        mTime.put("key", "monitorTime");
        colsMap.add(mTime);
        String pointsSql = "";
        for (EquipmentMonitorPoint point : pointList) {
            pointsSql += ",sum(CASE p.point_code WHEN '" + point.getPointCode() + "' THEN o.`value` ELSE 0 END ) '"
                    + point.getPointCode() + "'";
            Map<String, String> one = new HashMap<>();
            one.put("name", point.getPointName() + "(" + point.getUnit() + ")");
            one.put("key", CaseUtil.underlineToCamel(point.getPointCode()));
            colsMap.add(one);
        }
        monitorDataBO.setPointsSql(pointsSql);
        BasePage<LinkedHashMap<String, Object>> basePage = getBaseMapper().getMDataByEq(monitorDataBO.parse(), monitorDataBO);
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        linkedHashMap.put("colsMap", colsMap);
        linkedHashMap.put("basePage", basePage);
        return Result.ok(linkedHashMap);
    }

    @Override
    public BasePage<Map<String, Object>> selectListNew(EquipmentMonitorDataBO monitorDataBO) {
        BasePage<Map<String,Object>> list = getBaseMapper().selectListNew(monitorDataBO.parse(), monitorDataBO);
        return list;
    }

}
