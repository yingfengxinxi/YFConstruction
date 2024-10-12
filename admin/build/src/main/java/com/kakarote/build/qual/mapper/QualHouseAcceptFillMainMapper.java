package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.QualHouseAcceptFillMainBO;
import com.kakarote.build.qual.entity.PO.QualHouseAcceptFillMain;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 施工质量--分户验收--填报--主表 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-06-29
 */
public interface QualHouseAcceptFillMainMapper extends BaseMapper<QualHouseAcceptFillMain> {

    BasePage<QualHouseAcceptFillMain> selectByPage(BasePage<Object> parse,@Param("data") QualHouseAcceptFillMainBO qualHouseAcceptFillMainBO);

    BasePage<QualHouseAcceptFillMainBO> wxselectByPage(BasePage<Object> parse,@Param("data") QualHouseAcceptFillMainBO qualHouseAcceptFillMainBO);

    QualHouseAcceptFillMainBO wxselectById(Integer id);

    Long wxSave(QualHouseAcceptFillMain map);
}
