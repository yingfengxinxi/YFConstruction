package com.kakarote.build.jg.service.impl;

import com.kakarote.build.jg.entity.BO.BCreditBadBO;
import com.kakarote.build.jg.entity.BO.BCreditGoodBO;
import com.kakarote.build.jg.entity.PO.BCreditBad;
import com.kakarote.build.jg.entity.PO.BCreditGood;
import com.kakarote.build.jg.mapper.BCreditGoodMapper;
import com.kakarote.build.jg.service.IBCreditGoodService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * 信用良好行为 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-11-25
 */
@Service
public class BCreditGoodServiceImpl extends BaseServiceImpl<BCreditGoodMapper, BCreditGood> implements IBCreditGoodService {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BCreditGood> selectList(BCreditGoodBO bCreditGoodBO){
        bCreditGoodBO.setOrdersBd("good_date",false);
        BasePage<BCreditGood> BasePage = getBaseMapper().selectDataList(bCreditGoodBO.parse(),bCreditGoodBO);
        return BasePage;
    }

    public String add(BCreditGood bCreditGood){
        String uuid=UUID.randomUUID().toString().replaceAll("-", "");
        bCreditGood.setId(uuid);
        bCreditGood.setCreateBy(UserUtil.getUser().getRealname());
        bCreditGood.setCreateTime(new Date());
        save(bCreditGood);
        return "1";
    }

    public String edit(BCreditGood bCreditGood){
        bCreditGood.setUpdateBy(UserUtil.getUser().getRealname());
        bCreditGood.setUpdateTime(new Date());
        updateById(bCreditGood);
        return "1";
    }

}
