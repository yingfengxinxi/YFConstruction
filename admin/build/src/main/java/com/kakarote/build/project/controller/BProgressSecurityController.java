package com.kakarote.build.project.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.project.entity.BO.BProgressSecurityBO;
import com.kakarote.build.project.entity.PO.BProgressSecurity;
import com.kakarote.build.project.service.IBProgressSecurityService;
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
 * 施工建筑安全检查 前端控制器
 * </p>
 *
 * @author author
 * @since 2022-06-01
 */
@RestController
@RequestMapping("/bProgressSecurity")
public class BProgressSecurityController {

    @Autowired
    private IBProgressSecurityService iBProgressSecurityService;


    @RequestMapping("/list")
    @ApiOperation("查询数据列表")
    @OperateLog(detail = "查询数据列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BProgressSecurityBO>> list(@RequestBody BProgressSecurityBO bProgressSecurityBO)
    {
        BasePage<BProgressSecurityBO> list = iBProgressSecurityService.selectList(bProgressSecurityBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BProgressSecurity bProgressSecurity)
    {
        String falg=iBProgressSecurityService.add(bProgressSecurity);

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
        return Result.ok(iBProgressSecurityService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BProgressSecurity bProgressSecurity)
    {

        String falg= iBProgressSecurityService.edit(bProgressSecurity);
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

        return Result.ok( iBProgressSecurityService.removeById(id));
    }

    @PostMapping("/excelImport")
    @ApiOperation("excel导入考勤")
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file) {
        JSONObject object = iBProgressSecurityService.excelImport(file);
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
        BProgressSecurity security = new BProgressSecurity();
        security.setSecurityEvaluationId(id);
        List<BProgressSecurity> list = iBProgressSecurityService.list(new QueryWrapper<>(security));
        return  R.ok(list);
    }
}

