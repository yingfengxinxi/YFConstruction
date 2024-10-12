package com.kakarote.build.supervision.service.impl;

import com.kakarote.build.eval.entity.PO.EvalApply;
import com.kakarote.build.supervision.entity.BO.SupervisionEnterpriseBO;
import com.kakarote.build.supervision.entity.PO.SupervisionEnterprise;
import com.kakarote.build.supervision.mapper.SupervisionEnterpriseMapper;
import com.kakarote.build.supervision.service.ISupervisionEnterpriseService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzy
 * @since 2022-11-29
 */
@Service
public class SupervisionEnterpriseServiceImpl extends BaseServiceImpl<SupervisionEnterpriseMapper, SupervisionEnterprise> implements ISupervisionEnterpriseService {

    @Override
    public BasePage<Map<String,Object>> selectByPage(SupervisionEnterpriseBO supervisionEnterpriseBO) {
        BasePage<Map<String,Object>> list = this.getBaseMapper().selectByPage(supervisionEnterpriseBO.parse(), supervisionEnterpriseBO);
        return list;
    }

    @Override
    public Map<String, Object> selectDataById(String id) {
        Map<String,Object> data = this.getBaseMapper().selectDataById(id);
        return data;
    }

    @Override
    public void insertOrupdateData(SupervisionEnterprise supervisionEnterprise) {

        // 状态为0则插入数据 为1更新数据
        if ("0".equals(supervisionEnterprise.getState())) {
            // 状态由未分配，改为已分配
            supervisionEnterprise.setState("1");
            supervisionEnterprise.setAllocateTime(new Date());
            this.save(supervisionEnterprise);
        }

        // 更新数据
        supervisionEnterprise.setUpdateBy(UserUtil.getUserId());
        this.updateById(supervisionEnterprise);
    }

    @Override
    public List<Map<String, Object>> selectSupeList() {
        List<Map<String, Object>> list = this.getBaseMapper().selectSupeList();
        return list;
    }

    @Override
    public List<SupervisionEnterprise> selectByProjectId(SupervisionEnterprise supervisionEnterprise){
        List<SupervisionEnterprise> list = this.getBaseMapper().selectByProjectId(supervisionEnterprise);
        return list;
    }
}
