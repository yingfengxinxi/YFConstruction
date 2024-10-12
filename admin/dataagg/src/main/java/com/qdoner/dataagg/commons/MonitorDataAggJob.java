package com.qdoner.dataagg.commons;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ctc.wstx.util.DataUtil;
import com.qdoner.dataagg.entity.PO.*;
import com.qdoner.dataagg.service.*;
import com.qdoner.dataagg.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;

/**
 * 数据聚合JOB
 */
@Component
@Slf4j
public class MonitorDataAggJob {

    @Autowired
    private IMonitorPointService pointService;
    @Autowired
    private IMonitorDataService dataService;
    @Autowired
    private IMonitorDataDayService dataDayService;
    @Autowired
    private IMonitorDataMonthService monthService;
    @Autowired
    private IMonitorDataYearService yearService;

    /**
     * 每分钟计算 日、月、年最近周期内数据
     */
    @Scheduled(cron = "10 0/1 * * * ?")
    //@Scheduled(initialDelay = 1000, fixedDelay = 100000)
    @Transactional(rollbackFor = Exception.class)
    public void monitoringDataNear() {
        long start = System.currentTimeMillis();
        //当前时间
        Date nowDateTime = new Date();
        //
        String startTimeDay = DateUtil.formatDateStr(nowDateTime, "yyyy-MM-dd HH:00:00");
        String startTimeMonth = DateUtil.formatDateStr(nowDateTime, "yyyy-MM-dd 00:00:00");
        String startTimeYear = DateUtil.formatDateStr(nowDateTime, "yyyy-MM-01 00:00:00");
        //日月、月
        //1、获取所有有效点位
        List<MonitorPoint> pointList = pointService.lambdaQuery().eq(MonitorPoint::getStatus, "1")
                .isNotNull(MonitorPoint::getTenantId)
                .isNotNull(MonitorPoint::getProjectId)
                .eq(MonitorPoint::getMonitorType, "9")
                .eq(MonitorPoint::getStatus, "1").list();
        for (MonitorPoint point : pointList) {
            //日
            List<MonitorData> dataDayList = dataService.lambdaQuery()
                    .eq(MonitorData::getPointId, point.getId())
                    .isNotNull(MonitorData::getTenantId)
                    .isNotNull(MonitorData::getProjectId)
                    .ge(MonitorData::getMonitorTime, startTimeDay)
                    .lt(MonitorData::getMonitorTime, nowDateTime)
                    .list();
            double minDay = 0.00;
            double maxDay = 0.00;
            BigDecimal tValueDay = new BigDecimal(0.00);//总数
            if (dataDayList.size() > 0) {
                int time = 0;//次数
                int size;//数据条数
                for (MonitorData data : dataDayList) {
                    String value = data.getValue();
                    if (StringUtils.isEmpty(value)) {
                        value = "0";
                    }
                    tValueDay = tValueDay.add(new BigDecimal(value));
                    //最大最小值
                    double dValue = Double.valueOf(value);
                    if (time == 0) {
                        minDay = dValue;
                        maxDay = dValue;
                        time++;
                    } else {
                        if (minDay > dValue) {
                            minDay = dValue;
                        }
                        if (maxDay < dValue) {
                            maxDay = dValue;
                        }
                    }
                }
                size = dataDayList.size();

                if (point.getStatisticalType().equals("2")) {
                    tValueDay = tValueDay.divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP);
                }
            }
            List<MonitorDataDay> listDay = dataDayService.lambdaQuery()
                    .eq(MonitorDataDay::getPointId, point.getId())
                    .eq(MonitorDataDay::getProjectId, point.getProjectId())
                    .eq(MonitorDataDay::getTenantId, point.getTenantId())
                    .eq(MonitorDataDay::getIsNear, "1").list();
            MonitorDataDay monitorDataDay = new MonitorDataDay();
            if (listDay.size() > 0) {
                monitorDataDay = listDay.get(0);
            } else {
                monitorDataDay.setPointId(point.getId());
                monitorDataDay.setStatisticalType(point.getStatisticalType());
                monitorDataDay.setProjectId(point.getProjectId());
                monitorDataDay.setTenantId(point.getTenantId());
                monitorDataDay.setIsNear("1");
            }
            monitorDataDay.setValue(tValueDay.toString());
            monitorDataDay.setStartTime(DateUtil.parseDate(startTimeDay, DateUtil.YMDHMS_));
            monitorDataDay.setEndTime(nowDateTime);
            monitorDataDay.setCreateTime(new Date());
            monitorDataDay.setMaxValue(String.valueOf(maxDay));
            monitorDataDay.setMinValue(String.valueOf(minDay));
            if (listDay.size() > 0) {
                dataDayService.updateById(monitorDataDay);
            } else {
                dataDayService.save(monitorDataDay);
            }

            //月
            List<MonitorData> dataMonthList = dataService.lambdaQuery()
                    .eq(MonitorData::getPointId, point.getId())
                    .isNotNull(MonitorData::getTenantId)
                    .isNotNull(MonitorData::getProjectId)
                    .ge(MonitorData::getMonitorTime, startTimeMonth)
                    .lt(MonitorData::getMonitorTime, nowDateTime)
                    .list();
            double minMonth = 0.00;
            double maxMonth = 0.00;
            BigDecimal tValueMonth = new BigDecimal(0.00);//总数
            if (dataMonthList.size() > 0) {
                int time = 0;//次数
                int size;//数据条数
                for (MonitorData data : dataMonthList) {
                    String value = data.getValue();
                    if (StringUtils.isEmpty(value)) {
                        value = "0";
                    }
                    tValueMonth = tValueMonth.add(new BigDecimal(value));
                    //最大最小值
                    double dValue = Double.valueOf(value);
                    if (time == 0) {
                        minMonth = dValue;
                        maxMonth = dValue;
                        time++;
                    } else {
                        if (minMonth > dValue) {
                            minMonth = dValue;
                        }
                        if (maxMonth < dValue) {
                            maxMonth = dValue;
                        }
                    }
                }
                size = dataMonthList.size();

                if (point.getStatisticalType().equals("2")) {
                    tValueMonth = tValueMonth.divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP);
                }
            }

            List<MonitorDataMonth> listMonth = monthService.lambdaQuery()
                    .eq(MonitorDataMonth::getPointId, point.getId())
                    .eq(MonitorDataMonth::getProjectId, point.getProjectId())
                    .eq(MonitorDataMonth::getTenantId, point.getTenantId())
                    .eq(MonitorDataMonth::getIsNear, "1").list();
            MonitorDataMonth monitorDataMonth = new MonitorDataMonth();
            if (listMonth.size() > 0) {
                monitorDataMonth = listMonth.get(0);
            } else {
                monitorDataMonth.setPointId(point.getId());
                monitorDataMonth.setStatisticalType(point.getStatisticalType());
                monitorDataMonth.setProjectId(point.getProjectId());
                monitorDataMonth.setTenantId(point.getTenantId());
                monitorDataMonth.setIsNear("1");
            }
            monitorDataMonth.setValue(tValueMonth.toString());
            monitorDataMonth.setStartTime(DateUtil.parseDate(startTimeMonth, DateUtil.YMDHMS_));
            monitorDataMonth.setEndTime(nowDateTime);
            monitorDataMonth.setCreateTime(new Date());
            monitorDataMonth.setMaxValue(String.valueOf(maxMonth));
            monitorDataMonth.setMinValue(String.valueOf(minMonth));
            if (listMonth.size() > 0) {
                monthService.updateById(monitorDataMonth);
            } else {
                monthService.save(monitorDataMonth);
            }
        }
        //废弃2021-12-30 月计算完成后=>年
        /*for (MonitorPoint point : pointList) {
            //最大最小值
            double min = 0.00;
            double max = 0.00;
            BigDecimal tValue = new BigDecimal(0.00);//总数
            int time = 0;//次数
            int size;//数据条数
            List<MonitorDataMonth> dataList = monthService.lambdaQuery()
                    .eq(MonitorDataMonth::getPointId, point.getId())
                    .eq(MonitorDataMonth::getProjectId, point.getProjectId())
                    .eq(MonitorDataMonth::getTenantId, point.getTenantId())
                    .gt(MonitorDataMonth::getCreateTime, startTimeYear)
                    .lt(MonitorDataMonth::getCreateTime, nowDateTime)
                    .list();
            if (dataList.size() < 1) {
                continue;
            }
            for (MonitorDataMonth data : dataList) {
                String value = data.getValue();
                if (StringUtils.isEmpty(value)) {
                    value = "0";
                }
                tValue = tValue.add(new BigDecimal(value));
                //最大最小值
                double dValue = Double.valueOf(value);
                if (time == 0) {
                    min = dValue;
                    max = dValue;
                    time++;
                } else {
                    if (min > dValue) {
                        min = dValue;
                    }
                    if (max < dValue) {
                        max = dValue;
                    }
                }
            }
            //
            size = dataList.size();

            if (point.getStatisticalType().equals("2")) {
                tValue = tValue.divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP);
            }

            List<MonitorDataYear> yearList = yearService.lambdaQuery()
                    .eq(MonitorDataYear::getPointId, point.getId())
                    .eq(MonitorDataYear::getProjectId, point.getProjectId())
                    .eq(MonitorDataYear::getTenantId, point.getTenantId())
                    .eq(MonitorDataYear::getIsNear, "1").list();
            MonitorDataYear monitorDataYear = new MonitorDataYear();
            if (yearList.size() > 0) {
                monitorDataYear = yearList.get(0);
            } else {
                monitorDataYear.setPointId(point.getId());
                monitorDataYear.setStatisticalType(point.getStatisticalType());
                monitorDataYear.setProjectId(point.getProjectId());
                monitorDataYear.setTenantId(point.getTenantId());
                monitorDataYear.setIsNear("1");
            }
            monitorDataYear.setValue(tValue.toString());
            monitorDataYear.setStartTime(DateUtil.parseDate(startTimeYear, DateUtil.YMDHMS_));
            monitorDataYear.setEndTime(nowDateTime);
            monitorDataYear.setCreateTime(new Date());
            monitorDataYear.setMaxValue(String.valueOf(max));
            monitorDataYear.setMinValue(String.valueOf(min));
            if (yearList.size() > 0) {
                yearService.updateById(monitorDataYear);
            } else {
                yearService.save(monitorDataYear);
            }
        }*/
        long end = System.currentTimeMillis();
        log.info("MINUTE END AT " + DateUtil.formatDateStr(new Date(), DateUtil.YMDHMS_) + "，用时：" + (end - start) + "毫秒");
    }

    /**
     * 每整小时执行 为避免时间格式错误，可延时5秒执行 即整点零5秒
     */
    @PostConstruct
    @Scheduled(cron = "5 0 0/1 * * ?")
    //@Scheduled(initialDelay = 1000, fixedDelay = 100000)
    @Transactional(rollbackFor = Exception.class)
    public void monitoringDataDay() throws Exception {
        log.info("DAY start at " + DateUtil.formatDateStr(new Date(), DateUtil.YMDHMS_) + " ……");
        long start = System.currentTimeMillis();
        String format = "yyyy-MM-dd HH:00:00";
        Date nowTime = DateUtil.formatDate(new Date(), format);
        //2022-10-28 离线的设备
        List<Map<String, String>> assets = pointService.getAssetStatus();
        Map<String, Boolean> onlines = new HashMap<>();
        assets.forEach(a -> {
            String online = a.get("online");
            if("0".equals(online)){
                onlines.put(a.get("assetId"), false);
            }
            if("1".equals(online)){
                onlines.put(a.get("assetId"), true);
            }
        });
        //1、获取所有有效点位
        List<MonitorPoint> pointList = pointService.lambdaQuery().eq(MonitorPoint::getStatus, "1")
                .isNotNull(MonitorPoint::getTenantId)
                .isNotNull(MonitorPoint::getProjectId)
                .eq(MonitorPoint::getMonitorType, "9")
                .eq(MonitorPoint::getStatus, "1").list();
        for (MonitorPoint point : pointList) {
//            if (onlines.get(point.getEquipmentId()) == null || !onlines.get(point.getEquipmentId())) {
//                continue;
//            }
            //获取此次聚合开始时间
            String startTime = null;
            //是否已存在聚合数据
            boolean flag = true;
            List<MonitorDataDay> dataDayList = dataDayService.lambdaQuery()
                    .eq(MonitorDataDay::getPointId, point.getId())
                    .eq(MonitorDataDay::getProjectId, point.getProjectId())
                    .eq(MonitorDataDay::getTenantId, point.getTenantId())
                    .ne(MonitorDataDay::getIsNear, "1")
                    .orderByDesc(MonitorDataDay::getCreateTime).list();
            if (dataDayList.size() > 0) {//存在聚合数据则增量聚合
                //获取聚合开始整点时间
                startTime = DateUtil.formatDateStr(dataDayList.get(0).getCreateTime(), format);
                flag = false;
            }
            if (flag) {//需要重新解析聚合
                List<MonitorData> monitorDataList = dataService.lambdaQuery()
                        .eq(MonitorData::getPointId, point.getId())
                        .isNotNull(MonitorData::getTenantId)
                        .isNotNull(MonitorData::getProjectId)
                        .orderByAsc(MonitorData::getMonitorTime).list();
                if (monitorDataList.size() > 0) {
                    //获取聚合开始整点时间
                    startTime = DateUtil.formatDateStr(monitorDataList.get(0).getMonitorTime(), format);
                }
            }
            if (StringUtils.isEmpty(startTime)) {
                continue;
            }
            //日数据
            List<MonitorDataDay> dataDays = new ArrayList<>();
            String endTime = DateUtil.addHours(startTime, 1);
            while (nowTime.compareTo(DateUtil.parseDate(endTime, DateUtil.YMDHMS_)) > -1) {
                //最大最小值
                double min = 0.00;
                double max = 0.00;
                BigDecimal tValue = new BigDecimal(0.00);//总数
                int time = 0;//次数
                int size;//数据条数

                List<MonitorData> dataList = dataService.lambdaQuery()
                        .eq(MonitorData::getPointId, point.getId())
                        .isNotNull(MonitorData::getTenantId)
                        .isNotNull(MonitorData::getProjectId)
                        .ge(MonitorData::getMonitorTime, startTime)
                        .lt(MonitorData::getMonitorTime, endTime)
                        .list();
                if (dataList.size() < 1) {
                    startTime = endTime;
                    endTime = DateUtil.addHours(startTime, 1);
                    continue;
                }
                for (MonitorData data : dataList) {
                    String value = data.getValue();
                    if (StringUtils.isEmpty(value)) {
                        value = "0";
                    }
                    tValue = tValue.add(new BigDecimal(value));
                    //最大最小值
                    double dValue = Double.valueOf(value);
                    if (time == 0) {
                        min = dValue;
                        max = dValue;
                        time++;
                    } else {
                        if (min > dValue) {
                            min = dValue;
                        }
                        if (max < dValue) {
                            max = dValue;
                        }
                    }
                }
                //
                size = dataList.size();

                if (point.getStatisticalType().equals("2")) {
                    tValue = tValue.divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP);
                }
                MonitorDataDay monitorDataDay = new MonitorDataDay();
                monitorDataDay.setPointId(point.getId());
                monitorDataDay.setStatisticalType(point.getStatisticalType());
                monitorDataDay.setValue(tValue.toString());

                monitorDataDay.setStartTime(DateUtil.parseDate(startTime, DateUtil.YMDHMS_));
                monitorDataDay.setEndTime(DateUtil.parseDate(endTime, DateUtil.YMDHMS_));
                monitorDataDay.setCreateTime(new Date());

                monitorDataDay.setProjectId(point.getProjectId());
                monitorDataDay.setTenantId(point.getTenantId());
                monitorDataDay.setMaxValue(String.valueOf(max));
                monitorDataDay.setMinValue(String.valueOf(min));
                dataDays.add(monitorDataDay);
                log.info("日点位=>" + point.getPointName() + "[" + point.getId() + "],完成【"
                        + endTime + "】聚合 => tValue:" + tValue + "|max=" + max + "|min=" + min);
                //处理时间
                startTime = endTime;
                endTime = DateUtil.addHours(startTime, 1);
            }
            if (dataDays.size() > 0) {
                dataDayService.saveBatch(dataDays, 100);
            }
        }
        long end = System.currentTimeMillis();
        log.info("DAY end at " + DateUtil.formatDateStr(new Date(), DateUtil.YMDHMS_) + "，用时：" + (end - start) + "毫秒");
    }

    /**
     * 每日零点执行 为避免时间格式错误，可延时5秒执行 即整点零5秒
     */
    //@PostConstruct
    @Scheduled(cron = "5 0 0 1/1 * ?")
    //@Scheduled(initialDelay = 10000, fixedDelay = 100000)
    @Transactional(rollbackFor = Exception.class)
    public void monitoringDataMonth() throws Exception {
        log.info("MONTH start at " + DateUtil.formatDateStr(new Date(), DateUtil.YMDHMS_) + " ……");
        long start = System.currentTimeMillis();
        String format = "yyyy-MM-dd 00:00:00";
        Date nowTime = DateUtil.formatDate(new Date(), format);
        //2022-10-28 离线的设备
        List<Map<String, String>> assets = pointService.getAssetStatus();
        Map<String, Boolean> onlines = new HashMap<>();
        assets.forEach(a -> {
            String online = a.get("online");
            if("0".equals(online)){
                onlines.put(a.get("assetId"), false);
            }
            if("1".equals(online)){
                onlines.put(a.get("assetId"), true);
            }
        });
        //1、获取所有有效点位
        List<MonitorPoint> pointList = pointService.lambdaQuery().eq(MonitorPoint::getStatus, "1")
                .isNotNull(MonitorPoint::getTenantId)
                .isNotNull(MonitorPoint::getProjectId)
                .eq(MonitorPoint::getMonitorType, "9")
                .eq(MonitorPoint::getStatus, "1").list();
        for (MonitorPoint point : pointList) {
//            if (onlines.get(point.getEquipmentId()) == null || !onlines.get(point.getEquipmentId())) {
//                continue;
//            }
            //获取此次聚合开始时间
            String startTime = null;
            //是否已存在聚合数据
            boolean flag = true;
            List<MonitorDataMonth> monthList = monthService.lambdaQuery()
                    .eq(MonitorDataMonth::getPointId, point.getId())
                    .eq(MonitorDataMonth::getProjectId, point.getProjectId())
                    .eq(MonitorDataMonth::getTenantId, point.getTenantId())
                    .ne(MonitorDataMonth::getIsNear, "1")
                    .orderByDesc(MonitorDataMonth::getCreateTime).list();
            if (monthList.size() > 0) {//存在聚合数据则增量聚合
                //获取聚合开始整点时间
                startTime = DateUtil.formatDateStr(monthList.get(0).getCreateTime(), format);
                flag = false;
            }
            if (flag) {//需要重新解析聚合
                List<MonitorData> monitorDataList = dataService.lambdaQuery()
                        .eq(MonitorData::getPointId, point.getId())
                        .isNotNull(MonitorData::getTenantId)
                        .isNotNull(MonitorData::getProjectId)
                        .orderByAsc(MonitorData::getMonitorTime).list();
                if (monitorDataList.size() > 0) {
                    //获取聚合开始整点时间
                    startTime = DateUtil.formatDateStr(monitorDataList.get(0).getMonitorTime(), format);
                }
            }
            if (StringUtils.isEmpty(startTime)) {
                continue;
            }
            //月数据
            List<MonitorDataMonth> dataMonthes = new ArrayList<>();
            String endTime = DateUtil.addDay(startTime, 1);
            //此处后延一次
            while (nowTime.compareTo(DateUtil.parseDate(endTime, DateUtil.YMDHMS_)) > -1) {
                //最大最小值
                double min = 0.00;
                double max = 0.00;
                BigDecimal tValue = new BigDecimal(0.00);//总数
                int time = 0;//次数
                int size;//数据条数
                List<MonitorData> dataList = dataService.lambdaQuery()
                        .eq(MonitorData::getPointId, point.getId())
                        .isNotNull(MonitorData::getTenantId)
                        .isNotNull(MonitorData::getProjectId)
                        .ge(MonitorData::getMonitorTime, startTime)
                        .lt(MonitorData::getMonitorTime, endTime)
                        .list();
                if (dataList.size() < 1) {
                    startTime = endTime;
                    endTime = DateUtil.addDay(startTime, 1);
                    continue;
                }
                for (MonitorData data : dataList) {
                    String value = data.getValue();
                    if (StringUtils.isEmpty(value)) {
                        value = "0";
                    }
                    tValue = tValue.add(new BigDecimal(value));
                    //最大最小值
                    double dValue = Double.valueOf(value);
                    if (time == 0) {
                        min = dValue;
                        max = dValue;
                        time++;
                    } else {
                        if (min > dValue) {
                            min = dValue;
                        }
                        if (max < dValue) {
                            max = dValue;
                        }
                    }
                }
                //
                size = dataList.size();

                if (point.getStatisticalType().equals("2")) {
                    tValue = tValue.divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP);
                }
                MonitorDataMonth dataMonth = new MonitorDataMonth();
                dataMonth.setPointId(point.getId());
                dataMonth.setStatisticalType(point.getStatisticalType());
                dataMonth.setValue(tValue.toString());
                dataMonth.setStartTime(DateUtil.parseDate(startTime, DateUtil.YMDHMS_));
                dataMonth.setEndTime(DateUtil.parseDate(endTime, DateUtil.YMDHMS_));
                dataMonth.setCreateTime(new Date());
                dataMonth.setProjectId(point.getProjectId());
                dataMonth.setTenantId(point.getTenantId());
                dataMonth.setMaxValue(String.valueOf(max));
                dataMonth.setMinValue(String.valueOf(min));
                dataMonthes.add(dataMonth);
                log.info("月点位=>" + point.getPointName() + "[" + point.getId() + "],完成【"
                        + endTime + "】聚合 => tValue:" + tValue + "|max=" + max + "|min=" + min);
                //处理时间
                startTime = endTime;
                endTime = DateUtil.addDay(startTime, 1);
            }
            if (dataMonthes.size() > 0) {
                monthService.saveBatch(dataMonthes, 100);
            }
        }
        long end = System.currentTimeMillis();
        log.info("MONTH end at " + DateUtil.formatDateStr(new Date(), DateUtil.YMDHMS_) + "，用时：" + (end - start) + "毫秒");
        //2021-11-17 删除七日前数据   测试压力，于2021-12-30屏蔽
        deleteBefore7Days(nowTime);
        //聚合 年
        //monitoringDataYear();
    }

    /**
     * 删除7日前数据
     *
     * @param nowTime
     */
    void deleteBefore7Days(Date nowTime) {
        try {
            String day = DateUtil.addDay(DateUtil.formatDateStr(nowTime, DateUtil.YMDHMS_), -7);
            int delNum = dataService.deleteByMonitorTime(day);
            log.info("删除7日前数据 =>" + day + ";删除条数 =>" + delNum);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 每月01日零点执行 为避免时间格式错误，可延时5秒执行 即整点零5秒
     * 2021-10-18 考虑data数据仅保留七日，年数据聚合逻辑变更：
     * 由月数据聚合为年年数据
     * 2021-10-20 月数据聚合后进行聚合
     */
    void monitoringDataYear() throws Exception {
        log.info("YEAR start at " + DateUtil.formatDateStr(new Date(), DateUtil.YMDHMS_) + " ……");
        long start = System.currentTimeMillis();
        String format = "yyyy-MM-01 00:00:00";
        Date nowTime = DateUtil.formatDate(new Date(), format);
        //1、获取所有有效点位
        List<MonitorPoint> pointList = pointService.lambdaQuery().eq(MonitorPoint::getStatus, "1")
                .isNotNull(MonitorPoint::getTenantId)
                .isNotNull(MonitorPoint::getProjectId)
                .eq(MonitorPoint::getStatus, "1").list();
        for (MonitorPoint point : pointList) {
            //获取此次聚合开始时间
            String startTime = null;
            //是否已存在聚合数据
            boolean flag = true;

            List<MonitorDataYear> yearList = yearService.lambdaQuery()
                    .eq(MonitorDataYear::getPointId, point.getId())
                    .eq(MonitorDataYear::getProjectId, point.getProjectId())
                    .eq(MonitorDataYear::getTenantId, point.getTenantId())
                    .ne(MonitorDataYear::getIsNear, "1")
                    .orderByDesc(MonitorDataYear::getCreateTime).list();
            if (yearList.size() > 0) {//存在聚合数据则增量聚合
                //获取聚合开始整点时间
                startTime = DateUtil.formatDateStr(yearList.get(0).getCreateTime(), format);
                flag = false;
            }
            if (flag) {//需要重新解析聚合
                List<MonitorDataMonth> monitorDataMonths = monthService.lambdaQuery()
                        .eq(MonitorDataMonth::getProjectId, point.getProjectId())
                        .eq(MonitorDataMonth::getTenantId, point.getTenantId())
                        .eq(MonitorDataMonth::getPointId, point.getId())
                        .orderByAsc(MonitorDataMonth::getCreateTime).list();
                if (monitorDataMonths.size() > 0) {
                    //获取聚合开始整点时间
                    startTime = DateUtil.formatDateStr(monitorDataMonths.get(0).getCreateTime(), format);
                }
            }
            if (StringUtils.isEmpty(startTime)) {
                continue;
            }
            //年数据
            List<MonitorDataYear> dataYears = new ArrayList<>();
            String endTime = DateUtil.addMonth(startTime, 1);
            //此处后延一次
            while (nowTime.compareTo(DateUtil.parseDate(endTime, DateUtil.YMDHMS_)) > -1) {
                //最大最小值
                double min = 0.00;
                double max = 0.00;
                BigDecimal tValue = new BigDecimal(0.00);//总数
                int time = 0;//次数
                int size;//数据条数
                List<MonitorDataMonth> dataList = monthService.lambdaQuery()
                        .eq(MonitorDataMonth::getPointId, point.getId())
                        .eq(MonitorDataMonth::getProjectId, point.getProjectId())
                        .eq(MonitorDataMonth::getTenantId, point.getTenantId())
                        .gt(MonitorDataMonth::getCreateTime, startTime)
                        .lt(MonitorDataMonth::getCreateTime, endTime)
                        .list();
                if (dataList.size() < 1) {
                    startTime = endTime;
                    endTime = DateUtil.addMonth(startTime, 1);
                    continue;
                }
                for (MonitorDataMonth data : dataList) {
                    String value = data.getValue();
                    tValue = tValue.add(new BigDecimal(value));
                    //最大最小值
                    double dValue = Double.valueOf(value);
                    if (time == 0) {
                        min = dValue;
                        max = dValue;
                        time++;
                    } else {
                        if (min > dValue) {
                            min = dValue;
                        }
                        if (max < dValue) {
                            max = dValue;
                        }
                    }
                }
                //
                size = dataList.size();

                if (point.getStatisticalType().equals("2")) {
                    tValue = tValue.divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP);
                }
                MonitorDataYear dataYear = new MonitorDataYear();
                dataYear.setPointId(point.getId());
                dataYear.setStatisticalType(point.getStatisticalType());
                dataYear.setValue(tValue.toString());

                dataYear.setStartTime(DateUtil.parseDate(startTime, DateUtil.YMDHMS_));
                dataYear.setEndTime(DateUtil.parseDate(endTime, DateUtil.YMDHMS_));
                dataYear.setCreateTime(new Date());

                dataYear.setProjectId(point.getProjectId());
                dataYear.setTenantId(point.getTenantId());
                dataYear.setMaxValue(String.valueOf(max));
                dataYear.setMinValue(String.valueOf(min));
                dataYears.add(dataYear);
                log.info("年点位=>" + point.getPointName() + "[" + point.getId() + "],完成【"
                        + endTime + "】聚合 => tValue:" + tValue + "|max=" + max + "|min=" + min);
                //处理时间
                startTime = endTime;
                endTime = DateUtil.addMonth(startTime, 1);
            }
            if (dataYears.size() > 0) {
                yearService.saveBatch(dataYears);
            }
        }
        long end = System.currentTimeMillis();
        log.info("YEAR end at " + DateUtil.formatDateStr(new Date(), DateUtil.YMDHMS_) + "，用时：" + (end - start) + "毫秒");
    }

}
