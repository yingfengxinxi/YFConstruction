package com.kakarote.build.safety.service.impl;

import com.kakarote.build.safety.entity.BO.BSafetySafetyEducationBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEducation;
import com.kakarote.build.safety.mapper.BSafetySafetyEducationMapper;
import com.kakarote.build.safety.service.IBSafetySafetyEducationService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 安全教育培训管理 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
@Service
public class BSafetySafetyEducationServiceImpl extends BaseServiceImpl<BSafetySafetyEducationMapper, BSafetySafetyEducation> implements IBSafetySafetyEducationService {


    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BSafetySafetyEducation> selectList(BSafetySafetyEducationBO bSafetySafetyEducationBO)
    {
        BasePage<BSafetySafetyEducation> adminMessageBasePage = getBaseMapper().selectDataList(bSafetySafetyEducationBO.parse(),bSafetySafetyEducationBO);
        return adminMessageBasePage;
    }


    /**
     * 新增数据信息
     *
     */
    @Override
    public int add(BSafetySafetyEducation bSafetySafetyEducation)
    {
        boolean yz = save(bSafetySafetyEducation);
        int row=1;
        return row;
    }

    /**
     * 查询单条数据信息
     *
     */
    @Override
    public BSafetySafetyEducation selectById(String id)
    {
        return getById(id);
    }


    /**
     * 修改单条数据信息
     *
     */
    @Override
    @Transactional
    public int updateDetails(BSafetySafetyEducation bSafetySafetyEducation)
    {
        boolean yz =updateById(bSafetySafetyEducation);
        int row =1;
        return row;
    }


    /**
     * 删除单条数据信息
     *
     */
    @Override
    public int deleteDetails(String id)
    {
        boolean yz = removeById(id);
        int row =1;
        return row;
    }
}
