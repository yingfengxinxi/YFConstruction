package com.kakarote.build.safety.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.project.entity.BO.BProgressSecurityBO;
import com.kakarote.build.project.entity.PO.BProgressSecurity;
import com.kakarote.build.safety.entity.BO.BSafetyProductionBO;
import com.kakarote.build.safety.entity.PO.BSafetyProduction;
import com.kakarote.build.safety.mapper.BSafetyProductionMapper;
import com.kakarote.build.safety.service.IBSafetyProductionService;
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
 * 安全生产评价 服务实现类
 * </p>
 *
 * @author zjc
 * @since 2022-06-08
 */
@Service
public class BSafetyProductionServiceImpl extends BaseServiceImpl<BSafetyProductionMapper, BSafetyProduction> implements IBSafetyProductionService {

    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BSafetyProductionBO> selectList(BSafetyProductionBO bSafetyProductionBO)
    {
        bSafetyProductionBO.setOrdersBd("sort",false);
        BasePage<BSafetyProductionBO> adminMessageBasePage = getBaseMapper().selectDataList(bSafetyProductionBO.parse(),bSafetyProductionBO);

        return adminMessageBasePage;
    }

    /**
     * 新增数据
     *
     */
    @Override
    public String add(BSafetyProduction bSafetyProduction)
    {
        bSafetyProduction.setCreateTime(new Date());
        save(bSafetyProduction);
        return  "1";
    }

    /**
     * 修改数据
     *
     */
    @Override
    public String edit(BSafetyProduction bSafetyProduction)
    {
        bSafetyProduction.setUpdateTime(new Date());
        updateById(bSafetyProduction);
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

                BSafetyProduction bSafetyProduction=new BSafetyProduction();

                bSafetyProduction.setCreateTime(new Date());
                bSafetyProduction.setCreateBy(UserUtil.getUser().getRealname());
                bSafetyProduction.setProjectId(Integer.valueOf(UserUtil.getUser().getProjectId().toString()));
                bSafetyProduction.setTenantId(Integer.valueOf(UserUtil.getUser().getTenantId().toString()));
                String productionId = rowList.get(0).toString().trim();
                bSafetyProduction.setProductionId(productionId);
                String productionName = rowList.get(1).toString().trim();
                bSafetyProduction.setProductionName(productionName);
                String productionStandard = rowList.get(2).toString().trim();
                bSafetyProduction.setProductionStandard(productionStandard);
                String productionMethods = rowList.get(3).toString().trim();
                bSafetyProduction.setProductionMethods(productionMethods);
                String sort = rowList.get(4).toString().trim();
                bSafetyProduction.setSort(sort);
                save(bSafetyProduction);
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
