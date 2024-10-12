package com.kakarote.build.equipment.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.equipment.entity.BO.EquipmentFluoriteCloudBO;
import com.kakarote.build.equipment.entity.PO.EquipmentFluoriteCloud;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 萤石云设备信息 服务类
 * </p>
 *
 * @author wnj58
 * @since 2022-06-17
 */
public interface EquipmentFluoriteCloudService extends BaseService<EquipmentFluoriteCloud> {

    BasePage<Map> getList(EquipmentFluoriteCloudBO equipmentFluoriteCloudBO);

    JSONObject excelImport(MultipartFile file);

    Map getDetails(String id);

    boolean updateState(EquipmentFluoriteCloud equipmentFluoriteCloud);

    BasePage<Map> getAIEquipmentList(EquipmentFluoriteCloudBO equipmentFluoriteCloudBO);

    boolean addAIEquipment(EquipmentFluoriteCloudBO equipmentFluoriteCloudBO);

    boolean delAIEquipment(String id);
}
