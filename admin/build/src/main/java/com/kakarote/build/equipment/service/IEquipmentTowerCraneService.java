package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.PO.EquipmentTowerCrane;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 塔机配置表 服务类
 * </p>
 *
 * @author shz
 * @since 2021-11-18
 */
public interface IEquipmentTowerCraneService extends BaseService<EquipmentTowerCrane> {

    EquipmentTowerCrane getByDeviceId(Integer deviceId);

    Map getTdgx(Map map);

    List<Map> getTzphb(Map map);

    Map getQtdzxlfx(Map map);

    Map getXhdzcs(Map map);

    Map getDtzlztbfb(Map map);

    List getHsztddzcs(Map map);
}
