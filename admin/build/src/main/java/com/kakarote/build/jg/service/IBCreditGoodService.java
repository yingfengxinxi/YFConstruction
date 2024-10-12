package com.kakarote.build.jg.service;

import com.kakarote.build.jg.entity.BO.BCreditBadBO;
import com.kakarote.build.jg.entity.BO.BCreditGoodBO;
import com.kakarote.build.jg.entity.PO.BCreditBad;
import com.kakarote.build.jg.entity.PO.BCreditGood;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 信用良好行为 服务类
 * </p>
 *
 * @author author
 * @since 2021-11-25
 */
public interface IBCreditGoodService extends BaseService<BCreditGood> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BCreditGood> selectList(BCreditGoodBO bCreditGoodBO);

    public String add(BCreditGood bCreditGood);

    public String edit(BCreditGood bCreditGood);

}
