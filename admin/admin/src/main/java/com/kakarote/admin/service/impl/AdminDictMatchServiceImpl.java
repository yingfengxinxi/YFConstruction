package com.kakarote.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.kakarote.admin.common.AdminDictUtils;
import com.kakarote.admin.entity.BO.AdminDictMathBO;
import com.kakarote.admin.entity.PO.AdminDictMatch;
import com.kakarote.admin.mapper.AdminDictMatchMapper;
import com.kakarote.admin.service.IAdminDictMatchService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-11-30
 */
@Service
public class AdminDictMatchServiceImpl extends BaseServiceImpl<AdminDictMatchMapper, AdminDictMatch> implements IAdminDictMatchService {


    @PostConstruct
    public void init() {
        List<AdminDictMatch> list = getBaseMapper().selectAll();
        for(AdminDictMatch adminDictMatch :list){
            AdminDictUtils.setDictMatchCache(adminDictMatch.getTenantId(),adminDictMatch.getMatchDict(),adminDictMatch.getSelfDictValue(), JSON.toJSONString(adminDictMatch));
        }
    }

        @Override
    public BasePage<AdminDictMatch> selectByPage(AdminDictMathBO adminDictMathBO) {
        BasePage<AdminDictMatch> list =getBaseMapper().selectByPage(adminDictMathBO.parse(), adminDictMathBO);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectSuggestList(Map<String, Object> params) {
        List<Map<String, Object>> list = getBaseMapper().selectSuggestList(params);
        return list;
    }
}
