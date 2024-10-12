package com.kakarote.build.jg.mapper;

import com.kakarote.build.jg.entity.BO.JgEnforceRecordBO;
import com.kakarote.build.jg.entity.PO.JgEnforceRecord;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 视频监控执法记录 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-11-25
 */
public interface JgEnforceRecordMapper extends BaseMapper<JgEnforceRecord> {

    BasePage<JgEnforceRecord> selectByPage(BasePage<Object> parse,@Param("data") JgEnforceRecordBO jgEnforceRecordBO);
}
