package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.BQualSamplingProcessBO;
import com.kakarote.build.qual.entity.PO.BQualSamplingProcess;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergencyMembers;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 见证取样过程表 服务类
 * </p>
 *
 * @author author
 * @since 2021-07-30
 */
public interface IBQualSamplingProcessService extends BaseService<BQualSamplingProcess> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BQualSamplingProcessBO> selectList(BQualSamplingProcessBO bQualSamplingProcessBO);

    public String add(BQualSamplingProcess bQualSamplingProcess);

    public String edit(BQualSamplingProcess bQualSamplingProcess);

    public String  deletePid(String pid);

    /**
     * 查询数据列表
     *
     */
    public List<BQualSamplingProcess> getByPId(String pid);

}
