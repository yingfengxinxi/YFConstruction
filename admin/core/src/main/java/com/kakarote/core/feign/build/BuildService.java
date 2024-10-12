package com.kakarote.core.feign.build;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.feign.build.entity.LabourInfocollection;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;

@FeignClient(name = "build",contextId = "buildEqui")
public interface BuildService {

    @PostMapping("/monitorPointAlert/selectMonitorDataReport")
    public Result selectMonitorDataReport(@RequestBody Map<String, Object> params);

    @PostMapping("/equipmentAssets/recept")
    Result receptEqData(@RequestBody JSONObject jsonObject);

    @RequestMapping(value = "/adminUser/queryInfoByUserId")
    public Result<UserInfo> getUserInfo(@RequestParam("userId") Long userId);

    @GetMapping("/wxapp/{jsCode}")
    Result onLogion(@PathVariable("jsCode") String jsCode);

    @GetMapping("/wxapp")
    Result onBind(@RequestBody JSONObject bindData);

    @PostMapping("/projectInfo/WxslelctAll")
    Result WxslelctAll();

    @GetMapping("/labourInfocollection/getInfocollectionByPhone/{phone}")
    Result getInfocollectionByPhone(@PathVariable("phone") String phone);

    @PostMapping("/labourInfocollection/queryByIdCard/{idCard}")
    public Result queryByIdCard(@PathVariable("idCard") @NotNull String idCard);

    @PostMapping("/labourInfocollection/updateByIdCard")
    public Result updateByIdCard(@Validated @RequestBody LabourInfocollection labourWorktype);
    /**
     * 智慧工地打分，导出文件功能，查询导出数据。
     * @param id
     * @return
     */
    @PostMapping("/evalApply/selectExportDataById/{id}")
    public Result selectExportDataById(@PathVariable("id") String id);
}
