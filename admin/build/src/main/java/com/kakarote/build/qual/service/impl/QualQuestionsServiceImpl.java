package com.kakarote.build.qual.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceRecord;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.qual.entity.PO.QualQuestions;
import com.kakarote.build.qual.mapper.QualQuestionsMapper;
import com.kakarote.build.qual.service.IQualQuestionsService;
import com.kakarote.build.safety.entity.PO.BSafetyDangerSource;
import com.kakarote.core.common.SystemCodeEnum;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 质量问题库 服务实现类
 * </p>
 *
 * @author pz
 * @since 2021-03-22
 */
@Service
public class QualQuestionsServiceImpl extends BaseServiceImpl<QualQuestionsMapper, QualQuestions> implements IQualQuestionsService {

    public List<QualQuestions> queryByParentId(Integer parentId){
        List<QualQuestions> list = getBaseMapper().queryByParentId(parentId);
        return list;
    }

    @Override
    public List<Map<String, Object>> queryTextList() {
        List<Map<String, Object>> list = getBaseMapper().queryTextList();
        return list;
    }

    @Override
    public List<Map<String, Object>> queryAllList(Integer pId) {
        QueryWrapper<QualQuestions> wrapper = new QueryWrapper<>();
        // 查询条件
        wrapper.eq("parent_id", pId);
        List<Map<String, Object>> list = baseMapper.selectMaps(wrapper);

        for (Map<String, Object> q:list) {
            List<Map<String, Object>> children = queryAllList((Integer)q.get("questionId"));
            q.put("children",children);
        }
        return list;
    }

    @Override
    public boolean removeListById(Integer questionId) {

        // 需删除的主键列表
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(questionId);
        // 递归
        this.getChildId(ids, questionId);
        // 批量删除集合中的数据
        int i = getBaseMapper().deleteBatchIds(ids);

        return i > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject excelImport(MultipartFile file) {
        List<List<Object>> errList = new ArrayList<>();

        String filePath = getFilePath(file);
        AtomicReference<Integer> num = new AtomicReference<>(0);
        ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {

            if (rowIndex > 1) {

                QualQuestions question = new QualQuestions();
                String questionName = rowList.get(0).toString().trim();
                if (questionName != null && !questionName.equals("")) {
                    question.setQuestionName(questionName);
                } else {
                    // 如果质量问题名称为null 或者空字符串抛出异常
                    throw new CrmException(1001, "问题名称为空");
                }


                // 处理排序 如果未设置排序，默认设置为0
                String sort = rowList.get(1).toString().trim();
                if (sort != null && !sort.equals("")) {
                    question.setSort(Integer.valueOf(sort));
                } else {
                    question.setSort(0);
                }

                // 处理状态 如果未设置状态，默认设置启用 1
                String status = rowList.get(2).toString().trim();
                if (status != null && !status.equals("")) {
                    question.setStatus(Integer.valueOf(status));
                } else {
                    question.setStatus(1);
                }

                // 如果存在父级名字，需查询父级id，否则设置父id为0
                String parentName = rowList.get(3).toString().trim();
                if (parentName != null && !parentName.equals("")) {

                    // 查询父级id
                    LambdaQueryWrapper<QualQuestions> queryWrapper = new LambdaQueryWrapper();
                    queryWrapper.eq(QualQuestions::getQuestionName, parentName);
                    queryWrapper.last("limit 1");
                    QualQuestions pQuestion = getBaseMapper().selectOne(queryWrapper);

                    // 设置父级id
                    question.setParentId(pQuestion.getQuestionId());

                } else {
                    question.setParentId(0);
                }

                question.setRemarks(rowList.get(4).toString().trim());

                // 根据质量问题名称判断该问题是否存在，如果存在更新数据，否则插入
                LambdaQueryWrapper<QualQuestions> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.eq(QualQuestions::getQuestionName, question.getQuestionName());
                queryWrapper.last("limit 1");
                QualQuestions q = getBaseMapper().selectOne(queryWrapper);

                // 如果数据库中存在该问题名称, 更新数据
                if (q != null) {
                    LambdaUpdateWrapper<QualQuestions> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.set(QualQuestions::getSort, question.getSort());
                    updateWrapper.set(QualQuestions::getRemarks, question.getRemarks());
                    updateWrapper.eq(QualQuestions::getQuestionId, q.getQuestionId());

                    getBaseMapper().update(question, updateWrapper);
                } else {
                    save(question);
                }

            }
        });
        FileUtil.del(filePath);
        JSONObject result = new JSONObject().fluentPut("totalSize", num.get()).fluentPut("errSize", 0);
        /*if (errList.size() > 0) {
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
        }*/
        return result;
    }

    @Override
    public List<Map<String, Object>> selectQualHiddenDanger(Map<String,Object>map) {

        List<Map<String, Object>> list = getBaseMapper().selectQualHiddenDanger(map);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectHiddenDangerNumber(Map<String, Object> map) {
        List<Map<String, Object>> list = getBaseMapper().selectHiddenDangerNumber(map);
        return list;
    }

    // 递归查询出所有的子级id
    private void getChildId(ArrayList<Integer> ids, Integer id) {
        // 查询二级分类的对象
        QueryWrapper<QualQuestions> wrapper = new QueryWrapper<>();
        // 查询条件
        wrapper.eq("parent_id", id);
        List<QualQuestions> list = baseMapper.selectList(wrapper);

        for (QualQuestions qualQuestions:list) {
            Integer questionId = qualQuestions.getQuestionId();
            ids.add(questionId);
            this.getChildId(ids, questionId);
        }
    }

    private String getFilePath(MultipartFile file) {
        String dirPath = FileUtil.getTmpDirPath();
        try {
            InputStream inputStream = file.getInputStream();
            File fromStream = FileUtil.writeFromStream(inputStream, dirPath + "/" + IdUtil.simpleUUID() + file.getOriginalFilename());
            return fromStream.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件上传失败：" + e);
            throw new CrmException(SystemCodeEnum.SYSTEM_UPLOAD_FILE_ERROR);
        }
    }

}
