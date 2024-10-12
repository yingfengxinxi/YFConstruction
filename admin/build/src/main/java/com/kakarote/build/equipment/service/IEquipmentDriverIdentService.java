package com.kakarote.build.equipment.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.complex.entity.BO.ComplexMaterialsInBO;
import com.kakarote.build.equipment.entity.BO.EquipmentDriverIdentBO;
import com.kakarote.build.equipment.entity.PO.EquipmentDriverIdent;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 机械设备司机识别 服务类
 * </p>
 *
 * @author zy
 * @since 2021-07-26
 */
public interface IEquipmentDriverIdentService extends BaseService<EquipmentDriverIdent> {

    public BasePage<Map<String,Object>> selectList(EquipmentDriverIdentBO equipmentDriverIdentBO);

    Map<String,Object> getByIdInApp(Integer id);

    JSONObject excelImport(MultipartFile file) throws Exception;


    BasePage<Map<String, Object>> selectListNew(EquipmentDriverIdentBO equipmentDriverIdentBO);

    Result saveData(List<Map<String, Object>> list);
}
