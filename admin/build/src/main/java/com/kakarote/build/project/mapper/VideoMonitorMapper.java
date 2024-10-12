package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.PO.VideoMonitor;
import com.kakarote.core.servlet.BaseMapper;

import java.util.List;

/**
 * <p>
 * 视频监控 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-03-19
 */
public interface VideoMonitorMapper extends BaseMapper<VideoMonitor> {

    List<VideoMonitor> queryByParentId(Integer parentId);

}
