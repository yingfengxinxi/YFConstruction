package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.EquipmentMonitorPointBO;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorPoint;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测点设置 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-05-18
 */
public interface IEquipmentMonitorPointService extends BaseService<EquipmentMonitorPoint> {

    BasePage<EquipmentMonitorPoint> selectByPage(EquipmentMonitorPointBO monitorPointBO);

    BasePage<EquipmentMonitorPoint> listAllProject(EquipmentMonitorPointBO monitorPointBO);


    List<Map> selectPointsInfo(Map<String,Object>map);

    List<Map<String, Object>> selectRaiseDustData();

    List<EquipmentMonitorPoint> getMonitorList (String type);

    /**
     * 宁津项目主页--环境监测点统计
     * @param map
     * @return
     */
    Map<String, Object> EnvMonitPont(Map<String,Object> map);

    List<Map<String,Object>> EarlyWarningType(Map<String,Object> map);

    // 根据列表ID查询监测点信息
    List<EquipmentMonitorPoint> getPointListByIds (List<Integer> ids);

    Result getPointAndAtvalueByAssetId(String assetId);

    /**
    * @Description:监测数据接口
    * @author shz
    * @date 2022-4-11 11:53
    * @param
    * @return
    */
    EquipmentMonitorPoint getByIdInApi(Integer id);


}
