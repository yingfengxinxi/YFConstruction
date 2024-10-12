package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.BO.QualMonitSTbZhbBO;
import com.kakarote.build.qual.entity.BO.QualMonitsetBO;
import com.kakarote.build.qual.entity.PO.QualMonitJtbZb;
import com.kakarote.build.qual.entity.PO.QualMonitJtbZhb;
import com.kakarote.build.qual.entity.PO.QualMonitSTbZb;
import com.kakarote.build.qual.mapper.QualMonitJtbZhbMapper;
import com.kakarote.build.qual.service.IQualMonitJtbZbService;
import com.kakarote.build.qual.service.IQualMonitJtbZhbService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 检测检验--监测数据填报--主表 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-03-25
 */
@Service
public class QualMonitJtbZhbServiceImpl extends BaseServiceImpl<QualMonitJtbZhbMapper, QualMonitJtbZhb> implements IQualMonitJtbZhbService {

    @Autowired
    private IQualMonitJtbZbService iQualMonitJtbZbService;

    @Override
    public BasePage<QualMonitJtbZhb> selectList(QualMonitSTbZhbBO qualMonitSTbZhbBO) {
        qualMonitSTbZhbBO.setOrdersBd("a.create_Time",false);
        return getBaseMapper().selectList(qualMonitSTbZhbBO.parse(),qualMonitSTbZhbBO);
    }

    @Override
    public Boolean saveOrUpdateTB(QualMonitJtbZhb qualMonitSTbZhb, List<QualMonitsetBO> lists) {
        boolean b = false;
        if(qualMonitSTbZhb.getId() == null){//新增 undefined
            qualMonitSTbZhb.setCreateTime(new Date());
            b = save(qualMonitSTbZhb);

            if(b){
                for(QualMonitsetBO qualMonitset : lists){
                    QualMonitJtbZb qualMonitSTbZb = new QualMonitJtbZb();

                    qualMonitSTbZb.setSid(qualMonitset.getId());
                    qualMonitSTbZb.setSname(qualMonitset.getName());
                    qualMonitSTbZb.setSvalue(qualMonitset.getSValue());
                    qualMonitSTbZb.setNote(qualMonitset.getNote());

                    qualMonitSTbZb.setPid(qualMonitSTbZhb.getId());

                    b = iQualMonitJtbZbService.save(qualMonitSTbZb);
                }

            }

        }else {//修改

            b = updateById(qualMonitSTbZhb);

            if(b){
                for(QualMonitsetBO qualMonitset : lists){
                    QualMonitJtbZb qualMonitSTbZb = new QualMonitJtbZb();

                    qualMonitSTbZb.setSvalue(qualMonitset.getSValue());
                    qualMonitSTbZb.setNote(qualMonitset.getNote());
                    qualMonitSTbZb.setId(qualMonitset.getId());

                    b = iQualMonitJtbZbService.updateById(qualMonitSTbZb);

                }

            }

        }

        return b;
    }
}
