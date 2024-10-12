package com.kakarote.build.jg.service.impl;

import com.kakarote.build.jg.entity.BO.BCreditBadBO;
import com.kakarote.build.jg.entity.BO.BJgSiteRubbishBO;
import com.kakarote.build.jg.entity.PO.BCreditBad;
import com.kakarote.build.jg.entity.PO.BJgSiteRubbish;
import com.kakarote.build.jg.mapper.BJgSiteRubbishMapper;
import com.kakarote.build.jg.service.IBJgSiteRubbishService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工地垃圾分类管理 服务实现类
 * </p>
 *
 * @author author
 * @since 2022-02-09
 */
@Service
public class BJgSiteRubbishServiceImpl extends BaseServiceImpl<BJgSiteRubbishMapper, BJgSiteRubbish> implements IBJgSiteRubbishService {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BJgSiteRubbish> selectList(BJgSiteRubbishBO bJgSiteRubbishBO){
        bJgSiteRubbishBO.setOrdersBd("dealwith_time", false);
        BasePage<BJgSiteRubbish> BasePage = getBaseMapper().selectDataList(bJgSiteRubbishBO.parse(),bJgSiteRubbishBO);
        return BasePage;
    }
}
