package com.kakarote.build.jg.controller;


import com.kakarote.build.jg.entity.BO.BCreditBadBO;
import com.kakarote.build.jg.entity.BO.BCreditGoodBO;
import com.kakarote.build.jg.entity.PO.BCreditBad;
import com.kakarote.build.jg.entity.PO.BCreditGood;
import com.kakarote.build.jg.service.IBCreditBadService;
import com.kakarote.build.jg.service.IBCreditGoodService;
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
 * 信用良好行为 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-11-25
 */
@RestController
@RequestMapping("/bCreditGood")
public class BCreditGoodController {

    @Autowired
    private IBCreditGoodService iBCreditGoodService;

    @PostMapping("/list")
    @ApiOperation("查询信用良好行为列表")
    @OperateLog(detail = "查询信用良好行为列表", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<BasePage<BCreditGood>> list(@RequestBody BCreditGoodBO bCreditGoodBO) {
        return Result.ok(iBCreditGoodService.selectList(bCreditGoodBO));
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BCreditGood bCreditGood)
    {
        String falg=iBCreditGoodService.add(bCreditGood);

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
        return Result.ok(iBCreditGoodService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BCreditGood bCreditGood)
    {

        String falg= iBCreditGoodService.edit(bCreditGood);
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

        return Result.ok( iBCreditGoodService.removeById(id));
    }

}

