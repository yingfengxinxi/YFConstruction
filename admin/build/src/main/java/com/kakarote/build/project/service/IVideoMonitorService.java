package com.kakarote.build.project.service;

import com.kakarote.build.project.entity.PO.VideoMonitor;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 视频监控 服务类
 * </p>
 *
 * @author wudw
 * @since 2021-03-19
 */
public interface IVideoMonitorService extends BaseService<VideoMonitor> {

    List<VideoMonitor> queryByParentId(Integer parentId);


    List<Map<String,Object>> queryAllList(Integer parentId);
}
