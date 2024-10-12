package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.QualHouseTypeMangeBO;
import com.kakarote.build.qual.entity.PO.QualHouseTypeMange;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工质量--分户验收--户型管理 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-06-24
 */
public interface QualHouseTypeMangeMapper extends BaseMapper<QualHouseTypeMange> {

    BasePage<QualHouseTypeMange> selectByPage(BasePage<Object> parse,@Param("data") QualHouseTypeMangeBO qualHouseTypeMangeBO);

    QualHouseTypeMange getByIdDiy(@Param("id") Integer id);

    List<Map> wxGetByIdDiy(@Param("data") QualHouseTypeMange qualHouseTypeMange);

}
