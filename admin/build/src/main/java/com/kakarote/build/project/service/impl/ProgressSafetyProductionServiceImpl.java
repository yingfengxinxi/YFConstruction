package com.kakarote.build.project.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.project.entity.BO.ProgressSafetyProductionBO;
import com.kakarote.build.project.entity.PO.ProgressSafetyProduction;
import com.kakarote.build.project.mapper.ProgressSafetyProductionMapper;
import com.kakarote.build.project.service.IProgressSafetyProductionService;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
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
 *  服务实现类
 * </p>
 *
 * @author shz
 * @since 2022-06-14
 */
@Service
public class ProgressSafetyProductionServiceImpl extends BaseServiceImpl<ProgressSafetyProductionMapper, ProgressSafetyProduction> implements IProgressSafetyProductionService {

      /* 查询数据列表
     *
       */
    @Override
    public BasePage<ProgressSafetyProductionBO> selectList(ProgressSafetyProductionBO bProgressSecurityBO)
    {
        bProgressSecurityBO.setOrdersBd("scoring_table",true);
        bProgressSecurityBO.setOrdersBd("sort",true);

        BasePage<ProgressSafetyProductionBO> adminMessageBasePage = getBaseMapper().selectDataList(bProgressSecurityBO.parse(),bProgressSecurityBO);

        return adminMessageBasePage;
    }

    /**
     * 新增数据
     *
     */
    @Override
    public String add(ProgressSafetyProduction bProgressSecurity)
    {
        bProgressSecurity.setCreateTime(new Date());
        save(bProgressSecurity);
        return  "1";
    }

    /**
     * 修改数据
     *
     */
    @Override
    public String edit(ProgressSafetyProduction bProgressSecurity)
    {
        bProgressSecurity.setUpdateTime(new Date());
        updateById(bProgressSecurity);
        return  "1";
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

                ProgressSafetyProduction bProgressSecurity =new ProgressSafetyProduction();

                bProgressSecurity.setCreateTime(new Date());
                bProgressSecurity.setProjectId(51);
                bProgressSecurity.setTenantId(15190);
                String securityEvaluationId = rowList.get(0).toString().trim();
                bProgressSecurity.setSecurityEvaluationName(securityEvaluationId);
                String securityEvaluationName = rowList.get(1).toString().trim();
                bProgressSecurity.setSecurityEvaluationName(securityEvaluationName);
                String securityEvaluationStandard = rowList.get(2).toString().trim();
                bProgressSecurity.setSecurityEvaluationStandard(securityEvaluationStandard);
                Integer sort = (Integer) rowList.get(3);
                bProgressSecurity.setSort(sort);
                save(bProgressSecurity);
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
}
