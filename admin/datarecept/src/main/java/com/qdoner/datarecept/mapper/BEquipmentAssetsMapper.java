package com.qdoner.datarecept.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.qdoner.datarecept.entity.PO.BEquipmentAssets;
import com.kakarote.core.servlet.BaseMapper;
import com.qdoner.datarecept.entity.PO.DeviceProjectRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备资产 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
public interface BEquipmentAssetsMapper extends BaseMapper<BEquipmentAssets> {

    int updateByAssetCode(BEquipmentAssets assets);

    @SqlParser(filter = true)
    BEquipmentAssets getByCode(String code);

    BEquipmentAssets selectById(@Param("assetId") String assetId);

    Map selectElevatorById(String id);

    Map selectTowerCraneById(String id);

    List<Map<String,Object>> getIsAllowControl(@Param("data")Map<String,Object> map);

    @SqlParser(filter=true)
    List<Map<String, Object>> selectMonitorDataList(@Param("timeValue") String timeValue);

    @SqlParser(filter=true)
    List<Map<String, Object>> selectTowerCraneDataList(@Param("timeValue") String timeValue);

    @SqlParser(filter=true)
    List<Map<String, Object>> selectElevatorDataList(@Param("timeValue") String timeValue);

    @SqlParser(filter=true)
    int updateOnlineBatchById(@Param("list") List<DeviceProjectRelation> channelLists);

    @SqlParser(filter = true)
    BEquipmentAssets selectByAssetId(@Param("assetId") Integer assetId);

    @SqlParser(filter=true)
    List<Map<String, Object>> selectRoomDataList(@Param("timeValue") String timeValue);
}
