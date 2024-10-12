package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.EquipmentFluoriteCloudBO;
import com.kakarote.build.equipment.entity.PO.EquipmentFluoriteCloud;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorPoint;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName EquipmentFluoriteCloudMapper
 * @Author wnj58
 * @Date 2022/6/17 15:32
 * @purpose
 * @Version 1.0
 **/
/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wnj58
 * @since 2022-06-17
 */
public interface EquipmentFluoriteCloudMapper extends BaseMapper<EquipmentFluoriteCloud> {
    BasePage<Map> getAssetLilst(BasePage<EquipmentFluoriteCloudBO> parse, @Param("data") EquipmentFluoriteCloudBO equipmentFluoriteCloudBO);

    Map getDetails(String id);

    boolean updateState(@Param("data") EquipmentFluoriteCloud equipmentFluoriteCloud);

    BasePage<Map> getAIEquipmentList(BasePage<Object> parse, @Param("data") EquipmentFluoriteCloudBO equipmentFluoriteCloudBO);

    boolean addAIEquipment(@Param("data") EquipmentFluoriteCloudBO equipmentFluoriteCloudBO);

    boolean delAIEquipment(@Param("id") String id);

    Map getEquipmentType();
}
