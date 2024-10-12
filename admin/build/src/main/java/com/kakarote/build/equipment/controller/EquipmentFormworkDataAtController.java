package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.service.IEquipmentFormworkDataAtService;
import com.kakarote.build.equipment.service.impl.EquipmentFormworkDataAtServiceImpl;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 高支模实时监测数据 前端控制器
 * </p>
 *
 * @author shz
 * @since 2022-03-30
 */
@RestController
@RequestMapping("/equipmentFormworkDataAt")
public class EquipmentFormworkDataAtController {

    @Autowired
    private IEquipmentFormworkDataAtService equipmentFormworkDataAtService;

    @PostMapping("/saveData")
    @ApiOperation("高支模实时数据上传接口")
    public Result saveData(@RequestBody List list) {
        return equipmentFormworkDataAtService.saveData(list);
    }
}

