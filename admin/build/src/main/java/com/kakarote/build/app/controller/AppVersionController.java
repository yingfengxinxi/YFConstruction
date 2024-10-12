package com.kakarote.build.app.controller;


import com.kakarote.build.app.service.IAppVersionService;
import com.kakarote.core.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shz
 * @since 2021-07-01
 */
@RestController
@RequestMapping("/appVersion")
public class AppVersionController {

    @Autowired
    public IAppVersionService iAppVersionService;

    @PostMapping("/selectNewVersion")
    public Result selectNewVersion(){
        return Result.ok(iAppVersionService.selectNewVersion());
    }
}

