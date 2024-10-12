package com.kakarote.build.safety.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.project.entity.PO.BProgressSecurity;
import com.kakarote.build.safety.entity.BO.SafetyDynamicRiskBO;
import com.kakarote.build.safety.entity.PO.SafetyDynamicRisk;
import com.kakarote.build.safety.mapper.SafetyDynamicRiskMapper;
import com.kakarote.build.safety.service.ISafetyDynamicRiskService;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 动态风险数据 服务实现类
 * </p>
 *
 * @author shz
 * @since 2021-11-10
 */
@Service
public class SafetyDynamicRiskServiceImpl extends BaseServiceImpl<SafetyDynamicRiskMapper, SafetyDynamicRisk> implements ISafetyDynamicRiskService {

    @Override
    public BasePage<SafetyDynamicRisk> selectList(SafetyDynamicRiskBO safetyDynamicRisk) {
        safetyDynamicRisk.setOrdersBd("publish_date", false);
        return getBaseMapper().selectPageList(safetyDynamicRisk.parse(),safetyDynamicRisk);
    }

    /**
     * excel导入员工
     *
     * @param file file
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject excelImport(MultipartFile file) {
        List<List<Object>> errList = new ArrayList<>();
        String filePath = getFilePath(file);
        AtomicReference<Integer> num = new AtomicReference<>(0);
        ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {

            if (rowIndex > 1) {

                SafetyDynamicRisk safetyDynamicRisk =new SafetyDynamicRisk();


                safetyDynamicRisk.setCreateBy(UserUtil.getUser().getUserId());
                safetyDynamicRisk.setPublishDate(new Date());
                safetyDynamicRisk.setProjectId(Integer.valueOf(UserUtil.getUser().getProjectId().toString()));
                safetyDynamicRisk.setTenantId(Integer.valueOf(UserUtil.getUser().getTenantId().toString()));

                String riskNum = rowList.get(0).toString().trim();
                if(isNumeric(riskNum)){
                    int k = Integer.valueOf(riskNum);
                    if(k>100){
                        safetyDynamicRisk.setRiskNum("DR20220704"+riskNum);
                    }else if(k>10){
                        safetyDynamicRisk.setRiskNum("DR202207040"+riskNum);
                    }else{
                        safetyDynamicRisk.setRiskNum("DR2022070400"+riskNum);
                    }
                }else{
                    safetyDynamicRisk.setRiskNum(riskNum);
                }
                String riskName = rowList.get(1).toString().trim();
                safetyDynamicRisk.setRiskName(riskName);
                String riskDesc = rowList.get(2).toString().trim();
                safetyDynamicRisk.setRiskDesc(riskDesc);
                String partEngineering = rowList.get(3).toString().trim();
                safetyDynamicRisk.setPartEngineering(partEngineering);
                String riskLevel = rowList.get(4).toString().trim();
                safetyDynamicRisk.setRiskLevel(riskLevel);
                String controlLevel = rowList.get(5).toString().trim();
                safetyDynamicRisk.setControlLevel(controlLevel);
                if("enterprise".equals(controlLevel)){
                    safetyDynamicRisk.setDutyPerson("企业");
                }else if("project_department".equals(controlLevel)){
                    safetyDynamicRisk.setDutyPerson("项目部");
                }else if("construction_group".equals(controlLevel)){
                    safetyDynamicRisk.setDutyPerson("施工班组");
                }else if("worker".equals(controlLevel)){
                    safetyDynamicRisk.setDutyPerson("作业人员");
                }

                String accidentType = rowList.get(6).toString().trim();
                safetyDynamicRisk.setAccidentType(accidentType);
                String relieveStatus = rowList.get(7).toString().trim();
                safetyDynamicRisk.setRelieveStatus(relieveStatus);
                String engineeringMeasure = rowList.get(8).toString().trim();
                safetyDynamicRisk.setEngineeringMeasure(engineeringMeasure);
                String controlMeasures = rowList.get(9).toString().trim();
                safetyDynamicRisk.setControlMeasures(controlMeasures);
                String trainContent = rowList.get(10).toString().trim();
                safetyDynamicRisk.setTrainContent(trainContent);
                String individualProtection = rowList.get(11).toString().trim();
                safetyDynamicRisk.setIndividualProtection(individualProtection);
                String emergencyMeasures = rowList.get(12).toString().trim();
                safetyDynamicRisk.setEmergencyMeasures(emergencyMeasures);
                String content = rowList.get(13).toString().trim();
                safetyDynamicRisk.setContent(content);
                String riskAssessmentL = rowList.get(14).toString().trim();
                safetyDynamicRisk.setRiskAssessmentL(riskAssessmentL);
                String riskAssessmentE = rowList.get(15).toString().trim();
                safetyDynamicRisk.setRiskAssessmentE(riskAssessmentE);
                String riskAssessmentC = rowList.get(16).toString().trim();
                safetyDynamicRisk.setRiskAssessmentC(riskAssessmentC);
                String riskAssessmentD = rowList.get(17).toString().trim();
                safetyDynamicRisk.setRiskAssessmentD(riskAssessmentD);

                save(safetyDynamicRisk);
            }
        });
        FileUtil.del(filePath);
        JSONObject result = new JSONObject().fluentPut("totalSize", num.get()).fluentPut("errSize", 0);
        if (errList.size() > 0) {
            BigExcelWriter writer = null;
            try {
                String token = IdUtil.simpleUUID();
                writer = ExcelUtil.getBigWriter(FileUtil.getTmpDirPath() + "/" + token);
                writer.merge(6, "系统用户导入模板(*)为必填项");
                for (int i = 0; i < 7; i++) {
                    writer.setColumnWidth(i, 20);
                }
                writer.setDefaultRowHeight(20);
                Cell cell = writer.getCell(0, 0);
                CellStyle cellStyle = cell.getCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font font = writer.createFont();
                font.setBold(true);
                font.setFontHeightInPoints((short) 16);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                writer.writeHeadRow(Arrays.asList("错误信息", "姓名(*)", "身份证号(*)", "考勤卡号(*)", "打卡方向", "打卡时间"));
                writer.write(errList);
                result.fluentPut("errSize", errList.size()).fluentPut("token", token);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
        return result;
    }

    private String getFilePath(MultipartFile file) {
        String dirPath = FileUtil.getTmpDirPath();
        try {
            InputStream inputStream = file.getInputStream();
            File fromStream = FileUtil.writeFromStream(inputStream, dirPath + "/" + IdUtil.simpleUUID() + file.getOriginalFilename());
            return fromStream.getAbsolutePath();
        } catch (IOException e) {
            throw new CrmException(SystemCodeEnum.SYSTEM_UPLOAD_FILE_ERROR);
        }
    }

    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

}
