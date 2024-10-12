package com.kakarote.build.project.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.project.entity.PO.VideoMonitor;
import com.kakarote.build.project.service.IVideoMonitorService;
import com.kakarote.build.qual.entity.PO.QualQuestions;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 视频监控 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-03-19
 */
@RestController
@RequestMapping("/videoMonitor")
public class VideoMonitorController {

    @Autowired
    private IVideoMonitorService iVideoMonitorService;

    /**
    * @Description:方法描述
    * @author shz
    * @date 2021-3-26 11:09
    * @param 
    * @return 
    */
    @PostMapping("/add")
    @ApiOperation("新增视频监控")
    @OperateLog(detail = "新增视频监控", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>> add(@RequestBody VideoMonitor videoMonitor){
        if(videoMonitor.getParentId() == null){
            videoMonitor.setParentId(0);
        }
        iVideoMonitorService.saveOrUpdate(videoMonitor);
        return R.ok();
    }

    @GetMapping("/queryByParentId/{parentId}")
    @ApiOperation("根据上级查询视频监控")
    @OperateLog(detail = "根据上级查询视频监控", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<VideoMonitor>> queryByParentId(@PathVariable("parentId") Integer parentId) {
        List<VideoMonitor> list = iVideoMonitorService.queryByParentId(parentId);
        return Result.ok(list);
    }
    @GetMapping("/queryAllList/{parentId}")
    @ApiOperation("根据上级查询视频监控")
    @OperateLog(detail = "根据上级查询视频监控", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String,Object>>> queryAllList(@PathVariable("parentId") Integer parentId) {
        List<Map<String,Object>> list = iVideoMonitorService.queryAllList(parentId);
        return Result.ok(list);
    }
    @PostMapping("/removeById/{id}")
    @ApiOperation("删除数据")
    @OperateLog(detail = "删除数据",types= LogTypes.BUILD ,behavior = LogBehavior.DELETE)
    public Result removeById(@PathVariable Integer id){
        iVideoMonitorService.removeById(id);
        return Result.ok();
    }

}

