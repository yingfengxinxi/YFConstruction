package com.kakarote.build.wx.service;

import com.kakarote.build.wx.entity.BO.WxAppointmentBO;
import com.kakarote.build.wx.entity.PO.WxAppointment;
import com.kakarote.core.servlet.BaseService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 业主预约 服务类
 * </p>
 *
 * @author shz
 * @since 2021-07-15
 */
public interface IWxAppointmentService extends BaseService<WxAppointment> {

    void add(@Param("data")WxAppointment wxAppointment);

    List<WxAppointmentBO> wxSelectByPage(WxAppointmentBO wxAppointment);

    WxAppointmentBO wxGetById(Integer id);

}
