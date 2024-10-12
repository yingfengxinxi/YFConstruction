package com.kakarote.build.labour.apipm.controller;


import com.kakarote.build.labour.apipm.service.SafetyTrainingService;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
* @Description:上传培训主题信息
* @author shz
* @date 2022-3-1 16:02
*/
@RestController
@RequestMapping("/apiPerson/safetyTraining")
public class SafetyTrainingController {

    private SafetyTrainingService safetyTrainingService;

    /**
     * @Description:上传修改项目工人信息
     * @author shz
     * @date 2022-3-1 15:09
     * @param
     * @return
     */
    @PostMapping("/postSafetyTraining")
    @ApiOperation("上传培训主题信息")
    public Result postSafetyTraining(@RequestBody Map<String, Object> parpam){
        return safetyTrainingService.postSafetyTraining(parpam);
    }

    /**
     * @Description:上传培训人员列表数据
     * @author shz
     * @date 2022-3-1 15:09
     * @param
     * @return
     */
    @PostMapping("/safetyTrainingPeople")
    @ApiOperation("上传培训人员列表数据")
    public Result safetyTrainingPeople(@RequestBody Map<String, Object> parpam){
        return safetyTrainingService.safetyTrainingPeople(parpam);
    }


    /**
     * @Description:同步培训主题信息
     * @author shz
     * @date 2022-3-1 15:09
     * @param
     * @return
     */
    @PostMapping("/syncSafetyTraining")
    @ApiOperation("同步培训主题信息")
    public Result syncSafetyTraining(@RequestBody Map<String, Object> parpam){
        return safetyTrainingService.syncSafetyTraining(parpam);
    }


}
