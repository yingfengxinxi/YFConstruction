package com.qdoner.message.controller;


import cn.hutool.core.util.IdUtil;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.utils.UserUtil;
import com.qdoner.message.common.MqManageService;
import com.qdoner.message.entity.BO.ExchangesBO;
import com.qdoner.message.entity.PO.Exchanges;
import com.qdoner.message.service.IExchangesService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/msgExchanges")
public class ExchangesController {

    @Autowired
    private IExchangesService exchangesService;

    @Autowired
    private MqManageService mqManageService;

    /**
     * 分页列表
     */
    @PostMapping("/list")
    @ApiOperation("查询交换机列表")
    @OperateLog(detail = "查询交换机列表", types = LogTypes.OTHER, behavior = LogBehavior.SELECT)
    public Result list(@RequestBody ExchangesBO exchangesBO) {
        return Result.ok(exchangesService.queryPageList(exchangesBO));
    }

    /**
     * 获取详情
     */
    @PostMapping("/getById/{id}")
    @ApiOperation("查询交换机详情")
    @OperateLog(detail = "查询交换机详情", types = LogTypes.OTHER, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable String id) {
        return Result.ok(exchangesService.getById(id));
    }

    /**
     * 新增/修改
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation("维护交换机")
    @OperateLog(detail = "维护交换机", types = LogTypes.OTHER, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@RequestBody Exchanges exchanges) {
        if (exchanges.getId() == null) {
            exchanges.setId(IdUtil.randomUUID());
            exchanges.setCreateUserId(Integer.parseInt(UserUtil.getUserId().toString()));
            exchangesService.save(exchanges);
        }else{
            exchangesService.updateById(exchanges);
        }
        return Result.ok();
    }

    /**
     * 物理删除
     */
    @PostMapping("/delTrueByIds")
    @ApiOperation("删除交换机")
    @OperateLog(detail = "删除交换机", types = LogTypes.OTHER, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            Exchanges exchanges = exchangesService.getById(id);
            mqManageService.deleteExchange(exchanges.getName());
            exchangesService.removeById(id);
        }
        return Result.ok();
    }
    //部署
    /**
     * 获取详情
     */
    @PostMapping("/deployExchange/{id}")
    @ApiOperation("部署交换机")
    @OperateLog(detail = "部署交换机", types = LogTypes.OTHER, behavior = LogBehavior.OTHER)
    public Result deployExchange(@PathVariable String id) {
        Exchanges exchanges = exchangesService.getById(id);
        mqManageService.deployExchange(exchanges);
        exchanges.setDeployTime(new Date());
        exchangesService.updateById(exchanges);
        return Result.ok();
    }
}

