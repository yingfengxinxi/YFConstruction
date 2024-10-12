package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.PO.QualMaterialCatalog;
import com.kakarote.build.qual.mapper.QualMaterialCatalogMapper;
import com.kakarote.build.qual.service.IQualMaterialCatalogService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 质量资料目录 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-04-13
 */
@Service
public class QualMaterialCatalogServiceImpl extends BaseServiceImpl<QualMaterialCatalogMapper, QualMaterialCatalog> implements IQualMaterialCatalogService {

    @Override
    public List<Map> selectTree(Integer id) {
        List<QualMaterialCatalog> listAll = list();
        return recursion(id,listAll);
    }

    /**
     * @Description:树形数据递归查询
     * @author shz
     * @date 2021-4-8 10:05
     * @param
     * @return
     */
    public List<Map> recursion(Integer id,List<QualMaterialCatalog> listAll){
        List<Map> all = new ArrayList<>();
        List<QualMaterialCatalog> list = listAll.stream().filter(down -> down.getParentId().equals(id)).collect(Collectors.toList());
        if(list.size()<=0){
            return null;
        }
        for(QualMaterialCatalog catalog :list){
            Map obj = new HashMap();
            obj.put("id",catalog.getId());
            obj.put("label",catalog.getName());
            List children = recursion(catalog.getId(),listAll);
            if(children != null){
                obj.put("children",children);
            }
            all.add(obj);
        }
        return  all;
    }

    public List<Integer> recursionIds(Integer id,List<QualMaterialCatalog> listAll){
        List<Integer> all = new ArrayList<>();
        List<QualMaterialCatalog> list = listAll.stream().filter(down -> down.getParentId().equals(id)).collect(Collectors.toList());
        List<Integer> lons = new ArrayList<>();
        if(list.size()<=0){
            return null;
        }else {
            lons = list.stream().map(QualMaterialCatalog::getId).collect(Collectors.toList());
            all.addAll(lons);
        }
        for(QualMaterialCatalog catalog :list){
            List<Integer> children = recursionIds(catalog.getId(),listAll);
            if(children != null){
                all.addAll(children);
            }
        }
        return  all;
    }
}
