package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.LabourSubContractorBO;
import com.kakarote.build.labour.entity.PO.LabourSubcontractor;
import com.kakarote.build.labour.entity.PO.LabourWorktype;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 劳务管理--分包商 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-03-15
 */
public interface ILabourSubcontractorService extends BaseService<LabourSubcontractor> {

    /**
     * 获取分包商数据列表
     */
    public BasePage<Map<String, Object>> selectList(LabourSubContractorBO subContractorBO);

    public List<LabourSubcontractor> selcetIdsAndName();

    boolean removeByIdNew(Integer id);

    public List<LabourSubcontractor> selcetCorpCodeData(Map<String, Object> param);
}
