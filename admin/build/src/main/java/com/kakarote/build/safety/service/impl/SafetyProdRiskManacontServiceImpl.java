package com.kakarote.build.safety.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.safety.entity.PO.BSafetyDangerSource;
import com.kakarote.build.safety.entity.PO.SafetyProdRiskManacont;
import com.kakarote.build.safety.mapper.SafetyProdRiskManacontMapper;
import com.kakarote.build.safety.service.ISafetyProdRiskManacontService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 安全生产风险管控 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-04-02
 */
@Service
public class SafetyProdRiskManacontServiceImpl extends BaseServiceImpl<SafetyProdRiskManacontMapper, SafetyProdRiskManacont> implements ISafetyProdRiskManacontService {

    @Override
    public List<SafetyProdRiskManacont> queryByParentId(Integer parentId) {
        return getBaseMapper().queryByParentId(parentId);
    }

    @Override
    public boolean removeListById(Integer id) {
        // 需删除的主键列表
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(id);
        // 递归
        this.getChildId(ids, id);
        // 批量删除集合中的数据
        int i = getBaseMapper().deleteBatchIds(ids);

        return i > 0;
    }

    // 递归查询出所有的子级id
    private void getChildId(ArrayList<Integer> ids, Integer id) {
        // 查询二级分类的对象
        QueryWrapper<SafetyProdRiskManacont> wrapper = new QueryWrapper<>();
        // 查询条件
        wrapper.eq("parent_id", id);
        List<SafetyProdRiskManacont> list = baseMapper.selectList(wrapper);

        for (SafetyProdRiskManacont safetyProdRiskManacont:list) {
            Integer id1 = safetyProdRiskManacont.getId();
            ids.add(id1);
            this.getChildId(ids, id1);
        }
    }
}
