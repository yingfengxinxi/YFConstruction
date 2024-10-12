package com.kakarote.build.safety.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.safety.entity.PO.SafetyMaterial;
import com.kakarote.build.safety.entity.PO.SafetyMaterialCatalog;
import com.kakarote.build.safety.mapper.SafetyMaterialCatalogMapper;
import com.kakarote.build.safety.service.ISafetyMaterialCatalogService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 安全资料目录 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-04-07
 */
@Service
public class SafetyMaterialCatalogServiceImpl extends BaseServiceImpl<SafetyMaterialCatalogMapper, SafetyMaterialCatalog> implements ISafetyMaterialCatalogService {

    @Autowired
    private ISafetyMaterialService iSafetyMaterialService;

    @Override
    public List<Map> selectTree(Integer id) {
        List<SafetyMaterialCatalog> listAll = list();
        return recursion(id,listAll);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteOneselfAndSublevel(Integer id) {
        LambdaQueryWrapper<SafetyMaterialCatalog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SafetyMaterialCatalog::getParentId, id);
        List<SafetyMaterialCatalog> list = this.getBaseMapper().selectList(queryWrapper);
        if (list != null && list.size() > 0) {
            for (SafetyMaterialCatalog catalog:list) {
                this.deleteOneselfAndSublevel(catalog.getId());
            }
        }


        this.getBaseMapper().deleteById(id);
        QueryWrapper<SafetyMaterial> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("catalog_id",id);
        iSafetyMaterialService.remove(queryWrapper2);
    }

    /**
    * @Description:树形数据递归查询
    * @author shz
    * @date 2021-4-8 10:05
    * @param
    * @return
    */
    public List<Map> recursion(Integer id,List<SafetyMaterialCatalog> listAll){
        List<Map> all = new ArrayList<>();
        List<SafetyMaterialCatalog> list = listAll.stream().filter(down -> down.getParentId().equals(id)).collect(Collectors.toList());
        if(list.size()<=0){
            return null;
        }
        for(SafetyMaterialCatalog catalog :list){
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

    public List<Integer> recursionIds(Integer id,List<SafetyMaterialCatalog> listAll){
        List<Integer> all = new ArrayList<>();
        List<SafetyMaterialCatalog> list = listAll.stream().filter(down -> down.getParentId().equals(id)).collect(Collectors.toList());
        List<Integer> lons = new ArrayList<>();
        if(list.size()<=0){
            return null;
        }else {
            lons = list.stream().map(SafetyMaterialCatalog::getId).collect(Collectors.toList());
            all.addAll(lons);
        }
        for(SafetyMaterialCatalog catalog :list){
            List<Integer> children = recursionIds(catalog.getId(),listAll);
            if(children != null){
                all.addAll(children);
            }
        }
        return  all;
    }
}
