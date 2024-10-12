package com.kakarote.build.qual.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.qual.entity.BO.QualMaterialBO;
import com.kakarote.build.qual.entity.PO.QualMaterial;
import com.kakarote.build.qual.entity.PO.QualMaterialCatalog;
import com.kakarote.build.qual.mapper.QualMaterialMapper;
import com.kakarote.build.qual.service.IQualMaterialService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 质量资料 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-04-13
 */
@Service
public class QualMaterialServiceImpl extends BaseServiceImpl<QualMaterialMapper, QualMaterial> implements IQualMaterialService {

    @Autowired
    private QualMaterialCatalogServiceImpl qualMaterialCatalogService;

    @Override
    public BasePage<QualMaterial> selectPageAllByPid(QualMaterialBO qualMaterialBO) {
        List<QualMaterialCatalog> list = qualMaterialCatalogService.list();
        List<Integer> ids = qualMaterialCatalogService.recursionIds(qualMaterialBO.getCatalogId(),list);
        if(ids == null ){
            ids = new ArrayList<>();
        }
        ids.add(qualMaterialBO.getCatalogId());
        qualMaterialBO.setCatalogIds(ids);
        BasePage<QualMaterial> material = getBaseMapper().selectPageList(qualMaterialBO.parse(),qualMaterialBO);
        Long userId = UserUtil.getUserId();
        List<Integer> materials =  getBaseMapper().selectCollect(userId);
        for(QualMaterial qualMaterial:material.getList()){
            if(materials.contains(qualMaterial.getId())){
                qualMaterial.setCollect("1");
            }
        }
        return material;
    }

    public List<QualMaterial> selectByCollect(Integer catalogId){
        List<QualMaterialCatalog> list = qualMaterialCatalogService.list();
        List<Integer> ids = qualMaterialCatalogService.recursionIds(catalogId,list);
        if(ids == null ){
            ids = new ArrayList<>();
        }
        ids.add(catalogId);
        Long userId = UserUtil.getUserId();
        List<Integer> materials =  getBaseMapper().selectCollect(userId);
        if(materials == null || materials.size() == 0){
            materials = new ArrayList<Integer>();
            materials.add(0);
        }
        QueryWrapper<QualMaterial> wrappers = new QueryWrapper<>();
        List<Integer> finalIds = ids;
        List<Integer> finalMaterials = materials;
        wrappers.and(wrapper ->wrapper.in("catalog_id", finalIds).in("id", finalMaterials));
        return list(wrappers);
    }

    @Override
    public int collect(Integer id, Long userId) {
        return getBaseMapper().collect(id,userId);
    }

    @Override
    public List<Integer> selectCollect(Long userId) {
        return getBaseMapper().selectCollect(userId);
    }

    public int deleteCollect(Integer materialId){
        Long userId = UserUtil.getUserId();
        return getBaseMapper().deleteCollect(materialId,userId);
    }
}
