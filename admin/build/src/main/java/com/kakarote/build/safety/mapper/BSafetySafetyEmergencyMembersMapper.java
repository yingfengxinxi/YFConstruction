package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.BSafetySafetyEmergencyBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergency;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergencyMembers;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 应急班组成员管理 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
public interface BSafetySafetyEmergencyMembersMapper extends BaseMapper<BSafetySafetyEmergencyMembers> {

    List<BSafetySafetyEmergencyMembers>  selectDataList(String pid);

}