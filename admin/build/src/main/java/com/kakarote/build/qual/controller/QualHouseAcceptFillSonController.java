package com.kakarote.build.qual.controller;


import com.kakarote.build.qual.service.IQualHouseAcceptFillSonService;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 施工质量--分户验收--填报--子表 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-06-29
 */
@RestController
@RequestMapping("/qualHouseAcceptFillSon")
public class QualHouseAcceptFillSonController {

    @Autowired
    private IQualHouseAcceptFillSonService iQualHouseAcceptFillSonService;


    @GetMapping("/wxSelectByPid")
    public Result wxSelectByPid(String id){
        return Result.ok(iQualHouseAcceptFillSonService.wxSelectByPid(Integer.valueOf(id)));
    }
}

