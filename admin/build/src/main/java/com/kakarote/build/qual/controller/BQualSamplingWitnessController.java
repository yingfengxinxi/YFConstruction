package com.kakarote.build.qual.controller;


import com.kakarote.build.qual.entity.BO.BQualSamplingProcessBO;
import com.kakarote.build.qual.entity.BO.BQualSamplingWitnessBO;
import com.kakarote.build.qual.entity.PO.BQualSamplingProcess;
import com.kakarote.build.qual.entity.PO.BQualSamplingWitness;
import com.kakarote.build.qual.service.IBQualSamplingProcessService;
import com.kakarote.build.qual.service.IBQualSamplingWitnessService;
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
 * 见证取样表 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-07-30
 */
@RestController
@RequestMapping("/bQualSamplingWitness")
public class BQualSamplingWitnessController {


    @Autowired
    private IBQualSamplingProcessService iBQualSamplingProcessService;

    @Autowired
    private IBQualSamplingWitnessService iBQualSamplingWitnessService;






    @RequestMapping("/list")
    @ApiOperation("查询安全教育培训管理列表")
    @OperateLog(detail = "查询安全教育培训管理列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BQualSamplingWitnessBO>> list(@RequestBody BQualSamplingWitnessBO bQualSamplingWitnessBO)
    {
        BasePage<BQualSamplingWitnessBO> list = iBQualSamplingWitnessService.selectList(bQualSamplingWitnessBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    public Result add(@Validated @RequestBody BQualSamplingWitness bQualSamplingWitness)
    {
        String falg=iBQualSamplingWitnessService.add(bQualSamplingWitness);

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
        return Result.ok(iBQualSamplingWitnessService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BQualSamplingWitness bQualSamplingWitness)
    {

        String falg= iBQualSamplingWitnessService.edit(bQualSamplingWitness);
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
        iBQualSamplingProcessService.deletePid(id);
        return Result.ok(iBQualSamplingWitnessService.removeById(id));
    }

    /**
     * 取消项目过滤时使用，该接口返回项目名称
     * @param bQualSamplingWitnessBO
     * @return
     */
    @RequestMapping("/selectListNew")
    @ApiOperation("查询混凝土检验数据")
    @OperateLog(detail = "查询混凝土检验数据",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BQualSamplingWitnessBO>> selectListNew(@RequestBody BQualSamplingWitnessBO bQualSamplingWitnessBO)
    {
        BasePage<BQualSamplingWitnessBO> list = iBQualSamplingWitnessService.selectListNew(bQualSamplingWitnessBO);
        return Result.ok(list);
    }

}

