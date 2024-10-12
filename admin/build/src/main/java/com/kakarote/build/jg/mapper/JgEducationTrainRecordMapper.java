package com.kakarote.build.jg.mapper;

import com.kakarote.build.jg.entity.BO.JgEducationTrainRecordBO;
import com.kakarote.build.jg.entity.PO.JgEducationTrainRecord;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 安全教育培训记录 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-11-23
 */
public interface JgEducationTrainRecordMapper extends BaseMapper<JgEducationTrainRecord> {

    BasePage<JgEducationTrainRecord> selectByPage(BasePage<JgEducationTrainRecord> parse, @Param("data") JgEducationTrainRecordBO jgEducationTrainRecordBO);
}
