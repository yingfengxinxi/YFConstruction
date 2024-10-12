package com.kakarote.build.labour.service.impl;

import com.kakarote.build.labour.entity.BO.BLabour5gvideoBO;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceEpidemicBO;
import com.kakarote.build.labour.entity.PO.BLabour5gvideo;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceEpidemic;
import com.kakarote.build.labour.mapper.BLabourAttendanceEpidemicMapper;
import com.kakarote.build.labour.service.IBLabourAttendanceEpidemicService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 疫情进出场统计 服务实现类
 * </p>
 *
 * @author zhang
 * @since 2022-11-29
 */
@Service
public class BLabourAttendanceEpidemicServiceImpl extends BaseServiceImpl<BLabourAttendanceEpidemicMapper, BLabourAttendanceEpidemic> implements IBLabourAttendanceEpidemicService {

    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BLabourAttendanceEpidemicBO> selectList(BLabourAttendanceEpidemicBO bLabourAttendanceEpidemicBO)
    {
        bLabourAttendanceEpidemicBO.setOrdersBd("create_time",false);
        BasePage<BLabourAttendanceEpidemicBO> adminMessageBasePage = getBaseMapper().selectDataList(bLabourAttendanceEpidemicBO.parse(),bLabourAttendanceEpidemicBO);
        for(int i=0;i<adminMessageBasePage.getList().size();i++){
            float timeDay = differentDaysByMillisecond(adminMessageBasePage.getList().get(i).getNucleicAcidTime(), adminMessageBasePage.getList().get(i).getClockTime());
            if(timeDay>3){
                adminMessageBasePage.getList().get(i).setNucleicAcidInFO("1");
            }else{
                if(timeDay>2){
                    adminMessageBasePage.getList().get(i).setNucleicAcidTimeText("72h");
                }else{
                    adminMessageBasePage.getList().get(i).setNucleicAcidTimeText("48h");
                }
                adminMessageBasePage.getList().get(i).setNucleicAcidInFO("0");
            }
        }

        return adminMessageBasePage;
    }

    /**
     * 新增数据
     *
     */
    @Override
    public String add(BLabourAttendanceEpidemic bLabourAttendanceEpidemic)
    {
        bLabourAttendanceEpidemic.setCreateTime(new Date());
        save(bLabourAttendanceEpidemic);
        return  "1";
    }

    /**
     * 修改数据
     *
     */
    @Override
    public String edit(BLabourAttendanceEpidemic bLabourAttendanceEpidemic)
    {
        bLabourAttendanceEpidemic.setUpdateTime(new Date());
        updateById(bLabourAttendanceEpidemic);
        return  "1";
    }

    public static float differentDaysByMillisecond(Date date1, Date date2) {
        long diffSeconds = date2.getTime() - date1.getTime();
        long diffHour = diffSeconds / (1000 * 3600);
       // System.out.println("获得小时:" + diffHour);
        float diffDay = (float) diffHour / 24;
        float floatDay = decimalFormat("0.00", diffDay);
        System.out.println(decimalFormat("0", diffDay));
        return floatDay;
    }
    public static float decimalFormat(String pattern, double value) {
        return Float.parseFloat(new DecimalFormat(pattern).format(value));
    }

  /*public static void main(String[] args) {
        String dateStr = "2022-11-30 00:00:00";
        String dateStr2 = "2022-12-04 00:00:00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date2 = format.parse(dateStr2);
            Date date = format.parse(dateStr);
            float timeDay = differentDaysByMillisecond(date, date2);
            String k="0";
            if(timeDay>3){
                k="1";
            }
            System.out.println("是否超过3天：" +k);
            System.out.println("两个日期的差距：" + differentDaysByMillisecond(date, date2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    public Map<String, Object> attendanceEpidemicStatistical(){
        Map<String, Object> data =new HashMap<String, Object>();
        List<Map<String, Object>> attendanceEpidemicStatistical =new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> attendanceEpidemicStatisticalList =new ArrayList<Map<String, Object>>();
        Map<String, Object> params = new HashMap<>();
        params.put("tenantId",UserUtil.getUser().getTenantId());
        attendanceEpidemicStatistical =  getBaseMapper().attendanceEpidemicStatistical(params);
        attendanceEpidemicStatisticalList =  getBaseMapper().attendanceEpidemicStatisticalList(params);
        int ls =0;
        int hs =0;
        int hhs =0;
        int hs1 =0;
        int hs2 =0;
        int hs3 =0;
        for(int i=0;i<attendanceEpidemicStatistical.size();i++){
            ls = ls+Integer.valueOf(attendanceEpidemicStatistical.get(i).get("lm").toString());
            hs = hs+Integer.valueOf(attendanceEpidemicStatistical.get(i).get("hm").toString());
            hhs = hhs+Integer.valueOf(attendanceEpidemicStatistical.get(i).get("hhm").toString());
            hs1 = hs1+Integer.valueOf(attendanceEpidemicStatistical.get(i).get("hs1").toString());
            hs2 = hs2+Integer.valueOf(attendanceEpidemicStatistical.get(i).get("hs2").toString());
            hs3 = hs3+Integer.valueOf(attendanceEpidemicStatistical.get(i).get("hs3").toString());

        }
        data.put("attendanceEpidemicStatistical",attendanceEpidemicStatistical);
        data.put("ls",ls);
        data.put("hs",hs);
        data.put("hhs",hhs);
        data.put("hs1",hs1);
        data.put("hs2",hs2);
        data.put("hs3",hs3);
        data.put("attendanceEpidemicStatisticalList",attendanceEpidemicStatisticalList);
        return data;
    }

}
