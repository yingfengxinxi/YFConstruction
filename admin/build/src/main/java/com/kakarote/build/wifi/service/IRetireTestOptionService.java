package com.kakarote.build.wifi.service;

import com.kakarote.build.wifi.entity.BO.RetireTestOptionBO;
import com.kakarote.build.wifi.entity.PO.RetireTestOption;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 试卷问题选项 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-05-25
 */
public interface IRetireTestOptionService extends BaseService<RetireTestOption> {

    List<Map<String,Object>> getAllTestOption(Map<String,Object> map2);

    int MyUpdateById(RetireTestOptionBO rtob);

    void insertOption(Map<String,Object> tpo);
}
