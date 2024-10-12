package com.kakarote.build.safety.service.impl;

import com.kakarote.build.safety.entity.BO.BSafetySafetyDataBO;
import com.kakarote.build.safety.entity.BO.BSafetySafetyEducationBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyData;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEducation;
import com.kakarote.build.safety.mapper.BSafetySafetyDataMapper;
import com.kakarote.build.safety.service.IBSafetySafetyDataService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 安全资料管理 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
@Service
public class BSafetySafetyDataServiceImpl extends BaseServiceImpl<BSafetySafetyDataMapper, BSafetySafetyData> implements IBSafetySafetyDataService {

    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BSafetySafetyData> selectList(BSafetySafetyDataBO bSafetySafetyDataBO)
    {
        BasePage<BSafetySafetyData> adminMessageBasePage = getBaseMapper().selectDataList(bSafetySafetyDataBO.parse(),bSafetySafetyDataBO);
        return adminMessageBasePage;
    }


    /**
     * 新增数据信息
     *
     */
    @Override
    public int add(BSafetySafetyData bSafetySafetyData)
    {
        boolean yz = save(bSafetySafetyData);
        int row=1;
        return row;
    }

    /**
     * 查询单条数据信息
     *
     */
    @Override
    public BSafetySafetyData selectById(String id)
    {
        return getById(id);
    }


    /**
     * 修改单条数据信息
     *
     */
    @Override
    @Transactional
    public int updateDetails(BSafetySafetyData bSafetySafetyData)
    {
        boolean yz =updateById(bSafetySafetyData);
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
