package com.kakarote.build.qual.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.qual.entity.BO.QualAcceptTemplateMainSetBO;
import com.kakarote.build.qual.entity.PO.*;
import com.kakarote.build.qual.mapper.QualAcceptTemplateMainSetMapper;
import com.kakarote.build.qual.service.IQualAcceptTemplateMainSetService;
import com.kakarote.build.qual.service.IQualAcceptTemplateSonService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 施工质量--分户验收--模板设置--主表 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-06-28
 */
@Service
public class QualAcceptTemplateMainSetServiceImpl extends BaseServiceImpl<QualAcceptTemplateMainSetMapper, QualAcceptTemplateMainSet> implements IQualAcceptTemplateMainSetService {
@Autowired
private IQualAcceptTemplateSonService iQualAcceptTemplateSonService;


    @Override
    public BasePage<QualAcceptTemplateMainSet> selectByPage(QualAcceptTemplateMainSetBO qualAcceptTemplateMainSetBO) {
        BasePage<QualAcceptTemplateMainSet> s=getBaseMapper().selectByPage(qualAcceptTemplateMainSetBO.parse(),qualAcceptTemplateMainSetBO);
        return s;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean SaveOrUpdate(Map<String, Object> map) {
        boolean flag=false;
        if(map!=null){
            QualAcceptTemplateMainSet qualAcceptTemplateMainSet= JSON.parseObject(JSON.toJSONString((Map<String, Object>) map.get("templateForm")), QualAcceptTemplateMainSet.class);
            List<Map<String, Object>> templateList = (List<Map<String, Object>>) map.get("templateList");
            Integer acceptTemplateId=null;
            if(qualAcceptTemplateMainSet.getId()!=null){//修改
                qualAcceptTemplateMainSet.setUpdateBy(UserUtil.getUserId());
                qualAcceptTemplateMainSet.setUpdateTime(new Date());
                flag=updateById(qualAcceptTemplateMainSet);
                acceptTemplateId=qualAcceptTemplateMainSet.getId();
            }else{//新增
                qualAcceptTemplateMainSet.setCreateBy(UserUtil.getUserId());
                qualAcceptTemplateMainSet.setCreateTime(new Date());
                flag=save(qualAcceptTemplateMainSet);
                acceptTemplateId=qualAcceptTemplateMainSet.getId();
            }
            if(flag){
                // 先删除在更新
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("accept_template_id", acceptTemplateId);
                flag=iQualAcceptTemplateSonService.remove(queryWrapper);
                //插入模板内容
                for(Map<String, Object> MSO:templateList){
                    QualAcceptTemplateSon QualAcceptTemplateSon = JSON.parseObject(JSON.toJSONString(MSO), QualAcceptTemplateSon.class);
                    QualAcceptTemplateSon.setAcceptTemplateId(acceptTemplateId);
                    QualAcceptTemplateSon.setCreateBy(UserUtil.getUserId());
                    QualAcceptTemplateSon.setTenantId(null);
                    QualAcceptTemplateSon.setCreateTime(new Date());
                    QualAcceptTemplateSon.setRemarks(String.valueOf(MSO.get("remarks")));
                    iQualAcceptTemplateSonService.save(QualAcceptTemplateSon);
                }
            }
        }else{
            throw  new CrmException(1001,"必填参数不存在");
        }
        return flag;
    }

    @Override
    public Map<String, Object> selectById(Integer id) {
        QualAcceptTemplateMainSet qualAcceptTemplateMainSet = getById(id);
//        List<QualAcceptTemplateSon> List = iQualAcceptTemplateSonService
//                .lambdaQuery().eq(QualAcceptTemplateSon::getAcceptTemplateId, id)
//                .list();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("accept_Template_Id",id);
        queryWrapper.orderByAsc("sort");
        List<QualAcceptTemplateSon> List =  iQualAcceptTemplateSonService.list(queryWrapper);

        Map<String, Object> map = new HashMap<>();
        map.put("templateForm", qualAcceptTemplateMainSet);
        map.put("templateList", List);
        return map;
    }

    @Override
    public boolean deleteById(Integer id) {
       boolean flag= removeById(id);
       if(flag){
           QueryWrapper queryWrapper = new QueryWrapper();
           queryWrapper.eq("accept_template_id", id);
           flag=iQualAcceptTemplateSonService.remove(queryWrapper);
       }
        return flag;
    }
}
