package com.kakarote.build.qual.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.qual.entity.PO.QualQuestions;
import com.kakarote.core.common.Result;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 质量问题库 服务类
 * </p>
 *
 * @author pz
 * @since 2021-03-22
 */
public interface IQualQuestionsService extends BaseService<QualQuestions> {

    List<QualQuestions> queryByParentId(Integer parentId);

    List<Map<String, Object>> queryTextList();

    List<Map<String, Object>> queryAllList(Integer pId);

    boolean removeListById(Integer dangerSourceId);

    JSONObject excelImport(MultipartFile file);

    List<Map<String, Object>> selectQualHiddenDanger(Map<String,Object>map);

    List<Map<String, Object>> selectHiddenDangerNumber(Map<String, Object> map);

//    int save(QualQuestions qualQuestions);
//
//    int update(QualQuestions qualQuestions);
//
//    Result delete(String questionId);
}
