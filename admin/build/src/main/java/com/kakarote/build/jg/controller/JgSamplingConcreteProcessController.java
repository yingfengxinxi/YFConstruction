package com.kakarote.build.jg.controller;


import com.kakarote.build.jg.entity.BO.JgSamplingConcreteBO;
import com.kakarote.build.jg.entity.BO.JgSamplingConcreteProcessBO;
import com.kakarote.build.jg.entity.PO.JgSamplingConcrete;
import com.kakarote.build.jg.entity.PO.JgSamplingConcreteProcess;
import com.kakarote.build.jg.service.IJgSamplingConcreteProcessService;
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
@RequestMapping("/jgSamplingConcreteProcess")
public class JgSamplingConcreteProcessController {

    @Autowired
    private IJgSamplingConcreteProcessService jgSamplingConcreteProcessService;

    @PostMapping("/list")
    @ApiOperation("查询混凝土取样数据")
    @OperateLog(detail = "查询混凝土取样数据", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<BasePage<JgSamplingConcreteProcess>> list(@RequestBody JgSamplingConcreteProcessBO samplingConcreteProcessBO) {
        return Result.ok(jgSamplingConcreteProcessService.selectByPage(samplingConcreteProcessBO));
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    public Result add(@Validated @RequestBody JgSamplingConcreteProcess jgSamplingConcreteProcess)
    {
        String falg=jgSamplingConcreteProcessService.add(jgSamplingConcreteProcess);

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
        return Result.ok(jgSamplingConcreteProcessService.getById(id));
    }

    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody JgSamplingConcreteProcess jgSamplingConcreteProcess)
    {

        String falg= jgSamplingConcreteProcessService.edit(jgSamplingConcreteProcess);
        return Result.ok();
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
        return Result.ok(jgSamplingConcreteProcessService.getByPid(id));
    }
}

