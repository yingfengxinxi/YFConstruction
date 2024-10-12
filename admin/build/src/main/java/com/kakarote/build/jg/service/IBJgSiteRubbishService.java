package com.kakarote.build.jg.service;

import com.kakarote.build.jg.entity.BO.BCreditBadBO;
import com.kakarote.build.jg.entity.BO.BJgSiteRubbishBO;
import com.kakarote.build.jg.entity.PO.BCreditBad;
import com.kakarote.build.jg.entity.PO.BJgSiteRubbish;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 工地垃圾分类管理 服务类
 * </p>
 *
 * @author author
 * @since 2022-02-09
 */
public interface IBJgSiteRubbishService extends BaseService<BJgSiteRubbish> {
    /**
     * 查询数据列表
     *
     */
    public BasePage<BJgSiteRubbish> selectList(BJgSiteRubbishBO bJgSiteRubbishBO);
}
