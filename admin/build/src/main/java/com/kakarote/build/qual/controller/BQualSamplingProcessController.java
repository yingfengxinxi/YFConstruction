package com.kakarote.build.qual.controller;


import com.kakarote.build.qual.entity.BO.BQualSamplingProcessBO;
import com.kakarote.build.qual.entity.PO.BQualSamplingProcess;
import com.kakarote.build.qual.service.IBQualSamplingProcessService;
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
 * 见证取样过程表 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-07-30
 */
@RestController
@RequestMapping("/bQualSamplingProcess")
public class BQualSamplingProcessController {


    @Autowired
    private IBQualSamplingProcessService iBQualSamplingProcessService;





    @RequestMapping("/list")
    @ApiOperation("查询安全教育培训管理列表")
    @OperateLog(detail = "查询安全教育培训管理列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BQualSamplingProcessBO>> list(@RequestBody BQualSamplingProcessBO bQualSamplingProcessBO)
    {
        BasePage<BQualSamplingProcessBO> list = iBQualSamplingProcessService.selectList(bQualSamplingProcessBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    public Result add(@Validated @RequestBody BQualSamplingProcess bQualSamplingProcess)
    {
        String falg=iBQualSamplingProcessService.add(bQualSamplingProcess);

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
        return Result.ok(iBQualSamplingProcessService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BQualSamplingProcess bQualSamplingProcess)
    {

        String falg= iBQualSamplingProcessService.edit(bQualSamplingProcess);
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

        return Result.ok( iBQualSamplingProcessService.removeById(id));
    }

    /**
     * 根据数据主键ID查询信息
     *
     * @param id 数据主键
     * @return
     */
    @GetMapping(value = "/selectByPId/{id}")
    @ApiOperation("查询单条数据详细")
    @OperateLog(detail = "查询单条数据详细",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result selectByPId(@PathVariable String id)
    {
        return Result.ok(iBQualSamplingProcessService.getByPId(id));
    }


}

