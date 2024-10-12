package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.QualMaterialBO;
import com.kakarote.build.qual.entity.PO.QualMaterial;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 质量资料 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-04-13
 */
public interface QualMaterialMapper extends BaseMapper<QualMaterial> {

    BasePage<QualMaterial> selectPageList(BasePage<AdminMessage> parse, @Param("data")QualMaterialBO qualMaterialBO);

    int collect(@Param("id")Integer id, @Param("userId")Long userId);

    List<Integer> selectCollect(Long userId);

    int deleteCollect(@Param("materialId")Integer materialId, @Param("userId")Long userId);
}
