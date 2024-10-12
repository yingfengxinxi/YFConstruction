package com.kakarote.build.jg.controller;


import com.kakarote.build.jg.entity.BO.JgSamplingConcreteBO;
import com.kakarote.build.jg.entity.PO.JgSamplingConcrete;
import com.kakarote.build.jg.service.IJgSamplingConcreteService;
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
 *  前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-11-27
 */
@RestController
@RequestMapping("/jgSamplingConcrete")
public class JgSamplingConcreteController {

    @Autowired
    private IJgSamplingConcreteService jgSamplingConcreteService;

    @PostMapping("/list")
    @ApiOperation("查询混凝土取样数据")
    @OperateLog(detail = "查询混凝土取样数据", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<BasePage<JgSamplingConcrete>> list(@RequestBody JgSamplingConcreteBO samplingConcreteBO) {
        return Result.ok(jgSamplingConcreteService.selectByPage(samplingConcreteBO));
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    public Result add(@Validated @RequestBody JgSamplingConcrete jgSamplingConcrete)
    {
        String falg=jgSamplingConcreteService.add(jgSamplingConcrete);

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
        return Result.ok(jgSamplingConcreteService.getById(id));
    }

    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody JgSamplingConcrete jgSamplingConcrete)
    {

        String falg= jgSamplingConcreteService.edit(jgSamplingConcrete);
        return Result.ok();
    }
}

