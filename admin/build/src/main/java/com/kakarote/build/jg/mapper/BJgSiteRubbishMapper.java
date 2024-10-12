package com.kakarote.build.jg.mapper;

import com.kakarote.build.jg.entity.BO.BCreditBadBO;
import com.kakarote.build.jg.entity.BO.BJgSiteRubbishBO;
import com.kakarote.build.jg.entity.PO.BCreditBad;
import com.kakarote.build.jg.entity.PO.BJgSiteRubbish;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 工地垃圾分类管理 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2022-02-09
 */
public interface BJgSiteRubbishMapper extends BaseMapper<BJgSiteRubbish> {
    BasePage<BJgSiteRubbish> selectDataList(BasePage<AdminMessage> parse, @Param("data") BJgSiteRubbishBO bJgSiteRubbishBO);
}
