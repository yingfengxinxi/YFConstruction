package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.BO.LabourWorktypeBO;
import com.kakarote.build.labour.entity.PO.LabourWorktype;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 劳务管理--工种 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-03-15
 */
public interface LabourWorktypeMapper extends BaseMapper<LabourWorktype> {

    BasePage<LabourWorktype> selectList(BasePage<LabourWorktype> page, @Param("data") LabourWorktypeBO LabourWorktypeBO);

    List<LabourWorktype> selcetIdsAndWorktype();
}
