package com.kakarote.build.equipment.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.equipment.entity.BO.EquipmentAssetsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.gb28181.entity.PO.DeviceProjectRelation;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备资产 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
public interface EquipmentAssetsMapper extends BaseMapper<EquipmentAssets> {

    BasePage<EquipmentAssets> selectByPage(BasePage<EquipmentAssets> parse, @Param("data") EquipmentAssetsBO equipmentAssetsBO);

    List<Map<String, Object>> listByLocationId(@Param("locationId") Integer locationId);

    List<EquipmentAssets> selectEquList(@Param("data") EquipmentAssetsBO equipmentAssetsBO);


    List<Map> selectEquipTypeByMap(@Param("data") String code);


    List<Map<String, Object>> selectSprayList(@Param("data") EquipmentAssetsBO equipmentAssetsBO);


    Map<String,Object> selectVideoMonitor(@Param("data") Map<String,Object> map);

    BasePage<EquipmentAssets> selectByPageGroup(BasePage<Object> parse, @Param("data") EquipmentAssetsBO equipmentAssetsBO);


    List<Map<String,Object>> EquipmentNum(Map<String,Object> param);

    List<Map<String,Object>> videoEquiByPro(@Param("data") Map<String,Object> map);

    List<Map<String,Object>> videoEquiByProByCheckHox(@Param("data") Map<String,Object> map);


    Map<String,Object> getDeviceId(String assectId);

    List<EquipmentAssets> getEqListByMoType(@Param("moType") String moType);
    
     BasePage<EquipmentAssets> equiStateList(BasePage<EquipmentAssets> parse, @Param("data") EquipmentAssetsBO equipmentAssetsBO);

    List<EquipmentAssets> selectPlaneByType(@Param("typeId") Integer typeId);

    @SqlParser(filter=true)
    List<Map<String,Object>> videoMonitorDevices(@Param("data") Map<String,Object> map);

    @SqlParser(filter=true)
    List<Map<String,Object>> videoMonitorChannels(@Param("deviceId")String deviceId);

    List<Map<String,Object>> equimentList(@Param("data")Map<String,Object> map);

    List<Map<String,Object>> equimentListByCheckHox(@Param("data")Map<String,Object> map);

    List<Map<String,Object>> groupList2(@Param("data")EquipmentAssetsBO equipmentAssetsBO);

    List<Map<String,Object>> getIsAllowControl(@Param("data")Map<String,Object> map);

    List<Map> selectAllOverview();

    List<Map> selectTowerCraneOverview();

    List<Map> selectTowerCraneAlertNew();

    List<Map> selectTowerCraneAlertYday();

    List<Map> selectTowerCraneAlert30day();

    Map selectTowerCraneById(String id);

    List<Map> selectElevatorOverview();

    List<Map> selectElevatorAlertNew();

    List<Map> selectElevatorAlertYday();

    List<Map> selectElevatorAlert30day();

    Map selectElevatorById(String id);

    List<Map> selectPitOverview();

    List<Map> selectFormworkOverview();

    List<Map> selectMonitorAlertNew(String type);


    List<Map> selectHookOverview();

    List<Map> selectMonitorAlertYday(String type);


    List<Map> selectMonitorAlert30day(String type);

    List<Map<String,Object>> videoEquiByLocation(@Param("data") Map<String,Object> map);

    List<Map<String,Object>> videoStreamList(@Param("data") Map<String,Object> map);


    List<Map<String, Object>> getLine(@Param("data") Map<String, Object> params);

    Map<String,Object> videoAccessNum(@Param("data") Map<String,Object> map);

    Map<String,Object> selectProjectAccessNum(@Param("data") Map<String,Object> map);

    List<Map<String,Object>> selectEquiAccessNum(@Param("data") Map<String,Object> map);

    List<Map<String,Object>> selectEquiAlarmNum(@Param("data") Map<String,Object> map);

    List<Map<String,Object>> selectVideoEquiList(Map<String,Object> map);

    Map<String, Object> selectVideoNumber();

    List<Map<String, Object>> selectVideoList(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectEnvEquiInfo(@Param("iconType") String iconType);

    List<Map<String, Object>> selectAssetInfoByMonitorType(@Param("data") Map<String, Object> map);

    List<Map<String, Object>> selectVideoEquiInfo(@Param("iconType") String iconType);

    List<Map<String, Object>> selectSprayEquiInfo(@Param("iconType") String iconType);

    BasePage<Map<String,Object>> selectVideoOnlineList(BasePage<Object> parse,@Param("data") Map<String,Object> param);


    // 2022-04-18  查询数据监测的数据列表
    List<Map<String, Object>> selectEquipmentList(@Param("data") Map<String, Object> param);
    // 2022-04-18  查询数据监测的数据列表总数
    Map<String, Object> selectEquipmentListCount(@Param("data") Map<String, Object> param);

    Map<String, Object> machineEquNumber();

    Map<String, Object> selectVideoNumberByPro();

    BasePage<Map<String, Object>> selectEquiListByType(BasePage<Object> parse, @Param("data") EquipmentAssetsBO assetsBO);

    List<Map<String,Object>> selectByPageGroupCount(Map<String, Object> params);

    List<Map<String, Object>> getLineByThreeDimensional(@Param("data") Map<String, Object> params);

    BasePage<Map<String, Object>> selectEquiListByThree(BasePage<Object> parse, @Param("data") EquipmentAssetsBO assetsBO);

    List<Map<String, Object>> selectAllTower();

    List<Map<String, Object>> selectAllElevator();

    Map selectVideoById(Integer id);

    BasePage<Map<String, Object>> selectProjectByEquipment(BasePage<Object> parse, @Param("data") EquipmentAssetsBO equipmentAssetsBO);

    BasePage<EquipmentAssets> selectListByProject(BasePage<Object> parse, @Param("data") EquipmentAssetsBO equipmentAssetsBO);

    List<EquipmentAssets> selectSprayEquiList(@Param("data") EquipmentAssetsBO equipmentAssetsBO);

    BasePage<EquipmentAssets> equiStateListNew(BasePage<Object> parse, @Param("data") EquipmentAssetsBO equipmentAssetsBO);

    @SqlParser(filter=true)
    int updateOnlineBatchById(@Param("list") List<DeviceProjectRelation> channelLists);
}
