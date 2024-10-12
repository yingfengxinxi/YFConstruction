package com.qdoner.datarecept.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.core.servlet.BaseMapper;
import com.qdoner.datarecept.entity.PO.DeviceProjectRelation;
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

    @SqlParser(filter = true)
    List<DeviceProjectRelation> videoMonitorDevices(@Param("data") Map<String, Object> map);

    @SqlParser(filter = true)
    List<DeviceProjectRelation> getVideoDeviceAll(@Param("projectId") Integer projectId);

    @SqlParser(filter = true)
    List<DeviceProjectRelation> getVideoByProject(Integer id);

    @SqlParser(filter = true)
    List<Integer> getProject();

    @SqlParser(filter = true)
    int updateBatchById(@Param("list") List<DeviceProjectRelation> channelLists);

    @SqlParser(filter = true)
    List<DeviceProjectRelation> getVideoDeviceByDeviceSerial(String deviceSerial);

    int updateBybusinessId(@Param("data") DeviceProjectRelation deviceProjectRelation);
}
