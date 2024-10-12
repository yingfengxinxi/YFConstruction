package com.kakarote.build.safety.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.safety.entity.BO.SafetyMixPlantBO;
import com.kakarote.build.safety.entity.PO.SafetyMixPlant;
import com.kakarote.build.safety.mapper.SafetyMixPlantMapper;
import com.kakarote.build.safety.service.ISafetyMixPlantService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminDictData;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 拌合站监控 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-06-18
 */
@Service
public class SafetyMixPlantServiceImpl extends BaseServiceImpl<SafetyMixPlantMapper, SafetyMixPlant> implements ISafetyMixPlantService {
    @Autowired
    private AdminService AdminService;

    @Override
    public BasePage<SafetyMixPlant> selectByPage(SafetyMixPlantBO SafetyMixPlantBO) {
        SafetyMixPlantBO.setOrdersBd("alarm_time",false);
        return getBaseMapper().selectByPage(SafetyMixPlantBO.parse(), SafetyMixPlantBO);
    }

    @Override
    public JSONObject excelImport(MultipartFile file)  {
        List<SafetyMixPlant> listAdd = new ArrayList<>();

        ExcelReader reader = null;
        try {
            reader = ExcelUtil.getReader(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> list = reader.read(1, 2, Integer.MAX_VALUE);
            for (int i = 1; i <= list.size(); i++) {
                Map<String, Object> map = list.get(i - 1);
                String mixingPlantName = String.valueOf(map.get("拌合站名称(*)"));
                if (mixingPlantName == null || mixingPlantName.equals("")) {
                    throw new CrmException(1001, "拌合站名称不能为空");
                }

                Object concreteGrade1 = map.get("砼强度等级(*)");
                String concreteGrade = "";
                if (concreteGrade1 == null || concreteGrade1.equals("")) {
                    throw new CrmException(1001, "砼强度等级不能为空");
                } else {
                    concreteGrade = String.valueOf(map.get("砼强度等级(*)"));
                    String Grade_dictValue = dictInit(concreteGrade, "b_safety_streng_Grade");
                    if (Grade_dictValue.equals(concreteGrade)) {
                        throw new CrmException(1001, "砼强度等级填写错误");
                    } else {
                        concreteGrade = Grade_dictValue;
                    }
                }
                String unitWorks = String.valueOf(map.get("单位工程(*)"));
                if (unitWorks == null || unitWorks.equals("")) {
                    throw new CrmException(1001, "单位工程不能为空");
                }

                String proLocation = String.valueOf(map.get("工程部位(*)"));
                if (proLocation == null || proLocation.equals("")) {
                    throw new CrmException(1001, "工程部位不能为空");
                }
                String laboratoryMix = String.valueOf(map.get("试验室配合比(*)"));
                if (laboratoryMix == null || laboratoryMix.equals("")) {
                    throw new CrmException(1001, "试验室配合比不能为空");
                }
                String constructionMix = String.valueOf(map.get("施工配合比(*)"));
                if (constructionMix == null || constructionMix.equals("")) {
                    throw new CrmException(1001, "施工配合比不能为空");
                }
                String materialName = String.valueOf(map.get("材料名称(*)"));
                if (materialName == null || materialName.equals("")) {
                    throw new CrmException(1001, "材料名称不能为空");
                }
                String materialWeight = String.valueOf(map.get("材料标准重量(*)"));
                if (materialWeight == null || materialWeight.equals("")) {
                    throw new CrmException(1001, "材料标准重量不能为空");
                }
                String actualWeight = String.valueOf(map.get("实际重量(*)"));
                if (actualWeight == null || actualWeight.equals("")) {
                    throw new CrmException(1001, "实际重量不能为空");
                }
                String allowableError = String.valueOf(map.get("允许误差(*)"));
                if (allowableError == null || allowableError.equals("")) {
                    throw new CrmException(1001, "允许误差不能为空");
                }
                String actualError = String.valueOf(map.get("实际误差(*)"));
                if (actualError == null || actualError.equals("")) {
                    throw new CrmException(1001, "实际误差不能为空");
                }

                Object alarmLevel1 = map.get("报警级别(*)（预警：1；报警：2；）");
                String alarmLevel = "";
                String newAlarmLevel = "";
                if (alarmLevel1 == null || alarmLevel1.equals("")) {
                    throw new CrmException(1001, "报警级别不能为空");
                } else {
                    alarmLevel = String.valueOf(map.get("报警级别(*)（预警：1；报警：2；）"));
                    if (!("1".equals(alarmLevel)) && !("2".equals(alarmLevel))) {
                        throw new CrmException(1001, "报警级别填写错误");
                    }
                }

                String alarmTime = String.valueOf(map.get("报警时间(*)"));
                if (alarmTime == null || alarmTime.equals("")) {
                    throw new CrmException(1001, "报警时间不能为空");
                } else {
                    if (!isLegalDate(alarmTime)) {
                        throw new CrmException(1001, "报警时间格式错误");
                    }
                }

                SafetyMixPlant SafetyMixPlant = new SafetyMixPlant();
                SafetyMixPlant.setMixingPlantName(mixingPlantName);
                SafetyMixPlant.setConcreteGrade(concreteGrade);
                SafetyMixPlant.setUnitWorks(unitWorks);
                SafetyMixPlant.setProLocation(proLocation);
                SafetyMixPlant.setLaboratoryMix(laboratoryMix);
                SafetyMixPlant.setConstructionMix(constructionMix);
                SafetyMixPlant.setMaterialName(materialName);
                SafetyMixPlant.setMaterialWeight(Double.parseDouble(materialWeight));
                SafetyMixPlant.setActualWeight(Double.parseDouble(actualWeight));
                SafetyMixPlant.setAllowableError(allowableError);
                SafetyMixPlant.setActualError(actualError);
                SafetyMixPlant.setAlarmLevel(alarmLevel);
                SafetyMixPlant.setAlarmTime(StrToDate(alarmTime));

                SafetyMixPlant.setCreateBy(String.valueOf(UserUtil.getUserId()));
                SafetyMixPlant.setCreateTime(new Date());
                String batchId = UUID.randomUUID().toString();
                SafetyMixPlant.setBatchId(batchId);
                listAdd.add(SafetyMixPlant);
            }
            //批量保存数据
            saveBatch(listAdd,listAdd.size());

        JSONObject result = new JSONObject().fluentPut("totalSize", listAdd.size()).fluentPut("errSize", 0);
        return result;
    }

    //判断 时间格式
    private static boolean isLegalDate(String sDate) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //字典反向格式化
    private String dictInit(String dictName, String dictType) {
        String dictValue = "";
        if (dictName == null) {
            return dictName;
        }
        List<AdminDictData> alertType_dict = AdminService.dictType(dictType).getData();
        for (AdminDictData ADD : alertType_dict) {
            if (ADD.getDictLabel().equals(dictName)) {
                dictValue = ADD.getDictValue();
            }
        }
        if (dictValue.equals(dictName)||"".equals(dictValue)) {
            return dictName;
        }
        return dictValue;
    }

    /**
     * 时间格式化
     *
     * @param str
     * @return
     */
    private Date StrToDate(String str) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }
}
