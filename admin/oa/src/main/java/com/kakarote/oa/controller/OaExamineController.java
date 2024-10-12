package com.kakarote.oa.controller;


import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.oa.entity.BO.AuditExamineBO;
import com.kakarote.oa.entity.BO.ExamineExportBO;
import com.kakarote.oa.entity.BO.ExaminePageBO;
import com.kakarote.oa.entity.BO.GetExamineFieldBO;
import com.kakarote.oa.entity.PO.OaExamineCategory;
import com.kakarote.oa.entity.PO.OaExamineField;
import com.kakarote.oa.entity.VO.ExamineVO;
import com.kakarote.oa.service.IOaExamineCategoryService;
import com.kakarote.oa.service.IOaExamineFieldService;
import com.kakarote.oa.service.IOaExamineService;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 审批表 前端控制器
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@RestController
@RequestMapping("/oaExamine")
@Slf4j
public class OaExamineController {

    @Autowired
    private IOaExamineService oaExamineService;

    @Autowired
    private IOaExamineCategoryService examineCategoryService;

    @Autowired
    private IOaExamineFieldService examineFieldService;

    @ApiOperation("我发起的审批")
    @PostMapping("/myInitiate")
    public Result<BasePage<ExamineVO>> myInitiate(@RequestBody ExaminePageBO examinePageBO) {
        BasePage<ExamineVO> page = oaExamineService.myInitiate(examinePageBO);
        return Result.ok(page);
    }


    @ApiOperation("我审批的")
    @PostMapping("/myOaExamine")
    public Result<BasePage<ExamineVO>> myOaExamine(@RequestBody ExaminePageBO examinePageBO) {
        BasePage<ExamineVO> page = oaExamineService.myOaExamine(examinePageBO);
        return Result.ok(page);
    }

    @ApiOperation("查询详情或比编辑字段")
    @PostMapping("/getField")
    public Result<List<OaExamineField>> getField(@RequestBody GetExamineFieldBO getExamineFieldBO){
        List<OaExamineField> field = oaExamineService.getField(getExamineFieldBO);
        return Result.ok(field);
    }


    @PostMapping("/setOaExamine")
    @ApiOperation("创建审批")
    @OperateLog(detail="创建审批",types= LogTypes.OA,behavior=LogBehavior.INSERT)
    @GlobalTransactional(rollbackFor = Exception.class,timeoutMills = 120000)
    public Result setOaExamine(@RequestBody JSONObject jsonObject){
        oaExamineService.setOaExamine(jsonObject);
        return Result.ok();
    }


    @PostMapping("/auditExamine")
    @ApiOperation("审批")
    public Result auditExamine(@RequestBody AuditExamineBO auditExamineBO){
        oaExamineService.oaExamine(auditExamineBO);
        return Result.ok();
    }


    @PostMapping("/queryOaExamineInfo/{examineId}")
    @ApiOperation("查询审批详情")
    public Result<ExamineVO> queryOaExamineInfo(@PathVariable String examineId){
        ExamineVO examineVO = oaExamineService.queryOaExamineInfo(examineId);
        return Result.ok(examineVO);
    }

    @GetMapping("/getOaExamineInfo/{tenantId}/{examineId}")
    @ApiOperation("查询审批详情")
    public Result<JSONObject> getOaExamineInfo(@PathVariable Long tenantId, @PathVariable String examineId){
        JSONObject examineVO = oaExamineService.getOaExamineInfo(tenantId, examineId);
        return Result.ok(examineVO);
    }


    @PostMapping("/queryExamineRecordList")
    @ApiOperation("查询审批步骤")
    public Result<JSONObject> queryExamineRecordList(@RequestParam("recordId") String recordId){
        JSONObject jsonObject = oaExamineService.queryExamineRecordList(recordId);
        return Result.ok(jsonObject);
    }


    @PostMapping("/queryExamineLogList")
    @ApiOperation("查询审批历史")
    public Result<List<JSONObject>> queryExamineLogList(@RequestParam("recordId") Integer recordId){
        List<JSONObject> jsonObjects = oaExamineService.queryExamineLogList(recordId);
        return Result.ok(jsonObjects);
    }


    @PostMapping("/deleteOaExamine")
    @ApiOperation("删除审批")
    public Result deleteOaExamine(@RequestParam("examineId") Integer examineId){
        oaExamineService.deleteOaExamine(examineId);
        return Result.ok();
    }

    /**
     * @author hmb
     * 查询审批步骤
     */
    @PostMapping("/queryExaminStep")
    @ApiOperation("查询审批步骤")
    public Result<OaExamineCategory> queryExaminStep(@RequestParam("categoryId") String categoryId){
        OaExamineCategory examineCategory = oaExamineService.queryExaminStep(categoryId);
        return Result.ok(examineCategory);
    }




    /**
     * 导出
     */
    @PostMapping("/export")
    @ApiOperation("查询审批步骤")
    public void export(@RequestBody ExamineExportBO examineExportBO,HttpServletResponse response) {
        Integer categoryId = examineExportBO.getCategoryId();
        OaExamineCategory category = examineCategoryService.getById(categoryId);
        Integer type = category.getType();
        List<OaExamineField> fieldList = new ArrayList<>();
        if (type == 0) {
            fieldList = examineFieldService.lambdaQuery().eq(OaExamineField::getExamineCategoryId,categoryId).list();
        }
        List<Map<String, Object>> list = oaExamineService.export(examineExportBO,category,fieldList);
//        Aop.get(ActionRecordUtil.class).addExportActionRecord(CrmEnum.OA_EXAMINE, list.size());
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            Integer columnNum = 1;
            writer.addHeaderAlias("category", "审批类型");
            writer.addHeaderAlias("createTime", "创建日期");
            writer.addHeaderAlias("createUserName", "创建人");
            writer.addHeaderAlias("examineStatus", "状态");
            writer.addHeaderAlias("examineUserName", "当前审批人");
            writer.addHeaderAlias("previousExamineUserName", "上一审批人");
            switch (type) {
                case 1:
                    writer.addHeaderAlias("content", "审批内容");
                    writer.addHeaderAlias("remark", "备注");
                    columnNum = 9;
                    break;
                case 2:
                    writer.addHeaderAlias("content", "审批内容");
                    writer.addHeaderAlias("startTime", "开始时间");
                    writer.addHeaderAlias("endTime", "结束时间");
                    writer.addHeaderAlias("duration", "时长");
                    writer.addHeaderAlias("remark", "备注");
                    columnNum = 12;
                    break;
                case 3:
                    writer.addHeaderAlias("content", "出差事由");
                    writer.addHeaderAlias("remark", "备注");
                    writer.addHeaderAlias("duration", "出差总天数");
                    writer.addHeaderAlias("vehicle", "交通工具");
                    writer.addHeaderAlias("trip", "单程往返");
                    writer.addHeaderAlias("startAddress", "出发城市");
                    writer.addHeaderAlias("endAddress", "目的城市");
                    writer.addHeaderAlias("startTime", "开始时间");
                    writer.addHeaderAlias("endTime", "结束时间");
                    writer.addHeaderAlias("travelDuration", "时长");
                    writer.addHeaderAlias("description", "出差备注");
                    columnNum = 18;
                    break;
                case 4:
                    writer.addHeaderAlias("content", "加班原因");
                    writer.addHeaderAlias("startTime", "开始时间");
                    writer.addHeaderAlias("endTime", "结束时间");
                    writer.addHeaderAlias("duration", "加班总天数");
                    writer.addHeaderAlias("remark", "备注");
                    columnNum = 12;
                    break;
                case 5:
                    writer.addHeaderAlias("content", "差旅内容");
                    writer.addHeaderAlias("money", "报销总金额");
                    writer.addHeaderAlias("remark", "备注");
                    writer.addHeaderAlias("startAddress", "出发城市");
                    writer.addHeaderAlias("endAddress", "目的城市");
                    writer.addHeaderAlias("startTime", "开始时间");
                    writer.addHeaderAlias("endTme", "结束时间");
                    writer.addHeaderAlias("traffic", "交通费");
                    writer.addHeaderAlias("stay", "住宿费");
                    writer.addHeaderAlias("diet", "餐饮费");
                    writer.addHeaderAlias("other", "其他费用");
                    writer.addHeaderAlias("travelMoney", "合计");
                    writer.addHeaderAlias("description", "费用明细描述");
                    columnNum = 19;
                    break;
                case 6:
                    writer.addHeaderAlias("content", "审批内容");
                    writer.addHeaderAlias("money", "借款金额");
                    writer.addHeaderAlias("remark", "备注");
                    columnNum = 10;
                    break;
                case 0:
                    fieldList.forEach(field -> writer.addHeaderAlias(field.getFieldName(), field.getName()));
                    columnNum = fieldList.size() + 7;
                    break;
                default:
                    break;
            }
            writer.addHeaderAlias("relateCrmWork", "关联业务");
            writer.merge(columnNum - 1, "审批信息");
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < columnNum; i++) {
                writer.setColumnWidth(i, 20);
            }
            Cell cell = writer.getCell(0, 0);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=oa_examine.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出日志错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
    }

    @PostMapping("/transfer")
    @ApiOperation("转换审批")
    public Result<List<ExamineVO>> transfer(@RequestBody List<ExamineVO> recordList){
        List<ExamineVO> transfer = oaExamineService.transfer(recordList);
        return Result.ok(transfer);
    }

    @PostMapping("/queryMoney")
    @ApiOperation("查询客户下费用审批")
    @OperateLog(detail = "查询客户下项目",types= LogTypes.OA ,behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>>  queryMoney(@RequestParam("customerId") Integer customerId, @RequestBody PageEntity pageEntity) {
        return Result.ok(oaExamineService.queryMoney(customerId, pageEntity));
    }

    @PostMapping("/contractQueryMoney")
    @ApiOperation("查询合同下费用审批")
    @OperateLog(detail = "查询合同下项目",types= LogTypes.OA ,behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>>  contractQueryMoney(@RequestParam("contractId") Integer contractId, @RequestBody PageEntity pageEntity) {
        return Result.ok(oaExamineService.contractQueryMoney(contractId, pageEntity));
    }

    @PostMapping("/businessQueryMoney")
    @ApiOperation("查询商机下费用审批")
    @OperateLog(detail = "查询商机下项目",types= LogTypes.OA ,behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>>  businessQueryMoney(@RequestParam("businessId") Integer businessId, @RequestBody PageEntity pageEntity) {
        return Result.ok(oaExamineService.businessQueryMoney(businessId, pageEntity));
    }

}

