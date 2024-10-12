package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.PO.ProgressModelsetDetails;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 进度管理--模板设置--明细表 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-04-28
 */
public interface ProgressModelsetDetailsMapper extends BaseMapper<ProgressModelsetDetails> {

    List<ProgressModelsetDetails> queryByPidAndMid(@Param("data") Map<String,Object> map);

    List<Map> queryMapByPidAndMid(@Param("data") Map<String,Object> map);

}
