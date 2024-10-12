package com.kakarote.build.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.project.entity.PO.VideoMonitor;
import com.kakarote.build.project.mapper.VideoMonitorMapper;
import com.kakarote.build.project.service.IVideoMonitorService;
import com.kakarote.build.qual.entity.PO.QualQuestions;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频监控 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-03-19
 */
@Service
public class VideoMonitorServiceImpl extends BaseServiceImpl<VideoMonitorMapper, VideoMonitor> implements IVideoMonitorService {

    @Autowired
    private VideoMonitorMapper videoMonitorMapper;

    @Override
    public List<VideoMonitor> queryByParentId(Integer parentId) {
        return videoMonitorMapper.queryByParentId(parentId);
    }

    @Override
    public List<Map<String, Object>> queryAllList(Integer pId) {
        QueryWrapper<VideoMonitor> wrapper = new QueryWrapper<>();
        // 查询条件
        wrapper.eq("parent_id", pId);
        List<Map<String, Object>> list = baseMapper.selectMaps(wrapper);
        for (Map<String, Object> q : list) {
            List<Map<String, Object>> children = queryAllList((Integer) q.get("id"));
            q.put("children", children);
            q.put("text", q.get("deviceName"));
            q.put("value", q.get("id").toString());
        }
        return list;
    }
}
