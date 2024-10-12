package com.kakarote.build.jg.service.impl;

import com.kakarote.build.jg.entity.BO.BCreditBadBO;
import com.kakarote.build.jg.entity.PO.BCreditBad;
import com.kakarote.build.jg.mapper.BCreditBadMapper;
import com.kakarote.build.jg.service.IBCreditBadService;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * 信用不良行为 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-11-25
 */
@Service
public class BCreditBadServiceImpl extends BaseServiceImpl<BCreditBadMapper, BCreditBad> implements IBCreditBadService {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BCreditBad> selectList(BCreditBadBO bCreditBadBO){
        bCreditBadBO.setOrdersBd("bad_date",false);
        BasePage<BCreditBad> BasePage = getBaseMapper().selectDataList(bCreditBadBO.parse(),bCreditBadBO);
        return BasePage;
    }

    public String add(BCreditBad bCreditBad){
        String uuid=UUID.randomUUID().toString().replaceAll("-", "");
        bCreditBad.setId(uuid);
        bCreditBad.setCreateBy(UserUtil.getUser().getRealname());
        bCreditBad.setCreateTime(new Date());
        save(bCreditBad);
        return "1";
    }

    public String edit(BCreditBad bCreditBad){
        bCreditBad.setUpdateBy(UserUtil.getUser().getRealname());
        bCreditBad.setUpdateTime(new Date());
        updateById(bCreditBad);
        return "1";
    }


}
