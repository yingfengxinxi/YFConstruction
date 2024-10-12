package com.kakarote.build.equipment.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.equipment.entity.BO.EquipmentWaterRecordBO;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.equipment.entity.PO.EquipmentElectricityRecord;
import com.kakarote.build.equipment.entity.PO.EquipmentWaterRecord;
import com.kakarote.build.equipment.mapper.EquipmentWaterRecordMapper;
import com.kakarote.build.equipment.service.IEquipmentAssetsService;
import com.kakarote.build.equipment.service.IEquipmentWaterRecordService;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-06-21
 */
@Service
public class EquipmentWaterRecordServiceImpl extends BaseServiceImpl<EquipmentWaterRecordMapper, EquipmentWaterRecord> implements IEquipmentWaterRecordService {

    @Autowired
    private IEquipmentAssetsService assetsService;

    @Override
    public Map<String, Object> selectTableList(EquipmentWaterRecordBO waterRecordBO) {
        if (waterRecordBO.getDateType() == null) {
            throw new CrmException(500, "报表类型不能为空");
        }

        // 日期时间列表
        List<String> cols = new ArrayList<>();

        // 年度报表按年份查询，月份报表按时间范围查询
        if (waterRecordBO.getDateType().equals("1")) {
            // 参数未传查询时间，则默认查询当年数据
            if (waterRecordBO.getDate() == null) {
                Date currentTime = new Date();
                waterRecordBO.setDate(currentTime);
            }
            cols = getCols(waterRecordBO.getDate(), waterRecordBO.getDateType());
            // 设置时间格式化字符串
            waterRecordBO.setDateFormat("%Y-%m");

        } else if (waterRecordBO.getDateType().equals("2")) {
            // 参数未传查询时间，则默认查询当月数据
            if (waterRecordBO.getDate() == null) {
                Date currentTime = new Date();
                waterRecordBO.setDate(currentTime);
            }
            cols = getCols(waterRecordBO.getDate(), waterRecordBO.getDateType());
            // 设置时间格式化字符串
            waterRecordBO.setDateFormat("%Y-%m-%d");

        } else if (waterRecordBO.getDateType().equals("3")) {

            if (waterRecordBO.getStartDate() == null) {
                throw new CrmException(500, "开始时间不能为空");
            }
            if (waterRecordBO.getEndDate() == null) {
                throw new CrmException(500, "结束时间不能为空");
            }

            String pattern = "yyyy-MM-dd";
            try {
                cols = com.kakarote.build.utils.DateUtil.getDateStrList(DateUtil.format(waterRecordBO.getStartDate(), pattern), DateUtil.format(waterRecordBO.getEndDate(), pattern), pattern);
            } catch (ParseException e) {
                e.printStackTrace();
                log.error("时间列表生成失败：" + e);
                throw new CrmException(500, "时间列表生成失败");
            }
            // 设置时间格式化字符串
            waterRecordBO.setDateFormat("%Y-%m-%d");
        }
        waterRecordBO.setCols(cols);

        List<LinkedHashMap<String, Object>> list = getBaseMapper().selectTableList(waterRecordBO);

        // 返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("cols", cols);
        result.put("tableList", list);

        return result;
    }

    /**
     * 返回当前月份的天数
     * @param date
     * @return
     */
    private int getDaysOfMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据时间和类型，生成查询字段
     * @param date
     * @return
     */
    private List<String> getCols(Date date, String dateType) {
        Integer cycleIndex = 0;
        String str = "";

        // dateType 时间类型 1：年；2：月
        if (dateType != null && dateType.equals("1")) {
            cycleIndex = 12;
            str = DateUtil.format(date, "yyyy");
        } else if (dateType != null && dateType.equals("2")) {
            cycleIndex = getDaysOfMonth(date);
            str = DateUtil.format(date, "yyyy-MM");
        }

        List<String> cols = new ArrayList<>();
        for (int i = 1; i<= cycleIndex; i++) {
            String col = str + "-";
            if (i<10) {
                col += "0";
            }
            col += i;
            cols.add(col);
        }
        return cols;
    }

    @Override
    public JSONObject excelImport(MultipartFile file) {
        List<List<Object>> errList = new ArrayList<>();
        String filePath = getFilePath(file);
        ExcelReader reader = ExcelUtil.getReader(filePath);
        List<Map<String, Object>> list = reader.read(2, 3, Integer.MAX_VALUE);

        String message = "";
        Integer errSize = 0;
        List<EquipmentWaterRecord> recordList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            boolean isError = false;
            EquipmentWaterRecord record = new EquipmentWaterRecord();
            EquipmentAssets assets = null;

            Map<String, Object> map = list.get(i);

            // 设备名称
            String equipmentName = (String) map.get("equipmentName");
            if (equipmentName != null && !equipmentName.equals("")) {
                // 根据设备名称查询对应设备
                LambdaQueryWrapper<EquipmentAssets> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(EquipmentAssets::getAssetName, equipmentName);
                queryWrapper.last("limit 1");
                assets = assetsService.getOne(queryWrapper);

                // 如果检测点不存在，抛出异常
                if (assets == null) {
                    message += "第" + (i + 4) + "行设备名称：" + equipmentName + "不存在,请检查数据；";
                    isError = true;
                }
            } else {
                message += "第" + (i + 4) + "行设备名称不能为空,请检查数据；";
                isError = true;
            }

            // 记录时间
            String time = String.valueOf(map.get("recordDate"));
            if (time == null || time.equals("")) {
                message += "第" + (i + 4) + "行记录时间不能为空,请检查数据；";
                isError = true;
            }

            Date recordDate = null;
            try {
                recordDate = DateUtil.parseDateTime(time);
            } catch (Exception e) {
                message += "第" + (i + 4) + "行记录时间格式不规范,请检查数据；";
                isError = true;
            }


            // 用电量
            String waterConsumption = String.valueOf(map.get("waterConsumption"));
            if (waterConsumption == null || waterConsumption.equals("")) {
                message += "第" + (i + 4) + "行用电水不能为空,请检查数据；";
                isError = true;
            }

            // 如果当前行的数据不存在错误 设置保存数据
            if (!isError) {


                Date date = new Date();// 获取当前时间
                Long userId = UserUtil.getUser().getUserId();

                record.setEquipmentId(assets.getAssetId());
                record.setDeptId(assets.getDeptId());
                record.setRecordDate(recordDate);
                record.setWaterConsumption(waterConsumption);

                // 将当前行需要保存的bean对象添加到列表中
                recordList.add(record);
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
            int num = getBaseMapper().batchSaveData(recordList);
            result = new JSONObject().fluentPut("totalSize", recordList.size()).fluentPut("errSize", 0);
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
    public Map<String, Object> selectProTableList(EquipmentWaterRecordBO waterRecordBO) {
        if (waterRecordBO.getDateType() == null) {
            throw new CrmException(500, "报表类型不能为空");
        }

        // 日期时间列表
        List<String> cols = new ArrayList<>();

        // 年度报表按年份查询，月份报表按时间范围查询
        if (waterRecordBO.getDateType().equals("1")) {
            // 参数未传查询时间，则默认查询当年数据
            if (waterRecordBO.getDate() == null) {
                Date currentTime = new Date();
                waterRecordBO.setDate(currentTime);
            }
            cols = getCols(waterRecordBO.getDate(), waterRecordBO.getDateType());
            // 设置时间格式化字符串
            waterRecordBO.setDateFormat("%Y-%m");

        } else if (waterRecordBO.getDateType().equals("2")) {
            // 参数未传查询时间，则默认查询当月数据
            if (waterRecordBO.getDate() == null) {
                Date currentTime = new Date();
                waterRecordBO.setDate(currentTime);
            }
            cols = getCols(waterRecordBO.getDate(), waterRecordBO.getDateType());
            // 设置时间格式化字符串
            waterRecordBO.setDateFormat("%Y-%m-%d");

        } else if (waterRecordBO.getDateType().equals("3")) {

            if (waterRecordBO.getStartDate() == null) {
                throw new CrmException(500, "开始时间不能为空");
            }
            if (waterRecordBO.getEndDate() == null) {
                throw new CrmException(500, "结束时间不能为空");
            }

            String pattern = "yyyy-MM-dd";
            try {
                cols = com.kakarote.build.utils.DateUtil.getDateStrList(DateUtil.format(waterRecordBO.getStartDate(), pattern), DateUtil.format(waterRecordBO.getEndDate(), pattern), pattern);
            } catch (ParseException e) {
                e.printStackTrace();
                log.error("时间列表生成失败：" + e);
                throw new CrmException(500, "时间列表生成失败");
            }
            // 设置时间格式化字符串
            waterRecordBO.setDateFormat("%Y-%m-%d");
        }
        waterRecordBO.setCols(cols);

        List<LinkedHashMap<String, Object>> list = getBaseMapper().selectProTableList(waterRecordBO);

        // 返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("cols", cols);
        result.put("tableList", list);

        return result;
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
