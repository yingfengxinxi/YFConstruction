package com.kakarote.admin.controller;

import java.util.List;

import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kakarote.admin.entity.PO.AdminDictData;
import com.kakarote.admin.service.IAdminDictDataService;
import com.kakarote.admin.service.IAdminDictTypeService;

/**
 * 数据字典信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/adminDict/data")
public class AdminDictDataController {
    @Autowired
    private IAdminDictDataService dictDataService;
    
    @Autowired
    private IAdminDictTypeService dictTypeService;

    @GetMapping("/list")
    @ApiOperation("查询字典表明细列表")
    @OperateLog(detail = "查询字典表明细列表--管理员用",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result list(AdminDictData dictData)
    {
        List<AdminDictData> list = dictDataService.selectDictDataList(dictData);
        return Result.ok(list);
    }

//    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
//    @PreAuthorize(hasPermi = "system:dict:export")
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, SysDictData dictData) throws IOException
//    {
//        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
//        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
//        util.exportExcel(response, list, "字典数据");
//    }

    /**
     * 查询字典数据详细
     */
    @GetMapping(value = "/{dictCode}")
    @ApiOperation("查询字典数据详细")
    @OperateLog(detail = "查询字典数据详细--管理员用",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result getInfo(@PathVariable Long dictCode)
    {
        return Result.ok(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    @ApiOperation("根据字典类型查询字典数据信息")
    @OperateLog(detail = "根据字典类型查询字典数据信息",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result dictType(@PathVariable String dictType)
    {
        return Result.ok(dictTypeService.selectDictDataByType(dictType));
    }

    /**
     * 新增字典类型
     */
    @PostMapping
    @ApiOperation("新增字典类型")
    @OperateLog(detail = "新增字典类型",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody AdminDictData dict)
    {
        dict.setCreateBy(UserUtil.getUser().getRealname());
        return Result.ok(dictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @PutMapping
    @ApiOperation("修改保存字典类型")
    @OperateLog(detail = "修改保存字典类型",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody AdminDictData dict)
    {
        dict.setUpdateBy(UserUtil.getUser().getRealname());
        return Result.ok(dictDataService.updateDictData(dict));
    }

    /**
     * 删除字典类型
     */
    @DeleteMapping("/{dictCodes}")
    @ApiOperation("删除字典类型")
    @OperateLog(detail = "删除字典类型",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result remove(@PathVariable Long[] dictCodes)
    {
        return Result.ok(dictDataService.deleteDictDataByIds(dictCodes));
    }
}
