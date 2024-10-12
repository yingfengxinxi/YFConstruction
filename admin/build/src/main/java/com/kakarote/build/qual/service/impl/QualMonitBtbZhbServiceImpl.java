package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.BO.QualMonitSTbZhbBO;
import com.kakarote.build.qual.entity.BO.QualMonitsetBO;
import com.kakarote.build.qual.entity.PO.QualMonitBtbZb;
import com.kakarote.build.qual.entity.PO.QualMonitBtbZhb;
import com.kakarote.build.qual.entity.PO.QualMonitJtbZb;
import com.kakarote.build.qual.mapper.QualMonitBtbZhbMapper;
import com.kakarote.build.qual.service.IQualMonitBtbZbService;
import com.kakarote.build.qual.service.IQualMonitBtbZhbService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 标养室监测--监测数据填报--主表 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-03-25
 */
@Service
public class QualMonitBtbZhbServiceImpl extends BaseServiceImpl<QualMonitBtbZhbMapper, QualMonitBtbZhb> implements IQualMonitBtbZhbService {

    @Autowired
    private IQualMonitBtbZbService iQualMonitBtbZbService;

    @Override
    public BasePage<QualMonitBtbZhb> selectList(QualMonitSTbZhbBO qualMonitSTbZhbBO) {
        qualMonitSTbZhbBO.setOrdersBd("a.create_Time",false);
        return getBaseMapper().selectList(qualMonitSTbZhbBO.parse(),qualMonitSTbZhbBO);
    }

    @Override
    public Boolean saveOrUpdateTB(QualMonitBtbZhb qualMonitSTbZhb, List<QualMonitsetBO> lists) {
        boolean b = false;
        if(qualMonitSTbZhb.getId() == null){//新增 undefined
            b = save(qualMonitSTbZhb);

            if(b){
                for(QualMonitsetBO qualMonitset : lists){
                    QualMonitBtbZb qualMonitSTbZb = new QualMonitBtbZb();

                    qualMonitSTbZb.setSid(qualMonitset.getId());
                    qualMonitSTbZb.setSname(qualMonitset.getName());
                    qualMonitSTbZb.setSvalue(qualMonitset.getSValue());
                    qualMonitSTbZb.setNote(qualMonitset.getNote());

                    qualMonitSTbZb.setPid(qualMonitSTbZhb.getId());

                    b = iQualMonitBtbZbService.save(qualMonitSTbZb);
                }

            }

        }else {//修改
            b = updateById(qualMonitSTbZhb);

            if(b){
                for(QualMonitsetBO qualMonitset : lists){
                    QualMonitBtbZb qualMonitSTbZb = new QualMonitBtbZb();

                    qualMonitSTbZb.setSvalue(qualMonitset.getSValue());
                    qualMonitSTbZb.setNote(qualMonitset.getNote());
                    qualMonitSTbZb.setId(qualMonitset.getId());

                    b = iQualMonitBtbZbService.updateById(qualMonitSTbZb);

                }

            }

        }

        return b;
    }
}
