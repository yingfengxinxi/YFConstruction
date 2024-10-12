package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.BO.LabourSubContractorBO;
import com.kakarote.build.labour.entity.PO.LabourSubcontractor;
import com.kakarote.build.labour.entity.PO.LabourWorktype;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 劳务管理--分包商 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-03-15
 */
public interface LabourSubcontractorGrpMapper extends BaseMapper<LabourSubcontractor> {

    /**
     * 获取分包商数据列表
     */
    BasePage<Map<String, Object>> selectList(BasePage<LabourSubcontractor> page, @Param("data") LabourSubContractorBO subContractorBO);

    List<Map<String, Object>> seee();

    List<LabourSubcontractor> selcetIdsAndName();

    List<LabourSubcontractor> subcontractorGet(@Param("data")Map<String, Object> parpam);
    LabourSubcontractor selectById2(@Param("id")Integer id,@Param("tenantId")Integer tenantId);
    int deleteDetails(@Param("id")Integer id,@Param("tenantId")Integer tenantId);
    int removeByIdNew(Integer id);
    public List<LabourSubcontractor> selcetCorpCodeData(@Param("data")Map<String, Object> param);
}
