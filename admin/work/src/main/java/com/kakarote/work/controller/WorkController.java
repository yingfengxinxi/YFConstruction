package com.kakarote.work.controller;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Dict;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.work.entity.BO.*;
import com.kakarote.work.entity.PO.Work;
import com.kakarote.work.entity.VO.TaskInfoVO;
import com.kakarote.work.entity.VO.WorkInfoVo;
import com.kakarote.work.service.IWorkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 项目表 前端控制器
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@RestController
@RequestMapping("/work")
@Api(tags = "项目")
@Slf4j
public class WorkController {
    @Autowired
    private IWorkService workService;

    @PostMapping("/addWork")
    @ApiOperation("新建项目")
    public Result<Work> addWork(@RequestBody Work work) {
        Work work1 = workService.addWork(work);
        return R.ok(work1);
    }

    @PostMapping("/updateWork")
    @ApiOperation("编辑项目")
    public Result<Work> updateWork(@RequestBody Work work) {
        Work work1 = workService.updateWork(work);
        return R.ok(work1);
    }

    @PostMapping("/getWorkById")
    @ApiOperation("编辑项目")
    public Result<WorkInfoVo> getWorkById(@RequestParam("workId") Integer workId) {
        WorkInfoVo workInfoVo = workService.queryById(workId);
        return R.ok(workInfoVo);
    }

    @PostMapping("/deleteWork/{workId}")
    @ApiOperation("删除项目")
    public Result deleteWork(@PathVariable @NotNull Integer workId) {
        workService.deleteWork(workId);
        return R.ok();
    }

    @PostMapping("/queryWorkNameList")
    @ApiOperation("查询项目信息列表")
    public Result queryWorkNameList() {
        return R.ok(workService.queryWorkNameList());
    }

    @PostMapping("/queryTaskByWorkId")
    @ApiOperation("查询项目任务模板")
    public Result queryTaskByWorkId(@RequestBody WorkTaskTemplateBO workTaskTemplateBO) {
        return R.ok(workService.queryTaskByWorkId(workTaskTemplateBO));
    }

    @PostMapping("/queryOwnerTaskListByWorkId")
    @ApiOperation("查询项目任务模板（负责人视图）")
    public Result queryOwnerTaskListByWorkId(@RequestBody WorkTaskTemplateBO workTaskTemplateBO) {
        return R.ok(workService.queryOwnerTaskListByWorkId(workTaskTemplateBO));
    }

    @PostMapping("/queryTaskFileByWorkId")
    @ApiOperation("根据项目id查询项目附件")
    public Result<BasePage<FileEntity>> queryTaskFileByWorkId(@RequestBody  QueryTaskFileByWorkIdBO QueryTaskFileByWorkIdBO) {
        BasePage<FileEntity> page = workService.queryTaskFileByWorkId(QueryTaskFileByWorkIdBO);
        return R.ok(page);
    }

    @PostMapping("/queryArchiveWorkList")
    @ApiOperation("查询归档项目列表")
    public Result queryArchiveWorkList(@RequestBody PageEntity pageEntity) {
        return R.ok(workService.queryArchiveWorkList(pageEntity));
    }

    @PostMapping("/workStatistics/{workId}")
    @ApiOperation("查询项目任务统计")
    public Result workStatistics(@PathVariable @NotNull String workId) {
        return R.ok(workService.workStatistics(workId));
    }

    @PostMapping("/queryWorkOwnerList/{workId}")
    @ApiOperation("查询项目成员列表")
    public Result queryWorkOwnerList(@PathVariable @NotNull Integer workId) {
        return R.ok(workService.queryWorkOwnerList(workId));
    }

    @PostMapping("/updateOrder")
    @ApiOperation("移动项目任务")
    public Result updateOrder(@RequestBody UpdateTaskClassBO updateTaskClassBO) {
        workService.updateOrder(updateTaskClassBO);
        return R.ok();
    }

    @PostMapping("/leave/{workId}")
    @ApiOperation("退出项目")
    public Result leave(@PathVariable @NotNull Integer workId) {
        workService.leave(workId, UserUtil.getUserId());
        return R.ok();
    }

    @PostMapping("/removeWorkOwnerUser")
    @ApiOperation("移除项目成员")
    public Result removeWorkOwnerUser(@RequestBody RemoveWorkOwnerUserBO workOwnerUserBO) {
        workService.leave(workOwnerUserBO.getWorkId(), workOwnerUserBO.getOwnerUserId());
        return R.ok();
    }

    @PostMapping("/queryOwnerRoleList/{workId}")
    @ApiOperation("查询项目成员角色列表")
    public Result queryOwnerRoleList(@PathVariable @NotNull Integer workId) {
        return R.ok(workService.queryOwnerRoleList(workId));
    }


    @PostMapping("/setOwnerRole")
    @ApiOperation("设置项目成员角色")
    public Result setOwnerRole(@RequestBody SetWorkOwnerRoleBO setWorkOwnerRoleBO) {
        return R.ok(workService.setOwnerRole(setWorkOwnerRoleBO));
    }

    @PostMapping("/deleteTaskList")
    @ApiOperation("删除任务列表")
    public Result deleteTaskList(@RequestBody DeleteTaskClassBO deleteTaskClassBO) {
        workService.deleteTaskList(deleteTaskClassBO);
        return R.ok();
    }

    @PostMapping("/archiveTask/{classId}")
    @ApiOperation("归档已完成任务")
    public Result archiveTask(@PathVariable Integer classId) {
        workService.archiveTask(classId);
        return R.ok();
    }

    @PostMapping("/archiveTaskByOwner")
    @ApiOperation("归档已完成任务")
    public Result archiveTaskByOwner(@RequestBody ArchiveTaskByOwnerBO archiveTaskByOwnerBO) {
        workService.archiveTask(archiveTaskByOwnerBO);
        return R.ok();
    }

    @PostMapping("/archList/{workId}")
    @ApiOperation("查询项目归档任务")
    public Result<List<TaskInfoVO>> archList(@PathVariable Integer workId) {
        List<TaskInfoVO> taskInfoVOS = workService.archList(workId);
        return R.ok(taskInfoVOS);
    }

    @PostMapping("/updateClassOrder")
    @ApiOperation("移动任务列表")
    public Result updateClassOrder(@RequestBody UpdateClassOrderBO updateClassOrderBO) {
        workService.updateClassOrder(updateClassOrderBO);
        return R.ok();
    }

    @PostMapping("/activation/{taskId}")
    @ApiOperation("激活已归档任务")
    public Result activation(@PathVariable Integer taskId) {
        workService.activation(taskId);
        return R.ok();
    }

    @PostMapping("/downloadExcel")
    @ApiOperation("导入模板")
    public void downloadExcel(HttpServletResponse response) {
        List<JSONObject> recordList = new LinkedList<>();
        recordList.add(new JSONObject().fluentPut("name", "任务名称").fluentPut("is_null", 1).fluentPut("type", 1));
        recordList.add(new JSONObject().fluentPut("name", "任务描述").fluentPut("is_null", 0).fluentPut("type", 1));
        recordList.add(new JSONObject().fluentPut("name", "开始时间").fluentPut("is_null", 0).fluentPut("type", 4));
        recordList.add(new JSONObject().fluentPut("name", "结束时间").fluentPut("is_null", 0).fluentPut("type", 4));
        recordList.add(new JSONObject().fluentPut("name", "负责人").fluentPut("is_null", 0).fluentPut("type", 1));
        recordList.add(new JSONObject().fluentPut("name", "参与人").fluentPut("is_null", 0).fluentPut("type", 1));
        recordList.add(new JSONObject().fluentPut("name", "所属任务列表").fluentPut("is_null", 1).fluentPut("type", 1));
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("任务导入表");
        sheet.setDefaultRowHeight((short) 400);
        CellStyle textStyle = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        for (int i = 0; i < recordList.size(); i++) {
            if (Objects.equals(recordList.get(i).getInteger("type"), 4)) {
                CellStyle dateStyle = wb.createCellStyle();
                DataFormat dateFormat = wb.createDataFormat();
                dateStyle.setDataFormat(dateFormat.getFormat(DatePattern.NORM_DATE_PATTERN));
                sheet.setDefaultColumnStyle(i, dateStyle);
            } else {
                sheet.setDefaultColumnStyle(i, textStyle);
            }
            sheet.setColumnWidth(i, 20 * 256);
        }
        CellStyle cellStyle = wb.createCellStyle();
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.setHeight((short) (6 * 256));
        cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //开启自动换行
        cellStyle.setWrapText(true);
        HSSFRichTextString title = new HSSFRichTextString("任务导入模板\n(*)为必填项\n负责人必须为系统用户\n如果项目内有2个名称相同的任务列表名默认取第一个，如果列表名不存在会创建新的列表\n多个参与人时分隔符请使用英文逗号");
        //一个单元格内设置两种字体风格
        Font firstFont = wb.createFont();
        firstFont.setFontHeightInPoints((short) 16);
        firstFont.setBold(true);
        Font secondFont = wb.createFont();
        secondFont.setFontHeightInPoints((short) 10);
        title.applyFont(0, 6, firstFont);
        title.applyFont(6, title.length(), secondFont);
        titleRow.createCell(0).setCellValue(title);
        titleRow.getCell(0).setCellStyle(cellStyle);
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, recordList.size() - 1);
        sheet.addMergedRegion(region);
        try {
            HSSFRow row = sheet.createRow(1);
            for (int i = 0; i < recordList.size(); i++) {
                JSONObject record = recordList.get(i);
                // 在第一行第一个单元格，插入选项
                HSSFCell cell = row.createCell(i);
                // 普通写入操作
                if (record.getInteger("is_null") == 1) {
                    cell.setCellValue(record.getString("name") + "(*)");
                } else {
                    cell.setCellValue(record.getString("name"));
                }
            }
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=task_import.xls");
            wb.write(response.getOutputStream());

        } catch (Exception e) {
            log.error("error", e);
        } finally {
            try {
                wb.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * excel导入任务
     */
    @PostMapping("/excelImport")
    @ApiOperation("导入模板")
    public Result<Dict> excelImport(@RequestParam("file") MultipartFile file, @RequestParam("workId") Integer workId) throws IOException {
        Dict dict = workService.excelImport(file, workId);
        return Result.ok(dict);
    }

    @PostMapping("/downloadErrorExcel")
    @ApiOperation("下载错误数据")
    public void downloadErrorExcel(@RequestParam("token") String token, HttpServletResponse response) {
        File file = new File(BaseUtil.UPLOAD_PATH + "excel/" + token);
        try {
            ServletUtil.write(response, new FileInputStream(file), "application/vnd.ms-excel;charset=utf-8", "task_import.xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/queryProject")
    @ApiOperation("查询客户下项目")
    @OperateLog(detail = "查询客户下项目",types= LogTypes.WORK ,behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>>  queryProject(@RequestParam("customerId") Integer customerId, @RequestBody PageEntity pageEntity) {
        return R.ok(workService.queryProject(customerId, pageEntity));
    }

}

