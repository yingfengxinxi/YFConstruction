package com.kakarote.build.project.service;

import com.kakarote.build.project.entity.PO.ProgressModelsetDetails;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 进度管理--模板设置--明细表 服务类
 * </p>
 *
 * @author zy
 * @since 2021-04-28
 */
public interface IProgressModelsetDetailsService extends BaseService<ProgressModelsetDetails> {
    /**
     * 根据模板id获取列表信息
     * @return
     */
    public List<ProgressModelsetDetails> queryByPidAndMid(Map<String,Object> map);

    /**
     * 计划生成 使用
     * @param map
     * @return
     */
    public List<Map> queryMapByPidAndMid(Map<String,Object> map);


    public List<Map> selectTree(Integer mid);

    public Boolean delById(String id);

    public List<Map> appSelectTree(Integer mid);
}
