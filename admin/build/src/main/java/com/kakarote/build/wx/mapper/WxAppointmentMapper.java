package com.kakarote.build.wx.mapper;

import com.kakarote.build.wx.entity.BO.WxAppointmentBO;
import com.kakarote.build.wx.entity.PO.WxAppointment;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 业主预约 Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2021-07-15
 */
public interface WxAppointmentMapper extends BaseMapper<WxAppointment> {

    void add(@Param("data")WxAppointment wxAppointment);

    List<WxAppointmentBO> wxSelectByPage(BasePage<AdminMessage> parse, @Param("data") WxAppointmentBO bWxFeedbackBO);

    WxAppointmentBO wxGetById(Integer id);
}
