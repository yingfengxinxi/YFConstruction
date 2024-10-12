package com.qdoner.dataagg.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.qdoner.dataagg.entity.PO.MonitorPoint;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 设备监测-监测点设置 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-05-18
 */
public interface MonitorPointMapper extends BaseMapper<MonitorPoint> {
    /**
     * 获取扬尘状态
     * @return
     */
    @Select("select online,asset_id from EquOnlineView where type_id ='41' and tenant_id is not null" +
            " and project_id is not null")
    List<Map<String,String>> getAssetStatus();
}
