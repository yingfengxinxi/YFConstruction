package com.kakarote.build.equipment.controller;/**
 * @ClassName aaaaaa
 * @Author wnj58
 * @Date 2022/6/17 15:15
 * @purpose
 * @Version 1.0
 **/

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.equipment.entity.BO.EquipmentFluoriteCloudBO;
import com.kakarote.build.equipment.entity.PO.EquipmentFluoriteCloud;
import com.kakarote.build.equipment.service.EquipmentFluoriteCloudService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 萤石云设备信息 前端控制器
 * </p>
 *
 * @author wnj58
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/equipmentFluoriteCloud")
public class EquipmentFluoriteCloudController {

@Autowired
private EquipmentFluoriteCloudService EquipmentFluoriteCloudService;

    @RequestMapping("/list")
    @ApiOperation(value="查询萤石设备列表")
    @OperateLog(detail = "查询萤石设备列表",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result getList(@RequestBody EquipmentFluoriteCloudBO equipmentFluoriteCloudBO){
        BasePage<Map> list=EquipmentFluoriteCloudService.getList(equipmentFluoriteCloudBO);
        return R.ok(list);
    }

    @RequestMapping("/saveOrUpdate")
    @ApiOperation(value="新增或修改")
    @OperateLog(detail = "新增或修改",types= LogTypes.BUILD ,behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@RequestBody EquipmentFluoriteCloud equipmentFluoriteCloud){
        if(equipmentFluoriteCloud.getAssetId()==null){
            //新增逻辑，暂不实现
        }else{
           boolean flag= EquipmentFluoriteCloudService.updateState(equipmentFluoriteCloud);
            System.out.println(flag);
        }
        return R.ok();
    }
    @RequestMapping("/getDetails/{id}")
    @ApiOperation(value="查询萤石设备详情")
    @OperateLog(detail = "查询萤石设备详情",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result getDetails(@PathVariable String id){
        Map map=EquipmentFluoriteCloudService.getDetails(id);
        return R.ok(map);
    }
    @PostMapping("/excelImport")
    @ApiOperation("excel导入设备")
    @OperateLog(detail = "excel导入设备",types= LogTypes.BUILD ,behavior = LogBehavior.IMPORT)
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file) {
        JSONObject object = EquipmentFluoriteCloudService.excelImport(file);
        return R.ok(object);
    }

    @PostMapping("/getAIEquipmentList")
    @ApiOperation("AI设备列表查询")
    @OperateLog(detail = "AI设备列表查询",types= LogTypes.BUILD ,behavior = LogBehavior.IMPORT)
    public Result getAIEquipmentList(@RequestBody EquipmentFluoriteCloudBO equipmentFluoriteCloudBO) {
       BasePage<Map> bp = EquipmentFluoriteCloudService.getAIEquipmentList(equipmentFluoriteCloudBO);
        return R.ok(bp);
    }

    @PostMapping("/addAIEquipment")
    @ApiOperation("AI设备新增")
    @OperateLog(detail = "AI设备新增",types= LogTypes.BUILD ,behavior = LogBehavior.IMPORT)
    public Result addAIEquipment(@RequestBody EquipmentFluoriteCloudBO equipmentFluoriteCloudBO) {
        boolean bp = EquipmentFluoriteCloudService.addAIEquipment(equipmentFluoriteCloudBO);
        return R.ok();
    }

    @PostMapping("/deleteAIEquipment")
    @ApiOperation("AI设备删除")
    @OperateLog(detail = "AI设备删除",types= LogTypes.BUILD ,behavior = LogBehavior.IMPORT)
    public Result deleteAIEquipment(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for(String id:ids){
            boolean bp = EquipmentFluoriteCloudService.delAIEquipment(id);
        }
        return R.ok();
    }
}
