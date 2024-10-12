package com.kakarote.build.equipment.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.equipment.entity.PO.EquipmentTowerCrane;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 塔机配置表 Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2021-11-18
 */
public interface EquipmentTowerCraneMapper extends BaseMapper<EquipmentTowerCrane> {

    EquipmentTowerCrane getByDeviceId(@Param("deviceId") Integer deviceId);

    @SqlParser(filter = true)
    Integer getTowerCraneNumber(@Param("data")Map map);
    @SqlParser(filter = true)
    Integer getTowerCraneFrequency(@Param("data")Map map);
    @SqlParser(filter = true)
    Double getTowerCraneWeight(@Param("data")Map map);
    @SqlParser(filter = true)
    Integer getTowerCraneTime(@Param("data")Map map);
    @SqlParser(filter = true)
    /**
    * @Description:吊装次数分析
    * @author shz
    * @date 2021-11-23 16:12
    * @param [map]
    * @return java.util.List<java.util.Map>
    */
    List<Map> getQTDZXLFX(@Param("data")Map map);

    
    @SqlParser(filter = true)
    /**
    * @Description:吊装时间分析
    * @author shz
    * @date 2021-11-23 16:12
    * @param [map]
    * @return java.util.List<java.util.Map>
    */
    List<Map> getQTDZXLFXSJ(@Param("data")Map map);
    @SqlParser(filter = true)
    List<Map> getDZPHB(@Param("data")Map map);
    @SqlParser(filter = true)
    List<Map> getDZZL(@Param("data")Map map);
    @SqlParser(filter = true)
    List<Integer> getJSZTDCS(@Param("data")Map map);


    List<Map> selectAllTowerCranr();


}
