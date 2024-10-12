package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.PO.BSafetyDangerSource;
import com.kakarote.build.safety.entity.PO.SafetyProdRiskManacont;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 安全生产风险管控 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-04-02
 */
public interface SafetyProdRiskManacontMapper extends BaseMapper<SafetyProdRiskManacont> {

    List<SafetyProdRiskManacont> queryByParentId(@Param("parentId") Integer parentId);

}
