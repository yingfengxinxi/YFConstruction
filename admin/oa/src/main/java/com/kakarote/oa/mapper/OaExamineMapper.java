package com.kakarote.oa.mapper;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.oa.entity.BO.ExamineExportBO;
import com.kakarote.oa.entity.BO.ExaminePageBO;
import com.kakarote.oa.entity.PO.OaExamine;
import com.kakarote.oa.entity.PO.OaExamineField;
import com.kakarote.oa.entity.VO.ExamineVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 审批表 Mapper 接口
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface OaExamineMapper extends BaseMapper<OaExamine> {

    BasePage<ExamineVO> myInitiate(BasePage<Object> parse, @Param("data") ExaminePageBO examinePageBO, @Param("userId") Long userId, @Param("isAdmin") boolean isAdmin,@Param("biStatus") Integer biStatus);

    List<Long> queryExamineUserByExamineLog(@Param("record") ExamineVO record);

    BasePage<ExamineVO> myOaExamine(BasePage<Object> parse, @Param("data") ExaminePageBO examinePageBO, @Param("userId") Long userId, @Param("isAdmin") boolean isAdmin);

    ExamineVO queryExamineById(String id);

    JSONObject getExamineById(@Param("tenantId") Long tenantId, @Param("examineId") String examineId);

    JSONObject queryExamineRecordById(String recordId);

    List<JSONObject> myInitiateExcel(@Param("data") ExamineExportBO examineExportBO,@Param("userId") Long userId,@Param("isAdmin") boolean isAdmin);

    List<JSONObject> myOaExamineExcel(@Param("data") ExamineExportBO examineExportBO,@Param("userId") Long userId,@Param("isAdmin") boolean isAdmin);

    List<JSONObject> queryTravelExamineList(@Param("examineIdList") List<Integer> examineIdList);

    List<JSONObject> queryCustomExamineList(@Param("examineIdList") List<Integer> examineIdList,@Param("batchIds") List<String> batchIdList,@Param("fieldMap") List<OaExamineField> fields);
    List<JSONObject> queryExamineList(List<Integer> examineIdList);
    BasePage<Map<String, Object>> queryMoney(BasePage<Map<String, Object>> baseMapper, @Param("customerId") Integer customerId);

    BasePage<Map<String, Object>> contractQueryMoney(BasePage<Map<String, Object>> baseMapper, @Param("contractId") Integer contractId);

    BasePage<Map<String, Object>> businessQueryMoney(BasePage<Map<String, Object>> baseMapper, @Param("businessId") Integer businessId);
}