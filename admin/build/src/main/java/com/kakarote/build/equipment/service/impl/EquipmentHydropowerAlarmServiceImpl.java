package com.kakarote.build.equipment.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.equipment.entity.BO.EquipmentHydropowerAlarmBO;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.equipment.entity.PO.EquipmentHydropowerAlarm;
import com.kakarote.build.equipment.entity.PO.EquipmentLocation;
import com.kakarote.build.equipment.entity.PO.EquipmentWaterRecord;
import com.kakarote.build.equipment.mapper.EquipmentHydropowerAlarmMapper;
import com.kakarote.build.equipment.service.IEquipmentHydropowerAlarmService;
import com.kakarote.build.equipment.service.IEquipmentLocationService;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 水电报警 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-06-21
 */
@Service
public class EquipmentHydropowerAlarmServiceImpl extends BaseServiceImpl<EquipmentHydropowerAlarmMapper, EquipmentHydropowerAlarm> implements IEquipmentHydropowerAlarmService {

    @Autowired
    private IEquipmentLocationService locationService;

    @Override
    public BasePage<EquipmentHydropowerAlarm> selectByPage(EquipmentHydropowerAlarmBO hydropowerAlarmBO) {
        BasePage<EquipmentHydropowerAlarm> list = getBaseMapper().selectByPage(hydropowerAlarmBO.parse(), hydropowerAlarmBO);
        return list;
    }

    @Override
    public JSONObject excelImport(MultipartFile file) {
        List<List<Object>> errList = new ArrayList<>();
        String filePath = getFilePath(file);
        ExcelReader reader = ExcelUtil.getReader(filePath);
        List<Map<String, Object>> list = reader.read(2, 3, Integer.MAX_VALUE);

        String message = "";
        Integer errSize = 0;
        List<EquipmentHydropowerAlarm> hydropowerAlarmList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            boolean isError = false;
            EquipmentHydropowerAlarm hydropowerAlarm = new EquipmentHydropowerAlarm();
            EquipmentLocation location = null;

            Map<String, Object> map = list.get(i);

            // 监测区域
            String locationName = String.valueOf(map.get("locationName"));
            if (locationName != null && !locationName.equals("")) {
                // 根据设备名称查询对应设备
                LambdaQueryWrapper<EquipmentLocation> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(EquipmentLocation::getLocationName, locationName);
                queryWrapper.last("limit 1");
                location = locationService.getOne(queryWrapper);

                // 如果位置信息不存在，抛出异常
                if (location == null) {
                    message += "第" + (i + 4) + "行监测区域：" + locationName + "不存在,请检查数据；";
                    isError = true;
                }
            } else {
                message += "第" + (i + 4) + "行设备名称不能为空,请检查数据；";
                isError = true;
            }

            // 能源名称
            String energyName = String.valueOf(map.get("energyName")) ;
            if (energyName == null || energyName.equals("")) {
                message += "第" + (i + 4) + "行能源名称不能为空,请检查数据；";
                isError = true;
            }

            if (energyName.equals("电量")) {
                energyName = "1";
            } else if (energyName.equals("水量")) {
                energyName = "2";
            } else {
                message += "第" + (i + 4) + "行能源名称错误,请检查数据；";
                isError = true;
            }

            // 报警时间
            String alarmTimeStr = String.valueOf(map.get("alarmTime")) ;
            if (alarmTimeStr == null || alarmTimeStr.equals("")) {
                message += "第" + (i + 4) + "行报警时间不能为空,请检查数据；";
                isError = true;
            }

            Date alarmTime = null;
            try {
                alarmTime = DateUtil.parseDateTime(alarmTimeStr);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("行报警时间格式不规范：" + e);
                message += "第" + (i + 4) + "行报警时间格式不规范,请检查数据；";
                isError = true;
            }

            // 监测开始时间
            String startTimeStr = String.valueOf(map.get("startTime"));
            if (startTimeStr == null || startTimeStr.equals("")) {
                message += "第" + (i + 4) + "行报监测开始时间不能为空,请检查数据；";
                isError = true;
            }

            Date startTime = null;
            try {
                startTime = DateUtil.parseDateTime(startTimeStr);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("行监测开始时间格式不规范：" + e);
                message += "第" + (i + 4) + "行监测开始时间格式不规范,请检查数据；";
                isError = true;
            }

            // 监测结束时间
            String endTimeStr = String.valueOf(map.get("endTime"));
            if (endTimeStr == null || endTimeStr.equals("")) {
                message += "第" + (i + 4) + "行监测结束时间不能为空,请检查数据；";
                isError = true;
            }

            Date endTime = null;
            try {
                endTime = DateUtil.parseDateTime(endTimeStr);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("行监测结束时间格式不规范：" + e);
                message += "第" + (i + 4) + "行监测结束时间格式不规范,请检查数据；";
                isError = true;
            }

            // 累计上期使用量
            String accumulativePrevious = String.valueOf(map.get("accumulativePrevious"));
            if (accumulativePrevious == null || accumulativePrevious.equals("")) {
                message += "第" + (i + 4) + "行累计上期使用量不能为空,请检查数据；";
                isError = true;
            }

            // 本期使用量
            String currentUsage = String.valueOf(map.get("currentUsage"));
            if (currentUsage == null || currentUsage.equals("")) {
                message += "第" + (i + 4) + "行本期使用量不能为空,请检查数据；";
                isError = true;
            }

            // 累计使用量
            String cumulativeUsage = String.valueOf(map.get("cumulativeUsage"));
            if (cumulativeUsage == null || cumulativeUsage.equals("")) {
                message += "第" + (i + 4) + "行累计使用量不能为空,请检查数据；";
                isError = true;
            }

            // 本期使用指标
            String currentIndex = String.valueOf(map.get("currentIndex"));
            if (currentIndex == null || currentIndex.equals("")) {
                message += "第" + (i + 4) + "行本期使用指标不能为空,请检查数据；";
                isError = true;
            }

            // 累计使用指标
            String cumulativeIndex = String.valueOf(map.get("cumulativeIndex"));
            if (cumulativeIndex == null || cumulativeIndex.equals("")) {
                message += "第" + (i + 4) + "行累计使用指标不能为空,请检查数据；";
                isError = true;
            }

            // 超限类型
            String transfiniteType = String.valueOf(map.get("transfiniteType")) ;
            if (transfiniteType == null || transfiniteType.equals("")) {
                message += "第" + (i + 4) + "行超限类型不能为空,请检查数据；";
                isError = true;
            }

            if (transfiniteType.equals("当期")) {
                transfiniteType = "1";
            } else if (transfiniteType.equals("累计")) {
                transfiniteType = "2";
            } else {
                message += "第" + (i + 4) + "行超限类型错误,请检查数据；";
                isError = true;
            }


            // 超限数量
            String transfiniteNumber = String.valueOf(map.get("transfiniteNumber"));
            if (transfiniteNumber == null || transfiniteNumber.equals("")) {
                message += "第" + (i + 4) + "行超限数量不能为空,请检查数据；";
                isError = true;
            }

            // 如果当前行的数据不存在错误 设置保存数据
            if (!isError) {


                Date date = new Date();// 获取当前时间
                Long userId = UserUtil.getUser().getUserId();

                hydropowerAlarm.setLocationId(location.getLocationId());
                hydropowerAlarm.setEnergyName(energyName);
                hydropowerAlarm.setAlarmTime(alarmTime);
                hydropowerAlarm.setStartTime(startTime);
                hydropowerAlarm.setEndTime(endTime);
                hydropowerAlarm.setAccumulativePrevious(accumulativePrevious);
                hydropowerAlarm.setCurrentUsage(currentUsage);
                hydropowerAlarm.setCumulativeUsage(cumulativeUsage);
                hydropowerAlarm.setCurrentIndex(currentIndex);
                hydropowerAlarm.setCumulativeIndex(cumulativeIndex);
                hydropowerAlarm.setTransfiniteType(transfiniteType);
                hydropowerAlarm.setTransfiniteNumber(transfiniteNumber);

                // 将当前行需要保存的bean对象添加到列表中
                hydropowerAlarmList.add(hydropowerAlarm);
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
            result  = new JSONObject().fluentPut("message", message).fluentPut("errSize", errSize);

        } else {

            // 批量插入
            int num = getBaseMapper().batchSaveData(hydropowerAlarmList);
            result = new JSONObject().fluentPut("totalSize", hydropowerAlarmList.size()).fluentPut("errSize", 0);
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

    @Override
    public BasePage<EquipmentHydropowerAlarm> selectByPageAndPro(EquipmentHydropowerAlarmBO hydropowerAlarmBO) {
        BasePage<EquipmentHydropowerAlarm> list = getBaseMapper().selectByPageAndPro(hydropowerAlarmBO.parse(), hydropowerAlarmBO);
        return list;
    }

    private String getFilePath(MultipartFile file) {
        String dirPath = FileUtil.getTmpDirPath();
        try {
            InputStream inputStream = file.getInputStream();
            File fromStream = FileUtil.writeFromStream(inputStream, dirPath + "/" + IdUtil.simpleUUID() + file.getOriginalFilename());
            return fromStream.getAbsolutePath();
        } catch (IOException e) {
            throw new CrmException(SystemCodeEnum.SYSTEM_UPLOAD_FILE_ERROR);
        }
    }
}
