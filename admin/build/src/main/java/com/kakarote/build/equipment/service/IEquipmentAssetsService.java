package com.kakarote.build.equipment.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.equipment.entity.BO.EquipmentAssetsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceDetail;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备资产 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
public interface IEquipmentAssetsService extends BaseService<EquipmentAssets> {

    BasePage<EquipmentAssets> selectByPage(EquipmentAssetsBO equipmentAssetsBO);

    List<EquipmentAssets> selectPlaneByType(Integer typeId);

    List<Map<String, Object>> listByLocationId(Integer locationId);

    List<EquipmentAssets> selectEquList(EquipmentAssetsBO equipmentAssetsBO);

    List<Map<String, Object>> selectSprayList();

    List<EquipmentAssets> selectTowerCrane();

    List<Map> selectAllOverview();

    List<Map> selectTowerCraneOverview();

    Map selectTowerCraneAlertNew();

    Map selectTowerCraneById(String id);

    List<Map> selectElevatorOverview();

    List<Map> selectPitOverview();

    List<Map> selectFormworkOverview();

    List<Map> selectHookOverview();

    Map selectAllAlertNew();

    Map selectPitAlertNew();

    Map selectFormworkAlertNew();

    Map selectElevatorAlertNew();

    Map selectElevatorById(String id);

    Map<String, Object> selectVideoMonitor(Map<String, Object> map);

    BasePage<EquipmentAssets> selectByPageGroup(EquipmentAssetsBO equipmentAssetsBO);

    //getEqListByMoType
    List<EquipmentAssets> getEqListByMoType(String moType);

    List<Map<String, Object>> EquipmentNum(Map<String, Object> param);

    List<Map<String, Object>> videoEquiByPro(Map<String, Object> map);

    List<Map<String, Object>> videoEquiByProByCheckHox(Map<String, Object> map);

    Result monitorSaveEqData(JSONObject jsonObject);

    Map<String,Object> getDeviceId(String assectId);

    BasePage<EquipmentAssets> equiStateList(EquipmentAssetsBO equipmentAssetsBO);

    //智慧工地  点数据存储
    Result receptEqData(JSONObject jsonObject);

    List<Map<String,Object>> equimentList(Map<String,Object> map);

    List<Map<String,Object>> equimentListByCheckHox(Map<String,Object> map);

    List<Map<String,Object>> groupList2(EquipmentAssetsBO equipmentAssetsBO);

    List<Map<String, Object>> videoEquiByLocation(Map<String,Object> map);

    List<Map<String,Object>> videoStreamList(Map<String,Object> map);

    List<Map<String, Object>> getLine(Map<String, Object> params);

    Map<String,Object> videoAccessNum(Map<String,Object> map);

    Map<String,Object> selectProjectAccessNum(Map<String,Object> map);

    List<Map<String,Object>> selectEquiAccessNum(Map<String,Object> map);

    List<Map<String,Object>> selectEquiAlarmNum(Map<String,Object> map);

    List<Map<String,Object>> selectVideoEquiList(Map<String,Object> map);

    Map<String, Object> selectVideoNumber();

    List<Map<String, Object>> selectVideoList(Map<String, Object> params);

    List<Map<String, Object>> selectThreeDimensional(Map<String, Object> params);

    BasePage<Map<String,Object>> selectVideoOnlineList(Map<String,Object> param);

    //2022-04-19  查询设备的数据列表
    Map<String, Object> selectEquipmentList(Map<String, Object> params);

    Map<String, Object> machineEquNumber();

    BasePage<Map<String, Object>> selectEquiListByType(EquipmentAssetsBO assetsBO);

    Map<String, Object> selectVideoNumberByPro();

    List<Map<String,Object>> selectByPageGroupCount(Map<String, Object> params);

    List<Map<String, Object>> getLineByThreeDimensional(Map<String, Object> params);

    BasePage<Map<String,Object>> selectEquiListByThree(EquipmentAssetsBO assetsBO);

    List<Map> selectAllTower();

    List<Map> selectAllElevator();

    Map selectVideoById(Integer id);

    BasePage<Map<String,Object>> selectProjectByEquipment(EquipmentAssetsBO equipmentAssetsBO);

    BasePage<EquipmentAssets> selectListByProject(EquipmentAssetsBO equipmentAssetsBO);

    List<EquipmentAssets> selectSprayEquiList();

    BasePage<EquipmentAssets> equiStateListNew(EquipmentAssetsBO equipmentAssetsBO);
}
