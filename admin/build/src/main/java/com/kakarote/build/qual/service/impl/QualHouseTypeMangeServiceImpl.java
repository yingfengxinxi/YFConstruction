package com.kakarote.build.qual.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.qual.entity.BO.QualHouseTypeMangeBO;
import com.kakarote.build.qual.entity.PO.QualBuildMange;
import com.kakarote.build.qual.entity.PO.QualHouseName;
import com.kakarote.build.qual.entity.PO.QualHouseTypeMange;
import com.kakarote.build.qual.mapper.QualHouseTypeMangeMapper;
import com.kakarote.build.qual.service.IQualHouseNameService;
import com.kakarote.build.qual.service.IQualHouseTypeMangeService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 施工质量--分户验收--户型管理 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-06-24
 */
@Service
public class QualHouseTypeMangeServiceImpl extends BaseServiceImpl<QualHouseTypeMangeMapper, QualHouseTypeMange> implements IQualHouseTypeMangeService {
    @Autowired
    private IQualHouseNameService IQualHouseNameService;

    @Override
    public BasePage<QualHouseTypeMange> selectByPage(QualHouseTypeMangeBO qualHouseTypeMangeBO) {
        qualHouseTypeMangeBO.setOrdersBd("a.create_time",false);
        return getBaseMapper().selectByPage(qualHouseTypeMangeBO.parse(), qualHouseTypeMangeBO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateDiy(QualHouseTypeMange qualHouseTypeMange, List<QualHouseName> qualHouseName,List<QualHouseName> qualHouseName2) {
        //获取id为空  新增  否则 修改
        boolean flag = false;
        if (qualHouseTypeMange.getId() == null) {
            qualHouseTypeMange.setCreateBy(UserUtil.getUserId());
            qualHouseTypeMange.setCreateTime(new Date());
            if(qualHouseName2.size()>0){
                qualHouseTypeMange.setAcceptTemplateId(qualHouseName2.get(0).getAcceptTemplate());
            }
            flag = save(qualHouseTypeMange);
            if (flag) {
                for (QualHouseName QHN : qualHouseName) {
                    QHN.setHouseTypeId(qualHouseTypeMange.getId());
                    QHN.setCreateBy(UserUtil.getUserId());
                    QHN.setCreateTime(new Date());
                    QHN.setType("1");
                    flag = IQualHouseNameService.save(QHN);
                }
                for (QualHouseName QHN : qualHouseName2) {
                    QHN.setHouseTypeId(qualHouseTypeMange.getId());
                    QHN.setCreateBy(UserUtil.getUserId());
                    QHN.setCreateTime(new Date());
                    QHN.setType("2");
                    flag = IQualHouseNameService.save(QHN);
                }
            }
        } else {
            qualHouseTypeMange.setUpdateBy(UserUtil.getUserId());
            qualHouseTypeMange.setUpdateTime(new Date());
            if(qualHouseName2.size()>0){
                qualHouseTypeMange.setAcceptTemplateId(qualHouseName2.get(0).getAcceptTemplate());
            }
            flag = updateById(qualHouseTypeMange);
            if (flag) {
                IQualHouseNameService.delByHouseTypeId(qualHouseTypeMange.getId());
                for (QualHouseName QHN : qualHouseName) {
                    QHN.setHouseTypeId(qualHouseTypeMange.getId());
                    QHN.setUpdateBy(UserUtil.getUserId());
                    QHN.setUpdateTime(new Date());
                    QHN.setType("1");
                    QHN.setTenantId(null);
                    flag = IQualHouseNameService.save(QHN);
                }
                for (QualHouseName QHN : qualHouseName2) {
                    QHN.setHouseTypeId(qualHouseTypeMange.getId());
                    QHN.setUpdateBy(UserUtil.getUserId());
                    QHN.setUpdateTime(new Date());
                    QHN.setTenantId(null);
                    QHN.setType("2");
                    flag = IQualHouseNameService.save(QHN);
                }
            }
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean CopyHouseType(QualHouseTypeMange qualHouseTypeMange) {
        boolean flag = false;
        JSONArray jsArr = JSONObject.parseArray(qualHouseTypeMange.getHouseTemplateNames());
        for (int i = 0; i < jsArr.size(); i++) {
            JSONObject js = (JSONObject) jsArr.get(i);
            QualHouseTypeMange QTM = new QualHouseTypeMange();
            QTM.setHouseTemplateName(String.valueOf(js.get("houseTemplateName")));
            QTM.setHouseTypeImg(qualHouseTypeMange.getHouseTypeImg());
            QTM.setBatchId(qualHouseTypeMange.getBatchId());
            QTM.setCreateBy(UserUtil.getUserId());
            QTM.setCreateTime(new Date());
            flag = save(QTM);
            if (flag) {
                int id = QTM.getId();
                List<QualHouseName> list = IQualHouseNameService.getByHouseTypeId(qualHouseTypeMange.getId());
                for (QualHouseName QHN : list) {
                    QHN.setId(null);
                    QHN.setHouseTypeId(id);
                    QHN.setCreateBy(UserUtil.getUserId());
                    QHN.setCreateTime(new Date());
                    QHN.setTenantId(null);
                }
               flag= IQualHouseNameService.saveBatch(list);
                //更新验收模板id
                QualHouseTypeMange QTM2 = new QualHouseTypeMange();
                QTM2.setId(QTM.getId());
                List<QualHouseName> list2= list.stream().filter(a->"2".equals(a.getType())).collect(Collectors.toList());
                if(list2.size()>0){
                    QTM2.setAcceptTemplateId(list2.get(0).getAcceptTemplate());
                }
                flag= updateById(QTM2);
            }
        }
        return flag;
    }

    @Override
    public QualHouseTypeMange getByIdDiy(Integer id) {
        return getBaseMapper().getByIdDiy(id);
    }

    @Override
    public List<Map> wxGetByIdDiy(QualHouseTypeMange qualHouseTypeMange) {
        return getBaseMapper().wxGetByIdDiy(qualHouseTypeMange);
    }
}
