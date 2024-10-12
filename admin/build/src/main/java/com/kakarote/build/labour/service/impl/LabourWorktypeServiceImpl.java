package com.kakarote.build.labour.service.impl;

import com.kakarote.build.labour.entity.BO.LabourWorktypeBO;
import com.kakarote.build.labour.entity.PO.LabourWorktype;
import com.kakarote.build.labour.mapper.LabourWorktypeMapper;
import com.kakarote.build.labour.service.ILabourWorktypeService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 劳务管理--工种 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-03-15
 */
@Service
public class LabourWorktypeServiceImpl extends BaseServiceImpl<LabourWorktypeMapper, LabourWorktype> implements ILabourWorktypeService {

    /**
     * 根据条件查询
     * @param labourWorktypeBO
     * @return
     */
    @Override
    public BasePage<LabourWorktype> selectList(LabourWorktypeBO labourWorktypeBO) {
        BasePage<LabourWorktype> labourWorktypeBasePage = getBaseMapper().selectList(labourWorktypeBO.parse(), labourWorktypeBO);
        return labourWorktypeBasePage;
    }

    @Override
    public List<LabourWorktype> selcetIdsAndWorktype() {
        List<LabourWorktype> labourWorktypes = getBaseMapper().selcetIdsAndWorktype();
        return labourWorktypes;
    }


}
