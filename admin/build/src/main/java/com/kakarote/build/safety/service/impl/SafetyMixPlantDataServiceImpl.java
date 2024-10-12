package com.kakarote.build.safety.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.safety.entity.BO.SafetyMixPlantDataBO;
import com.kakarote.build.safety.entity.PO.SafetyMixPlantData;
import com.kakarote.build.safety.mapper.SafetyMixPlantDataMapper;
import com.kakarote.build.safety.service.ISafetyMixPlantDataService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminDictData;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拌合站监控基本信息 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2022-08-30
 */
@Service
public class SafetyMixPlantDataServiceImpl extends BaseServiceImpl<SafetyMixPlantDataMapper, SafetyMixPlantData> implements ISafetyMixPlantDataService {

    @Autowired
    private AdminService adminService;

    @Override
    public BasePage<SafetyMixPlantData> selectByPage(SafetyMixPlantDataBO safetyMixPlantDataBO) {
        return getBaseMapper().selectByPage(safetyMixPlantDataBO.parse(), safetyMixPlantDataBO);
    }

    @Override
    public JSONObject excelImport(MultipartFile file) {
        List<SafetyMixPlantData> listAdd = new ArrayList<>();

        ExcelReader reader = null;
        try {
            reader = ExcelUtil.getReader(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> list = reader.read(1, 2, Integer.MAX_VALUE);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);


            String manufactor = String.valueOf(map.get("商砼厂家(*)"));
            if (manufactor == null || manufactor.equals("")) {
                throw new CrmException(1001, "商砼厂家不能为空");
            }

            Object grade1 = map.get("砼强度等级(*)");
            String grade = "";
            if (grade1 == null || grade1.equals("")) {
                throw new CrmException(1001, "砼强度等级不能为空");
            } else {
                grade = String.valueOf(map.get("砼强度等级(*)"));
                String Grade_dictValue = dictInit(grade, "b_safety_streng_Grade");
                if (Grade_dictValue.equals(grade)) {
                    throw new CrmException(1001, "砼强度等级填写错误");
                } else {
                    grade = Grade_dictValue;
                }
            }

            String position = String.valueOf(map.get("工程部位(*)"));
            if (position == null || position.equals("")) {
                throw new CrmException(1001, "工程部位不能为空");
            }

            String labMaterialNum = String.valueOf(map.get("试验室材料登记号(*)"));
            if (position == null || position.equals("")) {
                throw new CrmException(1001, "试验室材料登记号不能为空");
            }

            String labMaterialName = String.valueOf(map.get("试验室材料种类(*)"));
            if (labMaterialName == null || labMaterialName.equals("")) {
                throw new CrmException(1001, "试验室材料种类不能为空");
            }

            String labMaterialVolume = String.valueOf(map.get("试验室材料占比（%）(*)"));
            if (labMaterialVolume == null || labMaterialVolume.equals("")) {
                throw new CrmException(1001, "试验室材料占比不能为空");
            }

            String materialNum = String.valueOf(map.get("工地材料登记号(*)"));
            if (materialNum == null || materialNum.equals("")) {
                throw new CrmException(1001, "工地材料登记号不能为空");
            }

            String materialName = String.valueOf(map.get("工地材料种类(*)"));
            if (materialName == null || materialName.equals("")) {
                throw new CrmException(1001, "工地材料种类不能为空");
            }

            String materialVolume = String.valueOf(map.get("工地用量（kg/m³）(*)"));
            if (materialVolume == null || materialVolume.equals("")) {
                throw new CrmException(1001, "工地用量不能为空");
            }

            String allowError = String.valueOf(map.get("工地允许误差(*)"));
            if (allowError == null || allowError.equals("")) {
                throw new CrmException(1001, "工地允许误差不能为空");
            }

            String actualError = String.valueOf(map.get("工地实际误差(*)"));
            if (actualError == null || actualError.equals("")) {
                throw new CrmException(1001, "工地实际误差不能为空");
            }

            SafetyMixPlantData mixPlantData = new SafetyMixPlantData();
            mixPlantData.setManufactor(manufactor);
            mixPlantData.setGrade(grade);
            mixPlantData.setPosition(position);
            mixPlantData.setLabMaterialNum(labMaterialNum);
            mixPlantData.setLabMaterialName(labMaterialName);
            mixPlantData.setLabMaterialVolume(Double.valueOf(labMaterialVolume));
            mixPlantData.setMaterialNum(materialNum);
            mixPlantData.setMaterialName(materialName);
            mixPlantData.setMaterialVolume(Double.valueOf(materialVolume));
            mixPlantData.setAllowError(Double.valueOf(allowError));
            mixPlantData.setActualError(Double.valueOf(actualError));

            listAdd.add(mixPlantData);
        }
        //批量保存数据
        saveBatch(listAdd,listAdd.size());

        JSONObject result = new JSONObject().fluentPut("totalSize", listAdd.size()).fluentPut("errSize", 0);
        return result;
    }

    //字典反向格式化
    private String dictInit(String dictName, String dictType) {
        String dictValue = "";
        if (dictName == null) {
            return dictName;
        }
        List<AdminDictData> alertType_dict = adminService.dictType(dictType).getData();
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
}
