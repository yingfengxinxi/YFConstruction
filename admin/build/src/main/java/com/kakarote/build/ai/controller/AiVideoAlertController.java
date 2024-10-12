package com.kakarote.build.ai.controller;


import com.kakarote.build.ai.entity.BO.AiVideoAlertBO;
import com.kakarote.build.ai.entity.PO.AiVideoAlert;
import com.kakarote.build.ai.service.IAiVideoAlertService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shao
 * @since 2021-05-24
 */
@RestController
@RequestMapping("/aiVideoAlert")
public class AiVideoAlertController {

    @Autowired
    private IAiVideoAlertService aiVideoAlertService;

    /**
    * @Description:分页查询
    * @author shz
    * @date 2021-5-24 11:31
    * @param
    * @return
    */
    @PostMapping("/selectPage")
    @ApiOperation("查询视屏监控列表")
    @OperateLog(detail = "查询视屏监控列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<AiVideoAlert>> selectPage(@RequestBody AiVideoAlertBO aiVideoAlertBO){
        return Result.ok(aiVideoAlertService.selectPage(aiVideoAlertBO));
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("根据id查询视屏监控数据")
    @OperateLog(detail = "根据id查询视屏监控数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<AiVideoAlert> getById(@PathVariable Integer id){
        return Result.ok(aiVideoAlertService.selectInfoById(id));
    }

    @PostMapping("/update")
    @ApiOperation("修改视屏监控数据")
    @OperateLog(detail = "修改视屏监控数据", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result update(@RequestBody AiVideoAlert aiVideoAlert){
        UserInfo user = UserUtil.getUser();
        aiVideoAlert.setHandleBy(user.getRealname());
        aiVideoAlert.setHandleTime(new Date());
        aiVideoAlertService.updateById(aiVideoAlert);
        return Result.ok();
    }

    @PostMapping("/aiEqumentStatistic")
    @ApiOperation("AI设备数量统计")
    @OperateLog(detail = "AI设备数量统计", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result aiEqumentStatistic(@RequestBody AiVideoAlert aiVideoAlert){
        Map map=aiVideoAlertService.aiEqumentStatistic();
        return Result.ok(map);
    }
    @PostMapping("/aiEventTypeStatistic")
    @ApiOperation("AI事件类型数量统计")
    @OperateLog(detail = "AI事件类型数量统计", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result aiEventTypeStatistic(@RequestBody AiVideoAlert aiVideoAlert){
        List<Map> map=aiVideoAlertService.aiEventTypeStatistic();
        return Result.ok(map);
    }

    /**
     * AI识别报警-30天报警类型统计
     *
     * @return
     */
    @PostMapping("/selectAIAlarmList")
    @ApiOperation("AI识别报警-30天报警类型统计")
    @OperateLog(detail = "AI识别报警-30天报警类型统计", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectAIAlarmList() {
        return Result.ok(aiVideoAlertService.selectAIAlarmList());
    }
}

