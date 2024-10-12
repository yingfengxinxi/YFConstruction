package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.BO.LabourSpeworktypeBO;
import com.kakarote.build.labour.entity.PO.LabourSpeworktype;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 特殊工种--证件管理（分包单位--特殊工种--证件台账） Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-03-19
 */
public interface LabourSpeworktypeMapper extends BaseMapper<LabourSpeworktype> {

    BasePage<LabourSpeworktype> selectList(BasePage<LabourSpeworktype> page, @Param("data") LabourSpeworktypeBO labourSpeworktypeBO);

    LabourSpeworktype selectByInfoId(@Param("infoId") Integer infoId);

    Integer deleteByInfoId(@Param("infoId") Integer infoId);

    BasePage<Map> selectTabsByInfo(BasePage<LabourSpeworktype> page,@Param("data") LabourSpeworktypeBO labourSpeworktypeBO);

    public List<LabourSpeworktype> selectByInfocolId(String InfocolId);

    BasePage<Map> selectTabsByInfoNew(BasePage<Object> parse, @Param("data") LabourSpeworktypeBO labourSpeworktypeBO);
}
