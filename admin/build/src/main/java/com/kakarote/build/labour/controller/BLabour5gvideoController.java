package com.kakarote.build.labour.controller;


import com.kakarote.build.labour.entity.BO.BLabour5gvideoBO;
import com.kakarote.build.labour.entity.PO.BLabour5gvideo;
import com.kakarote.build.labour.service.IBLabour5gvideoService;
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
 * 5G超高清全景视频上传 前端控制器
 * </p>
 *
 * @author author
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/bLabour5gvideo")
public class BLabour5gvideoController {

    @Autowired
    private IBLabour5gvideoService iBLabour5gvideoService;


    @RequestMapping("/list")
    @ApiOperation("查询数据列表")
    @OperateLog(detail = "查询数据列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BLabour5gvideoBO>> list(@RequestBody BLabour5gvideoBO bLabour5gvideoBO)
    {
        BasePage<BLabour5gvideoBO> list = iBLabour5gvideoService.selectList(bLabour5gvideoBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BLabour5gvideo bLabour5gvideo)
    {
        String falg=iBLabour5gvideoService.add(bLabour5gvideo);

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
        return Result.ok(iBLabour5gvideoService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BLabour5gvideo bLabour5gvideo)
    {

        String falg= iBLabour5gvideoService.edit(bLabour5gvideo);
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

        return Result.ok( iBLabour5gvideoService.removeById(id));
    }

}

