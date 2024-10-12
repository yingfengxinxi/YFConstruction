package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.BQualMarkingroomPoliceBO;
import com.kakarote.build.qual.entity.PO.BQualMarkingroomPolice;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 标养室报警信息 Mapper 接口
 * </p>
 *
 * @author zhang
 * @since 2022-07-14
 */
public interface BQualMarkingroomPoliceMapper extends BaseMapper<BQualMarkingroomPolice> {

    BasePage<BQualMarkingroomPolice> selectDataList(BasePage<AdminMessage> parse, @Param("data") BQualMarkingroomPoliceBO bQualMarkingroomPoliceBO);
}
