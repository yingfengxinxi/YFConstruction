package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.QualStandardRearingRoomBO;
import com.kakarote.build.qual.entity.PO.QualStandardRearingRoom;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 标养室监测信息 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2023-05-07
 */
public interface QualStandardRearingRoomMapper extends BaseMapper<QualStandardRearingRoom> {

    BasePage<QualStandardRearingRoom> selectByPage(BasePage<Object> parse, @Param("data") QualStandardRearingRoomBO standardRearingRoomBO);
}
