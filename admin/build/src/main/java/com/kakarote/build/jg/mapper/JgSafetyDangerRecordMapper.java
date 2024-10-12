package com.kakarote.build.jg.mapper;

import com.kakarote.build.jg.entity.BO.JgSafetyDangerRecordBO;
import com.kakarote.build.jg.entity.PO.JgSafetyDangerRecord;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 安全隐患记录 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-11-26
 */
public interface JgSafetyDangerRecordMapper extends BaseMapper<JgSafetyDangerRecord> {

    BasePage<JgSafetyDangerRecord> selectByPage(BasePage<Object> parse, @Param("data") JgSafetyDangerRecordBO jgSafetyDangerRecordBO);
}
