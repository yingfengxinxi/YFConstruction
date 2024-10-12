package com.kakarote.build.labour.controller;



import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.kakarote.build.labour.entity.BO.BLabourWageBO;
import com.kakarote.build.labour.entity.PO.BLabourWage;
import com.kakarote.build.labour.service.IBLabourWageDetailService;
import com.kakarote.build.labour.service.IBLabourWageService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工资管理 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-04-26
 */
@RestController
@RequestMapping("/bLabourWage")
public class BLabourWageController {

    @Autowired
    private IBLabourWageService iBLabourWageService;

    @Autowired
    private IBLabourWageDetailService iBLabourWageDetailService;

    @RequestMapping("/list")
    @ApiOperation("查询安全教育培训管理列表")
    @OperateLog(detail = "查询安全教育培训管理列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BLabourWage>> list(@RequestBody BLabourWageBO bLabourWageBO)
    {
        BasePage<BLabourWage> list = iBLabourWageService.selectList(bLabourWageBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BLabourWage bLabourWage)
    {
        String falg=iBLabourWageService.add(bLabourWage);

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
        return Result.ok(iBLabourWageService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody  BLabourWage bLabourWage)
    {

        String falg= iBLabourWageService.edit(bLabourWage);
        return Result.ok();
    }

    /**
     * 数据添加前验证
     */
    @PostMapping("/validation")
    @ApiOperation("数据验证")
    @OperateLog(detail = "数据验证",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result validation(@Validated @RequestBody BLabourWage bLabourWage)
    {

        String falg= iBLabourWageService.validation(bLabourWage);
        ;
        return Result.ok().setData(falg);
    }

    /**
     * 删除
     */
    @GetMapping(value = "/deleteDetails/{id}")
    @ApiOperation("删除数据详情")
    @OperateLog(detail = "删除数据详情",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable String id)
    {
        iBLabourWageDetailService.deleteByPid(id);
        return Result.ok( iBLabourWageService.removeById(id));
    }

    @PostMapping(value = "/export")
    @ApiOperation("数据导出")
    @OperateLog(detail = "数据导出",types= LogTypes.BUILD ,behavior = LogBehavior.EXPORT)
    public void export(@RequestBody Map<String,Object> map,HttpServletResponse response)
    {
       iBLabourWageService.export(String.valueOf(map.get("ids")),response);
    }
}

