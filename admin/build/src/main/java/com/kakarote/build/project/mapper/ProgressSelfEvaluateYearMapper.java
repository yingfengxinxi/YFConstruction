package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.BO.ProgressSelfEvaluateYearBO;
import com.kakarote.build.project.entity.PO.ProgressSelfEvaluateYear;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 年度自评 Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2022-06-17
 */
public interface ProgressSelfEvaluateYearMapper extends BaseMapper<ProgressSelfEvaluateYear> {


    BasePage<ProgressSelfEvaluateYearBO> selectList(BasePage<ProgressSelfEvaluateYearBO> page, @Param("data") ProgressSelfEvaluateYearBO selfEvaluateBO);

    List<ProgressSelfEvaluateYearBO> getEvaluateList(String id);
}
