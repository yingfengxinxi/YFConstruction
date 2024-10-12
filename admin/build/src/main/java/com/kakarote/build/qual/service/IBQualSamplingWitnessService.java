package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.BQualSamplingProcessBO;
import com.kakarote.build.qual.entity.BO.BQualSamplingWitnessBO;
import com.kakarote.build.qual.entity.PO.BQualSamplingProcess;
import com.kakarote.build.qual.entity.PO.BQualSamplingWitness;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 见证取样表 服务类
 * </p>
 *
 * @author author
 * @since 2021-07-30
 */
public interface IBQualSamplingWitnessService extends BaseService<BQualSamplingWitness> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BQualSamplingWitnessBO> selectList(BQualSamplingWitnessBO bQualSamplingWitnessBO);

    public String add(BQualSamplingWitness bQualSamplingWitness);

    public String edit(BQualSamplingWitness bQualSamplingWitness);

    BasePage<BQualSamplingWitnessBO> selectListNew(BQualSamplingWitnessBO bQualSamplingWitnessBO);
}
