package com.kakarote.build.safety.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.project.entity.BO.BProgressSecurityBO;
import com.kakarote.build.project.entity.PO.BProgressSecurity;
import com.kakarote.build.project.service.IBProgressSecurityService;
import com.kakarote.build.safety.entity.BO.BSafetyProductionBO;
import com.kakarote.build.safety.entity.PO.BSafetyProduction;
import com.kakarote.build.safety.service.IBSafetyProductionService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 安全生产评价 前端控制器
 * </p>
 *
 * @author zjc
 * @since 2022-06-08
 */
@RestController
@RequestMapping("/bSafetyProduction")
public class BSafetyProductionController {

    @Autowired
    private IBSafetyProductionService iBSafetyProductionService;


    @RequestMapping("/list")
    @ApiOperation("查询数据列表")
    @OperateLog(detail = "查询数据列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BSafetyProductionBO>> list(@RequestBody BSafetyProductionBO bSafetyProductionBO)
    {
        BasePage<BSafetyProductionBO> list = iBSafetyProductionService.selectList(bSafetyProductionBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BSafetyProduction bSafetyProduction)
    {
        String falg=iBSafetyProductionService.add(bSafetyProduction);

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
        return Result.ok(iBSafetyProductionService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BSafetyProduction bSafetyProduction)
    {

        String falg= iBSafetyProductionService.edit(bSafetyProduction);
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

        return Result.ok( iBSafetyProductionService.removeById(id));
    }

    @PostMapping("/excelImport")
    @ApiOperation("excel导入考勤")
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file) {
        JSONObject object = iBSafetyProductionService.excelImport(file);
        return R.ok(object);
    }

    /**
     * 下拉框使用
     * @return
     */
    @RequestMapping("/selectSafetyProduction")
    @ApiOperation("查询数据列表")
    @OperateLog(detail = "查询数据列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectSafetyProduction() {

        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id, production_name as name");
        List<Map<String, Object>> list = iBSafetyProductionService.listMaps(queryWrapper);
        return Result.ok(list);
    }
}

