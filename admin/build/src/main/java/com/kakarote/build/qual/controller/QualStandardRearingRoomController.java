package com.kakarote.build.qual.controller;


import com.kakarote.build.qual.entity.BO.QualStandardRearingRoomBO;
import com.kakarote.build.qual.entity.PO.QualStandardRearingRoom;
import com.kakarote.build.qual.service.IQualStandardRearingRoomService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 标养室监测信息 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2023-05-07
 */
@RestController
@RequestMapping("/qualStandardRearingRoom")
public class QualStandardRearingRoomController {

    @Autowired
    private IQualStandardRearingRoomService standardRearingRoomService;

    @PostMapping("/list")
    @ApiOperation("查询标养室监测数据")
    @OperateLog(detail = "查询标养室监测数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<QualStandardRearingRoom>> list(@RequestBody QualStandardRearingRoomBO standardRearingRoomBO) {
        return Result.ok(standardRearingRoomService.selectByPage(standardRearingRoomBO));
    }

}

