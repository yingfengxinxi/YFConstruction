package com.kakarote.build.labour.apipm.controller;

import com.kakarote.build.labour.apipm.service.WorkerService;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
* @Description:工人信息对外接口
* @author shz
* @date 2022-2-23 14:19
*/
@RestController
@RequestMapping("/apiPerson/worker")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    /**
    * @Description:上传修改项目工人信息
    * @author shz
    * @date 2022-2-23 15:09
    * @param 
    * @return 
    */
    @PostMapping("/postWorker")
    @ApiOperation("上传修改项目工人信息")
    public Result postWorker(@RequestBody Map<String, Object> parpam){
        return workerService.postWorker(parpam);
    }

    /**
    * @Description:上传项目工人进/退场信息
    * @author shz
    * @date 2022-2-25 15:13
    * @param 
    * @return 
    */
    @PostMapping("/postWorkerAdvance")
    @ApiOperation("上传项目工人进/退场信息")
    public Result postWorkerAdvance(@RequestBody Map<String, Object> parpam){
        return workerService.postWorkerAdvance(parpam);
    }

    /**
    * @Description:上传/修改工人合同信息
    * @author shz
    * @date 2022-2-25 15:13
    * @param 
    * @return 
    */
    @PostMapping("/putWorkerContract")
    @ApiOperation("上传/修改工人合同信息")
    public Result putWorkerContract(@RequestBody Map<String, Object> parpam){
        return workerService.putWorkerContract(parpam);
    }

}
