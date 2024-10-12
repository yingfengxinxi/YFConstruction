package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.BO.BLabourWageBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.build.labour.entity.PO.BLabourWage;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工资管理 服务类
 * </p>
 *
 * @author author
 * @since 2021-04-26
 */
public interface IBLabourWageService extends BaseService<BLabourWage> {


    /**
     * 查询数据列表
     *
     */
    public BasePage<BLabourWage> selectList(BLabourWageBO bLabourWageBO);

    public String add(BLabourWage bLabourWage);

    public String edit(BLabourWage bLabourWage);

    public String validation(BLabourWage bLabourWage);

    void export(String ids,HttpServletResponse response);
}
