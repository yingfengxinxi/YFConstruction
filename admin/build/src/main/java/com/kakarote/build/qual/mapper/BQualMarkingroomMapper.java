package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.BQualMarkingroomBO;
import com.kakarote.build.qual.entity.PO.BQualMarkingroom;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 标养室基本信息 Mapper 接口
 * </p>
 *
 * @author zhang
 * @since 2022-07-14
 */
public interface BQualMarkingroomMapper extends BaseMapper<BQualMarkingroom> {

    BasePage<BQualMarkingroom> selectDataList(BasePage<AdminMessage> parse, @Param("data") BQualMarkingroomBO BQualMarkingroomBO);
    List<BQualMarkingroom> listAll();
}
