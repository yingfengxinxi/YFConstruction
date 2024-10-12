package com.kakarote.build.qual.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.qual.entity.BO.QualHouseAcceptFillMainBO;
import com.kakarote.build.qual.entity.PO.QualAcceptTemplateMainSet;
import com.kakarote.build.qual.entity.PO.QualAcceptTemplateSon;
import com.kakarote.build.qual.entity.PO.QualHouseAcceptFillMain;
import com.kakarote.build.qual.entity.PO.QualHouseAcceptFillSon;
import com.kakarote.build.qual.mapper.QualHouseAcceptFillMainMapper;
import com.kakarote.build.qual.mapper.QualHouseAcceptFillSonMapper;
import com.kakarote.build.qual.service.IQualHouseAcceptFillMainService;
import com.kakarote.build.qual.service.IQualHouseAcceptFillSonService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 施工质量--分户验收--填报--主表 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-06-29
 */
@Service
public class QualHouseAcceptFillMainServiceImpl extends BaseServiceImpl<QualHouseAcceptFillMainMapper, QualHouseAcceptFillMain> implements IQualHouseAcceptFillMainService {
    @Autowired
    private IQualHouseAcceptFillSonService iQualHouseAcceptFillSonService;
    @Autowired
    private QualHouseAcceptFillSonMapper qualHouseAcceptFillSonMapper;
    @Override
    public BasePage<QualHouseAcceptFillMain> selectByPage(QualHouseAcceptFillMainBO qualHouseAcceptFillMainBO) {
        qualHouseAcceptFillMainBO.setOrdersBd("a.create_time",false);
        return getBaseMapper().selectByPage(qualHouseAcceptFillMainBO.parse(),qualHouseAcceptFillMainBO);
    }

    @Override
    public BasePage<QualHouseAcceptFillMainBO> wxselectByPage(QualHouseAcceptFillMainBO qualHouseAcceptFillMainBO) {
        return getBaseMapper().wxselectByPage(qualHouseAcceptFillMainBO.parse(),qualHouseAcceptFillMainBO);
    }

    @Override
    public  Map<String, Object> selectById(Integer id) {
//        QualHouseAcceptFillMain qualHouseAcceptFillMain = getById(id);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("*").eq("accept_Id",id);
        Map map1 = getMap(queryWrapper);
        String acceptBy = String.valueOf(map1.get("acceptBy"));
        if(acceptBy != null && !acceptBy.equals("null") && !acceptBy.equals("") ){
            List<Integer> list = new ArrayList<>();
            String[] split = acceptBy.split(",");
            for(String s :split){
                Integer integer = Integer.valueOf(s);
                list.add(integer);
            }
            map1.put("acceptBy",list);
        }

        List<QualHouseAcceptFillSon> List = iQualHouseAcceptFillSonService.lambdaQuery().eq(QualHouseAcceptFillSon::getPid, id).list();
        Map<String, Object> map = new HashMap<>();
        map.put("templateForm", map1);
        map.put("templateList", List);
        return map;
    }

    @Override
    public QualHouseAcceptFillMainBO wxselectById(Integer id) {
        return getBaseMapper().wxselectById(id);
    }

    @Override
    public boolean SaveOrUpdate(Map<String, Object> map) {
        boolean flag=false;
        if(map!=null){
            QualHouseAcceptFillMain qualHouseAcceptFillMain= JSON.parseObject(JSON.toJSONString((Map<String, Object>) map.get("templateForm")), QualHouseAcceptFillMain.class);
            List<Map<String, Object>> templateList = (List<Map<String, Object>>) map.get("templateList");
            Integer AcceptId=null;
            if(qualHouseAcceptFillMain.getAcceptId()!=null){//修改
                qualHouseAcceptFillMain.setUpdateBy(UserUtil.getUserId());
                qualHouseAcceptFillMain.setUpdateTime(new Date());
                flag=updateById(qualHouseAcceptFillMain);
                AcceptId=qualHouseAcceptFillMain.getAcceptId();
            }else{//新增
                qualHouseAcceptFillMain.setCreateBy(UserUtil.getUserId());
                qualHouseAcceptFillMain.setCreateTime(new Date());
                flag=save(qualHouseAcceptFillMain);
                AcceptId=qualHouseAcceptFillMain.getAcceptId();
            }
            if(flag){
                // 先删除在更新
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("pid", AcceptId);
                flag=iQualHouseAcceptFillSonService.remove(queryWrapper);
                //插入模板内容
                for(Map<String, Object> MSO:templateList){
                    QualHouseAcceptFillSon qualHouseAcceptFillSon = JSON.parseObject(JSON.toJSONString(MSO), QualHouseAcceptFillSon.class);
                    qualHouseAcceptFillSon.setPid(AcceptId);
                    qualHouseAcceptFillSon.setCreateBy(UserUtil.getUserId());
                    qualHouseAcceptFillSon.setTenantId(null);
                    qualHouseAcceptFillSon.setCreateTime(new Date());
                    flag= iQualHouseAcceptFillSonService.save(qualHouseAcceptFillSon);
                }
            }
        }else{
            throw  new CrmException(1001,"必填参数不存在");
        }
        return flag;
    }

    @Override
    public boolean wxSaveOrUpdate(Map<String, Object> map) {
        boolean flag=false;
        if(map!=null){
            QualHouseAcceptFillMain qualHouseAcceptFillMain= JSON.parseObject(JSON.toJSONString(map), QualHouseAcceptFillMain.class);
            qualHouseAcceptFillMain.setCreateTime(new Date());
            Long i =getBaseMapper().wxSave(qualHouseAcceptFillMain);
            List<Map> list = (List<Map>)map.get("list");
            for(Map son: list){
                son.put("pid",qualHouseAcceptFillMain.getAcceptId());
                son.put("tenantId",qualHouseAcceptFillMain.getTenantId());
                son.put("projectId",qualHouseAcceptFillMain.getProjectId());
                qualHouseAcceptFillSonMapper.wxSave(son);
            }
        }else{
            throw  new CrmException(1001,"必填参数不存在");
        }
        return flag;
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean flag= removeById(id);
        if(flag){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("pid", id);
            flag=iQualHouseAcceptFillSonService.remove(queryWrapper);
        }
        return flag;
    }
}
