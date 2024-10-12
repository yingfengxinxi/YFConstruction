package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.BEquipmentCertificateBO;
import com.kakarote.build.equipment.entity.PO.BEquipmentCertificate;
import com.kakarote.build.equipment.service.IBEquipmentCertificateService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 机械设备证书管理 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-08-04
 */
@RestController
@RequestMapping("/bEquipmentCertificate")
public class BEquipmentCertificateController {



    @Autowired
    private IBEquipmentCertificateService iBEquipmentCertificateService;





    @RequestMapping("/list")
    @ApiOperation("查询安全教育培训管理列表")
    @OperateLog(detail = "查询安全教育培训管理列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BEquipmentCertificateBO>> list(@RequestBody BEquipmentCertificateBO bEquipmentCertificateBO)
    {
        BasePage<BEquipmentCertificateBO> list = iBEquipmentCertificateService.selectList(bEquipmentCertificateBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    public Result add(@Validated @RequestBody BEquipmentCertificate bEquipmentCertificate)
    {
        String falg=iBEquipmentCertificateService.add(bEquipmentCertificate);

        return Result.ok();
    }

    /**
     * 根据数据主键ID查询信息
     *
     * @param id 数据主键
     * @return
     */
    @GetMapping(value = "/selectById/{id}")
    @ApiOperation("查询单条数据详细")
    @OperateLog(detail = "查询单条数据详细",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result getInfo(@PathVariable String id)
    {
        return Result.ok(iBEquipmentCertificateService.selectById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BEquipmentCertificate bEquipmentCertificate)
    {

        String falg= iBEquipmentCertificateService.edit(bEquipmentCertificate);
        return Result.ok();
    }

    /**
     * 删除
     */
    @GetMapping(value = "/deleteDetails/{id}")
    @ApiOperation("删除数据详情")
    @OperateLog(detail = "删除数据详情",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable String id)
    {

        return Result.ok( iBEquipmentCertificateService.removeById(id));
    }


}

