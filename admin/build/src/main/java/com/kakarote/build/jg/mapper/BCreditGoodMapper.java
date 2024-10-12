package com.kakarote.build.jg.mapper;

import com.kakarote.build.jg.entity.BO.BCreditBadBO;
import com.kakarote.build.jg.entity.BO.BCreditGoodBO;
import com.kakarote.build.jg.entity.PO.BCreditBad;
import com.kakarote.build.jg.entity.PO.BCreditGood;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 信用良好行为 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-11-25
 */
public interface BCreditGoodMapper extends BaseMapper<BCreditGood> {
    BasePage<BCreditGood> selectDataList(BasePage<AdminMessage> parse, @Param("data") BCreditGoodBO bCreditGoodBO);
}
