package com.kakarote.build.jg.service;

import com.kakarote.build.jg.entity.BO.BCreditBadBO;
import com.kakarote.build.jg.entity.PO.BCreditBad;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 信用不良行为 服务类
 * </p>
 *
 * @author author
 * @since 2021-11-25
 */
public interface IBCreditBadService extends BaseService<BCreditBad> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BCreditBad> selectList(BCreditBadBO bCreditBadBO);

    public String add(BCreditBad bCreditBad);

    public String edit(BCreditBad bCreditBad);

}
