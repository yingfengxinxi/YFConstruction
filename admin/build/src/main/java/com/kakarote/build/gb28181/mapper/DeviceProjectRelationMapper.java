package com.kakarote.build.gb28181.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.equipment.entity.BO.EquipmentAssetsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.gb28181.entity.PO.DeviceProjectRelation;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName DeviceProjectRelationMapper
 * @Author wnj58
 * @Date 2022/1/20 18:13:36
 * @purpose
 * @Version 1.0
 **/

public interface DeviceProjectRelationMapper extends BaseMapper<DeviceProjectRelation> {

    @SqlParser(filter=true)
    List<DeviceProjectRelation> videoMonitorDevices(@Param("data") Map<String,Object> map);
    @SqlParser(filter=true)
    List<DeviceProjectRelation> getVideoDeviceAll();
    @SqlParser(filter=true)
    List<DeviceProjectRelation> getVideoByProject(Integer id);
    @SqlParser(filter=true)
    List<Integer> getProject();
    @SqlParser(filter=true)
    int updateBatchById(@Param("list") List<DeviceProjectRelation> channelLists);
    @SqlParser(filter=true)
    List<DeviceProjectRelation> getVideoDeviceByDeviceSerial(String deviceSerial);

    int updateBybusinessId(@Param("data") DeviceProjectRelation deviceProjectRelation);
}
