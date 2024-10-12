package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.BO.BSafetySafetyEducationBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEducation;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 安全教育培训管理 服务类
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
public interface IBSafetySafetyEducationService extends BaseService<BSafetySafetyEducation> {


    /**
     * 查询数据列表
     *
     */
    public BasePage<BSafetySafetyEducation> selectList(BSafetySafetyEducationBO bSafetySafetyEducationBO);


    /**
     * 新增数据信息
     *
     */
    public int add(BSafetySafetyEducation bSafetySafetyEducation);


    /**
     * 查询单条数据信息
     *
     */
    public BSafetySafetyEducation selectById(String id);


    /**
     * 修改单条数据信息
     *
     */
    public int updateDetails(BSafetySafetyEducation bSafetySafetyEducation);



    /**
     * 删除单条数据信息
     *
     */
    public int deleteDetails(String id);

}
