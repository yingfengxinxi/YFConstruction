package com.kakarote.build.material.controller;



import com.kakarote.build.material.entity.BO.BMaterialWeighingBO;
import com.kakarote.build.material.entity.PO.BMaterialWeighing;
import com.kakarote.build.material.service.IBMaterialWeighingService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.service.ISequenceService;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 物料称重 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-08-11
 */
@RestController
@RequestMapping("/bMaterialWeighing")
public class BMaterialWeighingController {



    @Autowired
    private IBMaterialWeighingService iBMaterialWeighingService;

    @Autowired
    private ISequenceService sequenceService;





    @RequestMapping("/list")
    @ApiOperation("查询安全教育培训管理列表")
    @OperateLog(detail = "查询安全教育培训管理列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BMaterialWeighingBO>> list(@RequestBody BMaterialWeighingBO bMaterialWeighingBO)
    {
        BasePage<BMaterialWeighingBO> list = iBMaterialWeighingService.selectList(bMaterialWeighingBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    public Result add(@Validated @RequestBody BMaterialWeighing bMaterialWeighing)
    {
        String falg=iBMaterialWeighingService.add(bMaterialWeighing);

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
        return Result.ok(iBMaterialWeighingService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BMaterialWeighing bMaterialWeighing)
    {

        String falg= iBMaterialWeighingService.edit(bMaterialWeighing);
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

        return Result.ok( iBMaterialWeighingService.removeById(id));
    }

    @PostMapping("/generateDocumentNumber")
    @ApiOperation("物料称重生成单据编号")
    @OperateLog(detail = "物料称重生成单据编号", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result<String> generateDocumentNumber() {
        UserInfo userInfo = UserUtil.getUser();
        Integer projectId = userInfo.getProjectId().intValue();
        return Result.ok(sequenceService.getDateSeq("2", projectId));
    }


}

