package com.kakarote.build.equipment.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataBO;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataEchartsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorData;
import com.kakarote.build.equipment.service.IEquipmentMonitorDataAtService;
import com.kakarote.build.equipment.service.IEquipmentMonitorDataService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测项实时数据表 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-05-20
 */
@RestController
@RequestMapping("/eqMonitorDataAt")
public class EquipmentMonitorDataAtController {

    @Autowired
    private IEquipmentMonitorDataAtService dataAtService;

    @GetMapping("/listByPro/{proId}")
    @ApiOperation("查询监测项实时数据-工程")
    @OperateLog(detail = "查询监测项实时数据-工程", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String,Object>>> listByPro(@PathVariable Integer proId) {
        return Result.ok(dataAtService.listByPro(proId));
    }

    /**
     * 查询温度、风力、噪音、PM2.5、PM10
     * @return
     */
    @PostMapping("/selectEnvDataAt")
    @ApiOperation("查询最新的环境监测数据")
    @OperateLog(detail = "查询最新的环境监测数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String,Object>> selectEnvDataAt() {
        return Result.ok(dataAtService.selectEnvDataAt());
    }

    /**
     * 三维大屏使用
     * @param equipmentId
     * @return
     */
    @GetMapping("/listByEquipment/{equipmentId}")
    @ApiOperation("查询监测项实时数据-工程")
    @OperateLog(detail = "查询监测项实时数据-工程", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String,Object>>> listByEquipment(@PathVariable Integer equipmentId) {
        return Result.ok(dataAtService.listByEquipment(equipmentId));
    }

}

