package com.kakarote.build.labour.apipm.service;



import com.kakarote.build.labour.entity.PO.LabourSubcontractor;
import com.kakarote.core.common.Result;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;


/**
 * <p>
 * 分包商数据接取外放接口
 * </p>
 *
 * @author author
 * @since 2022-02-18
 */
public interface SubcontractorDataService extends BaseService<LabourSubcontractor> {

    public Result subcontractorAdd(Map<String, Object> parpam);

    public Result subcontractorEdit(Map<String, Object> parpam);

    public Result subcontractorDelete(Map<String, Object> parpam);

    public Result subcontractorGet(Map<String, Object> parpam);
}
