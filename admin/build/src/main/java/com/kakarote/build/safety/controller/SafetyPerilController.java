package com.kakarote.build.safety.controller;


import com.kakarote.build.safety.entity.BO.SafetyPerilBo;
import com.kakarote.build.safety.entity.PO.SafetyPeril;
import com.kakarote.build.safety.service.ISafetyPerilService;
import com.kakarote.core.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 危大工程清单 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-03-29
 */
@RestController
@RequestMapping("/safetyPeril")
public class SafetyPerilController {

    @Autowired
    private ISafetyPerilService iSafetyPerilService;


    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SafetyPeril safetyPeril){
        if (safetyPeril.getId() == null) {
            iSafetyPerilService.save(safetyPeril);
        }else{
            iSafetyPerilService.updateById(safetyPeril);
        }
        //throw new RuntimeException();
        return Result.ok();
    }

    @PostMapping("/list")
    public Result list(@RequestBody SafetyPerilBo safetyPerilBo) {
        return Result.ok(iSafetyPerilService.selectByPage(safetyPerilBo));
    }

}

