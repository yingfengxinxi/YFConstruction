package com.kakarote.build.gb28181.controller;/**
 * @ClassName YsyDockingController
 * @Author wnj58
 * @Date 2021/12/6 18:19
 * @purpose
 * @Version 1.0
 **/

import com.kakarote.build.gb28181.service.YsyDockingService;
import com.kakarote.core.common.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName YsyDockingController
 * @Author wnj58
 * @Date 2021/12/6 18:19
 * @purpose
 * @Version 1.0
 **/
@RestController
@RequestMapping("/ysy")
public class YsyDockingController {

    @Autowired
    private YsyDockingService ysyDockingService;

    @PostMapping("/playback")
    @ApiOperation("获取视频播放地址")
    @OperateLog(detail = "获取视频播放地址", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result playback(@RequestBody Map<String, Object> map) {
        return ysyDockingService.playback(map);
    }

    @PostMapping("/getAccessToken")
    @ApiOperation("获取萤石云授权token")
    @OperateLog(detail = "获取萤石云授权token", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getAccessToken(@RequestBody Map<String, Object> map) {
        return ysyDockingService.getAccessToken(map);
    }

    @PostMapping("/ptzControl")
    @ApiOperation("萤石云-云台控制")
    @OperateLog(detail = "萤石云-云台控制", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result ptzControl(@RequestBody Map<String, Object> map) {
        return ysyDockingService.ptzControl(map);
    }

    @PostMapping("/wvpPtzControl")
    @ApiOperation("开源平台-云台控制")
    @OperateLog(detail = "开源平台-云台控制", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result wvpPtzControl(@RequestBody Map<String, Object> map) {
        return ysyDockingService.wvpPtzControl(map);
    }

    @PostMapping("/wvpOpenUrl")
    @ApiOperation("开源平台-视频播放地址")
    @OperateLog(detail = "开源平台-视频播放地址", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result wvpOpenUrl(@RequestBody Map<String, Object> map) {
        return R.ok(ysyDockingService.wvpOpenUrl(map));
    }
}
