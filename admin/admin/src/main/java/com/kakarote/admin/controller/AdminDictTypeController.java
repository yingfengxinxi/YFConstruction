package com.kakarote.admin.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.kakarote.admin.entity.BO.AdminDictTypeBO;
import com.kakarote.admin.entity.VO.AdminDeptVO;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
import com.kakarote.admin.entity.PO.AdminDictType;
import com.kakarote.admin.service.IAdminDictTypeService;

/**
 * 数据字典信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/adminDict/type")
@Api(tags = "字典管理相关接口")
@Slf4j
public class AdminDictTypeController {
    @Autowired
    private IAdminDictTypeService dictTypeService;

    @RequestMapping("/list")
    @ApiOperation("查询字典表列表")
    @OperateLog(detail = "查询字典表列表--管理员用",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<AdminDictType>> list(@RequestBody AdminDictTypeBO dictTypeBO)
    {
        BasePage<AdminDictType> list = dictTypeService.selectDictTypeList(dictTypeBO);
        return Result.ok(list);
    }

    @RequestMapping("/custom")
    @ApiOperation("自定义下拉框查询数据")
    @OperateLog(detail = "自定义下拉框查询数据--页面控件用",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<List<AdminDictType>> custome(@RequestBody Map<String,String> params)
    {
        List<AdminDictType> list = dictTypeService.selectCustomList(params);
        return Result.ok(list);
    }

//    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
//    @PreAuthorize(hasPermi = "system:dict:export")
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, AdminDictType dictType) throws IOException
//    {
//        List<AdminDictType> list = dictTypeService.selectDictTypeList(dictType);
//        ExcelUtil<AdminDictType> util = new ExcelUtil<AdminDictType>(AdminDictType.class);
//        util.exportExcel(response, list, "字典类型");
//    }

    /**
     * 查询字典类型详细
     */
    @GetMapping(value = "/{dictId}")
    @ApiOperation("查询字典类型详细")
    @OperateLog(detail = "查询字典类型详细",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result getInfo(@PathVariable Long dictId)
    {
        return Result.ok(dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 新增字典类型
     */
    @PostMapping
    @ApiOperation("新增字典类型")
    @OperateLog(detail = "新增字典类型",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody AdminDictType dict)
    {
        //TODO
//        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict)))
//        {
//            return AjaxResult.error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
//        }
        dict.setCreateBy(UserUtil.getUser().getRealname());
        return Result.ok(dictTypeService.insertDictType(dict));
    }

    /**
     * 修改字典类型
     */
    @PutMapping
    @ApiOperation("修改字典类型")
    @OperateLog(detail = "修改字典类型",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody AdminDictType dict)
    {
//        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict)))
//        {
//            return AjaxResult.error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
//        }
        dict.setUpdateBy(UserUtil.getUser().getRealname());
        return Result.ok(dictTypeService.updateDictType(dict));
    }

    /**
     * 删除字典类型
     */
    @DeleteMapping("/{dictIds}")
    @ApiOperation("删除字典类型")
    @OperateLog(detail = "删除字典类型",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable Long[] dictIds)
    {
        return Result.ok(dictTypeService.deleteDictTypeByIds(dictIds));
    }

    /**
     * 清空字典缓存
     */
    @DeleteMapping("/clearCache")
    @ApiOperation("清空字典缓存")
    @OperateLog(detail = "清空字典缓存",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result clearCache()
    {
        dictTypeService.clearCache();
        return Result.ok();
    }

//    /**
//     * 获取字典选择框列表
//     */
//    @GetMapping("/optionselect")
//    public AjaxResult optionselect()
//    {
//        List<AdminDictType> dictTypes = dictTypeService.selectDictTypeAll();
//        return AjaxResult.success(dictTypes);
//    }
}
