package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.PO.ProgressPlanModel;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 计划模型关联表 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2022-04-18
 */
public interface ProgressPlanModelMapper extends BaseMapper<ProgressPlanModel> {

    List<Map<String, Object>> selectModelListByPlanId(@Param("id") Integer id);

    Integer selectModelNumberByPlanId(@Param("data") Map lastPlanMid);
}
