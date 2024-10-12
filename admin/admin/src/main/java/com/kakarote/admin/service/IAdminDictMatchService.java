package com.kakarote.admin.service;

import com.kakarote.admin.entity.BO.AdminDictMathBO;
import com.kakarote.admin.entity.PO.AdminDictMatch;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzy
 * @since 2021-11-30
 */
public interface IAdminDictMatchService extends BaseService<AdminDictMatch> {

    BasePage<AdminDictMatch> selectByPage(AdminDictMathBO adminDictMathBO);

    List<Map<String, Object>> selectSuggestList(Map<String, Object> params);
}
