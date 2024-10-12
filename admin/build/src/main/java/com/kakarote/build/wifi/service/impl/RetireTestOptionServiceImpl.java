package com.kakarote.build.wifi.service.impl;

import com.kakarote.build.wifi.entity.BO.RetireTestOptionBO;
import com.kakarote.build.wifi.entity.PO.RetireTestOption;
import com.kakarote.build.wifi.mapper.RetireTestOptionMapper;
import com.kakarote.build.wifi.service.IRetireTestOptionService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 试卷问题选项 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-05-25
 */
@Service
public class RetireTestOptionServiceImpl extends BaseServiceImpl<RetireTestOptionMapper, RetireTestOption> implements IRetireTestOptionService {

    @Override
    public List<Map<String,Object>> getAllTestOption(Map<String, Object> map2) {
        return getBaseMapper().getAllTestOption(map2);
    }

    @Override
    public int MyUpdateById(RetireTestOptionBO rtob) {
        return getBaseMapper().MyUpdateById(rtob);
    }

    @Override
    public void insertOption(Map<String, Object> tpo) {
        getBaseMapper().insertOption(tpo);
    }
}
