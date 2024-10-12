package com.kakarote.build.labour.service.impl;

import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceDetailBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceDetail;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceDetailMonth;
import com.kakarote.build.labour.mapper.BLabourAttendanceDetailMapper;
import com.kakarote.build.labour.service.IBLabourAttendanceDetailService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 考勤明细管理 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
@Service
public class BLabourAttendanceDetailServiceImpl extends BaseServiceImpl<BLabourAttendanceDetailMapper, BLabourAttendanceDetail> implements IBLabourAttendanceDetailService {

    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BLabourAttendanceDetail> selectList(BLabourAttendanceDetailBO bLabourAttendanceDetailBO)
    {
        BasePage<BLabourAttendanceDetail> adminMessageBasePage = getBaseMapper().selectDataList(bLabourAttendanceDetailBO.parse(),bLabourAttendanceDetailBO);
        return adminMessageBasePage;
    }

    /**
     * 查询月份数据列表
     *
     */
    public List<BLabourAttendanceDetailMonth> selectMonth(BLabourAttendanceDetailMonth bLabourAttendanceDetailMonth){
        List<BLabourAttendanceDetailMonth> list=getBaseMapper().selectMonth(bLabourAttendanceDetailMonth);
        return list;
    }

    public Long countByattendance(String id) {
        Long count=getBaseMapper().countByattendance(id);
        return count;
    }

    public BLabourAttendanceDetail selectUpdate(BLabourAttendanceDetail bLabourAttendanceDetail){
        BLabourAttendanceDetail data=getBaseMapper().selectUpdate(bLabourAttendanceDetail);
        return data ;
    }

    public String  deletePid(String pid){
        Integer k= getBaseMapper().deletePid(pid);
        return "";
    }

    public String  selectWageDetail(BLabourAttendanceDetail bLabourAttendanceDetail){
        return getBaseMapper().selectWageDetail(bLabourAttendanceDetail);
    }
}
