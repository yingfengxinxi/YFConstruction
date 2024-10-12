package com.kakarote.build.qual.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.qual.entity.BO.QualCheckPersonnelBO;
import com.kakarote.build.qual.entity.PO.QualCheckPersonnel;
import com.kakarote.build.qual.mapper.QualCheckPersonnelMapper;
import com.kakarote.build.qual.service.IQualCheckPersonnelService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 验收人员管理 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-07-08
 */
@Service
public class QualCheckPersonnelServiceImpl extends BaseServiceImpl<QualCheckPersonnelMapper, QualCheckPersonnel> implements IQualCheckPersonnelService {

    @Override
    public BasePage<QualCheckPersonnel> selectByPage(QualCheckPersonnelBO qualCheckPersonnelBO) {
        qualCheckPersonnelBO.setOrdersBd("A.create_time",false);
        return getBaseMapper().selectByPage(qualCheckPersonnelBO.parse(),qualCheckPersonnelBO);
    }

    @Override
    public List<Map> selectMap() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("id","person_name").orderByDesc("create_time");
        List<Map> list = listMaps(queryWrapper);

        return list;
    }

    @Override
    public List<QualCheckPersonnel> selectWxList(QualCheckPersonnelBO qualCheckPersonnelBO) {
        return getBaseMapper().selectWxList(qualCheckPersonnelBO);
    }
}
