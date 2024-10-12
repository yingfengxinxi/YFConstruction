package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.BO.ProgressSelfEvaluateBO;
import com.kakarote.build.project.entity.PO.ProgressSelfEvaluate;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 月度自评 Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2022-06-07
 */
public interface ProgressSelfEvaluateMapper extends BaseMapper<ProgressSelfEvaluate> {

    BasePage<ProgressSelfEvaluateBO> selectList(BasePage<ProgressSelfEvaluateBO> page, @Param("data") ProgressSelfEvaluateBO selfEvaluateBO);

    List<ProgressSelfEvaluateBO> getEvaluateList(String id);

}
