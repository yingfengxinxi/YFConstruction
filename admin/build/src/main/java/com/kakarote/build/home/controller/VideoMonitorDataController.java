package com.kakarote.build.home.controller;/**
 * @ClassName videoMonitorController
 * @Author wnj58
 * @Date 2021/9/24 16:28
 * @purpose
 * @Version 1.0
 **/

import com.kakarote.build.home.service.VideoMonitorDataService;
import com.kakarote.build.project.entity.BO.ProjectInfoBO;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 *@ClassName videoMonitorController
 *@Author wnj58
 *@Date 2021/9/24 16:28
 *@purpose
 *@Version 1.0
 **/
@RestController
@RequestMapping("/videoMonitorData")
public class VideoMonitorDataController {

    @Autowired
    private IProjectInfoService iProjectInfoService;

    @Autowired
    private VideoMonitorDataService videoMonitorDataService;

    @PostMapping("/videoEquiByPro")
    @ApiOperation("分项目统计视频设备")
    @OperateLog(detail = "分项目统计视频设备",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result videoEquiByPro(@RequestBody Map<String,Object> map){
        return  R.ok(videoMonitorDataService.videoEquiByPro( map));
    }

    @PostMapping("/videoEquiByProByCheckHox")
    @ApiOperation("分项目统计视频设备")
    @OperateLog(detail = "分项目统计视频设备",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result videoEquiByProByCheckHox(@RequestBody Map<String,Object> map){
        return  R.ok(videoMonitorDataService.videoEquiByProByCheckHox( map));
    }


    @PostMapping("/videoEquiByLocation")
    @ApiOperation("分位置统计视频设备")
    @OperateLog(detail = "分项目统计视频设备",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result videoEquiByLocation(@RequestBody Map<String,Object> map){
        return  R.ok(videoMonitorDataService.videoEquiByLocation( map));
    }


    @PostMapping("/latelyAccessPro")
    @ApiOperation("最近接入项目")
    @OperateLog(detail = "最近接入项目",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result latelyAccessPro(@RequestBody ProjectInfoBO projectInfo){
        projectInfo.setPageType(0);
        projectInfo.setProjectState("2");
        return R.ok(iProjectInfoService.queryPageListAll(projectInfo));
    }


    @PostMapping("/videoStreamList")
    @ApiOperation("默认展示的视频流列表")
    @OperateLog(detail = "默认展示的视频流列表",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result videoStreamList(@RequestBody Map<String,Object> map){
        return R.ok(videoMonitorDataService.videoStreamList(map));
    }

    /**
     * @Description:视频监控数状态
     * @author lzy
     * @date 2022-3-21 10:10
     * @param
     * @return
     */
    @PostMapping("/selectVideoNumber")
    @ApiOperation("视频监控数状态")
    @OperateLog(detail = "视频监控数状态",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectVideoNumber(){
        return  R.ok(videoMonitorDataService.selectVideoNumber());
    }

    /**
     * @Description:视频监控数状态 监管端使用 只查询在建项目
     * @author lzy
     * @date 2022-3-21 10:10
     * @param
     * @return
     */
    @PostMapping("/selectVideoNumberByPro")
    @ApiOperation("视频监控数状态")
    @OperateLog(detail = "视频监控数状态",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectVideoNumberByPro(){
        return  R.ok(videoMonitorDataService.selectVideoNumberByPro());
    }


    /**
     * @Description: 视频监控列表
     * @author lzy
     * @date 2022-3-21 10:10
     * @param
     * @return
     */
    @PostMapping("/selectVideoList")
    @ApiOperation("视频监控列表")
    @OperateLog(detail = "视频监控列表",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectVideoList(@RequestBody Map<String, Object> params){
        return  R.ok(videoMonitorDataService.selectVideoList(params));
    }

    /**
     * @description 视频监控在线、离线设备列表
     * @author wnj58
     * @Params [param]
     * @updateTime 2022/4/18 17:29
     * @return com.kakarote.core.common.Result
     * @throws
     */
    @PostMapping("/selectVideoOnlineList")
    @ApiOperation("视频监控状态列表")
    @OperateLog(detail = "视频监控状态列表",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> selectVideoOnlineList(@RequestBody Map<String, Object> param){
        return  R.ok(videoMonitorDataService.selectVideoOnlineList(param));
    }

    /**
    * @Description:根据塔机设备id查询吊钩监控信息
    * @author shz
    * @date 2022-7-20 14:54
    * @param 
    * @return 
    */
    @GetMapping("/selectVideoByEquipmentId/{id}")
    @ApiOperation("根据塔机设备id查询吊钩监控信息")
    @OperateLog(detail = "根据塔机设备id查询吊钩监控信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map> selectVideoById(@PathVariable @NotNull Integer id){
        return R.ok(videoMonitorDataService.selectVideoById(id));
    }

    /**
     * 视频设备在线验证
     * @param map
     * @return
     */
    @PostMapping("/videoOnline")
    @ApiOperation("视频设备在线验证")
    @OperateLog(detail = "视频设备在线验证",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result videoOnline(@RequestBody Map<String,Object> map) throws Exception {
        return  R.ok(videoMonitorDataService.videoOnline( map));
    }
}
