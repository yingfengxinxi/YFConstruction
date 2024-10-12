package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.PO.SingleBuildingInfo;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 单体楼座信息 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-03-15
 */
public interface SingleBuildingInfoMapper extends BaseMapper<SingleBuildingInfo> {

    Map<String, Object> getBuildingNumberByPro();

    List<Map<String, Object>> selectListByPro();

    SingleBuildingInfo getInfoById(@Param("id") Integer id);
}
