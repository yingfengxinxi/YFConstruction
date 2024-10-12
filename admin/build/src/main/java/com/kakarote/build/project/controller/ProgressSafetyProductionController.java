package com.kakarote.build.project.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.project.entity.BO.BProgressSecurityBO;
import com.kakarote.build.project.entity.BO.ProgressSafetyProductionBO;
import com.kakarote.build.project.entity.PO.ProgressSafetyProduction;
import com.kakarote.build.project.service.IProgressSafetyProductionService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shz
 * @since 2022-06-14
 */
@RestController
@RequestMapping("/progressSafetyProduction")
public class ProgressSafetyProductionController {

    @Autowired
    private IProgressSafetyProductionService safetyProductionService;

    @RequestMapping("/list")
    @ApiOperation("查询数据列表")
    @OperateLog(detail = "查询数据列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<ProgressSafetyProductionBO>> list(@RequestBody ProgressSafetyProductionBO bProgressSecurityBO)
    {
        BasePage<ProgressSafetyProductionBO> list = safetyProductionService.selectList(bProgressSecurityBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody ProgressSafetyProduction bProgressSecurity)
    {
        String falg=safetyProductionService.add(bProgressSecurity);

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
        return Result.ok(safetyProductionService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody ProgressSafetyProduction bProgressSecurity)
    {

        String falg= safetyProductionService.edit(bProgressSecurity);
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

        return Result.ok( safetyProductionService.removeById(id));
    }

    @PostMapping("/excelImport")
    @ApiOperation("excel导入考勤")
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file) {
        JSONObject object = safetyProductionService.excelImport(file);
        return R.ok(object);
    }
    /**
     * @Description:根据评定项目id查询
     * @author shz
     * @date 2022-6-7 14:09
     * @param
     * @return
     */
    @GetMapping(value = "/selectByEvaluationId/{id}")
    @ApiOperation("根据评定项目id查询")
    @OperateLog(detail = "根据评定项目id查询",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result selectByEvaluationId(@PathVariable String id){
        ProgressSafetyProduction security = new ProgressSafetyProduction();
        security.setScoringTable(id);
        List<ProgressSafetyProduction> list = safetyProductionService.list(new QueryWrapper<>(security));
        return  R.ok(list);
    }
}

