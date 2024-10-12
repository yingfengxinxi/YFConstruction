package com.kakarote.build.project.service;

import com.kakarote.build.project.entity.PO.SingleBuildingInfo;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 单体楼座信息 服务类
 * </p>
 *
 * @author wudw
 * @since 2021-03-15
 */
public interface ISingleBuildingInfoService extends BaseService<SingleBuildingInfo> {

    //获取楼座信息，用于  进度管理--计划生成
    public Map selectLoft();

    // 查询当前项目的建筑数量
    Map<String, Object> getBuildingNumberByPro();

    List<Map<String, Object>> selectListByPro();

    SingleBuildingInfo getInfoById(Integer id);
}
