package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.BO.BSafetySafetyDataBO;
import com.kakarote.build.safety.entity.BO.BSafetySafetyEducationBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyData;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEducation;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 安全资料管理 服务类
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
public interface IBSafetySafetyDataService extends BaseService<BSafetySafetyData> {


    /**
     * 查询数据列表
     *
     */
    public BasePage<BSafetySafetyData> selectList(BSafetySafetyDataBO bSafetySafetyDataBO);


    /**
     * 新增数据信息
     *
     */
    public int add(BSafetySafetyData bSafetySafetyData);


    /**
     * 查询单条数据信息
     *
     */
    public BSafetySafetyData selectById(String id);


    /**
     * 修改单条数据信息
     *
     */
    public int updateDetails(BSafetySafetyData bSafetySafetyData);



    /**
     * 删除单条数据信息
     *
     */
    public int deleteDetails(String id);

}
