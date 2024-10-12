package com.kakarote.build.wx.service.impl;

import com.kakarote.build.wx.entity.BO.WxAppointmentBO;
import com.kakarote.build.wx.entity.PO.WxAppointment;
import com.kakarote.build.wx.mapper.WxAppointmentMapper;
import com.kakarote.build.wx.service.IWxAppointmentService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 业主预约 服务实现类
 * </p>
 *
 * @author shz
 * @since 2021-07-15
 */
@Service
public class WxAppointmentServiceImpl extends BaseServiceImpl<WxAppointmentMapper, WxAppointment> implements IWxAppointmentService {

    @Override
    public void add(WxAppointment wxAppointment) {
        this.getBaseMapper().add(wxAppointment);
    }

    @Override
    public List<WxAppointmentBO> wxSelectByPage(WxAppointmentBO wxAppointment) {
        return this.getBaseMapper().wxSelectByPage(wxAppointment.parse(),wxAppointment);
    }

    @Override
    public WxAppointmentBO wxGetById(Integer id) {
        return this.getBaseMapper().wxGetById(id);
    }
}
