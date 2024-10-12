package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.BO.BQualSamplingProcessBO;
import com.kakarote.build.qual.entity.BO.BQualSamplingWitnessBO;
import com.kakarote.build.qual.entity.PO.BQualSamplingProcess;
import com.kakarote.build.qual.entity.PO.BQualSamplingWitness;
import com.kakarote.build.qual.mapper.BQualSamplingWitnessMapper;
import com.kakarote.build.qual.service.IBQualSamplingWitnessService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 见证取样表 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-07-30
 */
@Service
public class BQualSamplingWitnessServiceImpl extends BaseServiceImpl<BQualSamplingWitnessMapper, BQualSamplingWitness> implements IBQualSamplingWitnessService {


    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BQualSamplingWitnessBO> selectList(BQualSamplingWitnessBO bQualSamplingWitnessBO)
    {
        bQualSamplingWitnessBO.setOrdersBd("create_time",false);
        BasePage<BQualSamplingWitnessBO> adminMessageBasePage = getBaseMapper().selectDataList(bQualSamplingWitnessBO.parse(),bQualSamplingWitnessBO);

        return adminMessageBasePage;
    }

    @Override
    public String add(BQualSamplingWitness bQualSamplingWitness){
        bQualSamplingWitness.setCreateTime(new Date());
        bQualSamplingWitness.setCreateBy(UserUtil.getUser().getRealname());
        this.save(bQualSamplingWitness);
        return"";
    }

    @Override
    public String edit(BQualSamplingWitness bQualSamplingWitness){
        bQualSamplingWitness.setUpdateBy(UserUtil.getUser().getRealname());
        bQualSamplingWitness.setUpdateTime(new Date());
        this.updateById(bQualSamplingWitness);
        return"";
    }

    @Override
    public BasePage<BQualSamplingWitnessBO> selectListNew(BQualSamplingWitnessBO bQualSamplingWitnessBO) {
        bQualSamplingWitnessBO.setOrdersBd("a.create_time",false);
        BasePage<BQualSamplingWitnessBO> adminMessageBasePage = getBaseMapper().selectListNew(bQualSamplingWitnessBO.parse(),bQualSamplingWitnessBO);
        return adminMessageBasePage;
    }
}
