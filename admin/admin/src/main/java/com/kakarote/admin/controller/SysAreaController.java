package com.kakarote.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.admin.entity.BO.SysAreaBO;
import com.kakarote.admin.entity.PO.SysArea;
import com.kakarote.admin.service.ISysAreaService;
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
 * 区划表 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-03-18
 */
@RestController
@RequestMapping("/sysArea")
public class SysAreaController {

    @Autowired
    private ISysAreaService sysAreaService;

    /**
     * 区划查询
     */
    @ApiOperation("区划查询")
    @PostMapping("/list")
    public Result<List<SysArea>> list(@RequestBody SysAreaBO sysAreaBO){
        List<SysArea> list = sysAreaService.selectAreaList(sysAreaBO);
        return Result.ok(list);
    }
}

