package com.kakarote.build.labour.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceDetail;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceRecord;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.mapper.BLabourAttendanceMapper;
import com.kakarote.build.labour.service.IBLabourAttendanceDetailService;
import com.kakarote.build.labour.service.IBLabourAttendanceRecordService;
import com.kakarote.build.labour.service.IBLabourAttendanceService;
import com.kakarote.build.labour.service.ILabourInfocollectionService;
import com.kakarote.build.safety.entity.BO.BSafetySafetyTrainingBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyTraining;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 考勤管理 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
@Service
public class BLabourAttendanceServiceImpl extends BaseServiceImpl<BLabourAttendanceMapper, BLabourAttendance> implements IBLabourAttendanceService {

    @Autowired
    private ILabourInfocollectionService labourInfocollectionService;

    @Autowired
    private IBLabourAttendanceDetailService iBLabourAttendanceDetailService;

    @Autowired
    private IBLabourAttendanceRecordService iBLabourAttendanceRecordService;
    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BLabourAttendanceBO> selectList(BLabourAttendanceBO bLabourAttendanceBO)
    {
        bLabourAttendanceBO.setOrdersBd("attendance_month",false);
        BasePage<BLabourAttendanceBO> adminMessageBasePage = getBaseMapper().selectDataList(bLabourAttendanceBO.parse(),bLabourAttendanceBO);

        for(int i=0;i<adminMessageBasePage.getList().size();i++){

          Long count=  iBLabourAttendanceDetailService.countByattendance( adminMessageBasePage.getList().get(i).getId());

            //根据当前数据获取总出勤出数
            adminMessageBasePage.getList().get(i).setAttendanceTotal(String.valueOf(count));
            //根据当前数据获取总工作数
            adminMessageBasePage.getList().get(i).setManDayTotal(String.valueOf(count));
        }

        return adminMessageBasePage;
    }

    /**
     * 新增数据
     *
     */
    @Override
    public String add(BLabourAttendance bLabourAttendance)
    {
        String falg="1";
        String uuid=UUID.randomUUID().toString().replaceAll("-", "");
        List<LabourInfocollection> list= new ArrayList<LabourInfocollection>();//选择的组员列表
        List<String> DayByYearMonth =new ArrayList<String>(); //月份中全部日期的列表
        //获取班组内全部成员信息
        if(bLabourAttendance.getAttendanceTeamName()!=null&&!"".equals(bLabourAttendance.getAttendanceTeamName())){
            list=labourInfocollectionService.ByTeamName(bLabourAttendance.getAttendanceTeamName());
            String count=String.valueOf(list.size());
            bLabourAttendance.setInfocollectionNumber(count);
        }
        //获取用户所选月份的全部信息
        if(bLabourAttendance.getAttendanceMonth()!=null&&!"".equals(bLabourAttendance.getAttendanceMonth())){
            String[] strs = bLabourAttendance.getAttendanceMonth().split("-");
            if(strs.length>1){
                DayByYearMonth=getDayByYearMonth(Integer.valueOf(strs[0]),Integer.valueOf(strs[1]));
            }
        }
        //判断该组员是否有组员
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                for(int j=0;j<DayByYearMonth.size();j++){
                    //数据填入到详细表的对象中用于新增
                    BLabourAttendanceDetail  bLabourAttendanceDetail=new  BLabourAttendanceDetail();
                    bLabourAttendanceDetail.setCreateBy(UserUtil.getUser().getRealname());
                    bLabourAttendanceDetail.setCreateTime(new Date());
                    bLabourAttendanceDetail.setAttendanceId(uuid);//主数据id
                    bLabourAttendanceDetail.setInfocollectionId(String.valueOf(list.get(i).getId()));//人员id
                    bLabourAttendanceDetail.setInfocollectionName(list.get(i).getName());//人员姓名
                    bLabourAttendanceDetail.setInfocollectionCard(list.get(i).getIdCard());//人员身份证
                    bLabourAttendanceDetail.setAttendanceNumber(list.get(i).getAttendanceNumber());//考勤卡号
                    bLabourAttendanceDetail.setAttendanceDate(StrToDate(DayByYearMonth.get(j)));//考勤日期
                    bLabourAttendanceDetail.setInfoHoliday(isWeekend(DayByYearMonth.get(j)));//是否是周末

                    //去记录表中查询当日是否存在该用户的考勤信息
                    BLabourAttendanceRecord bLabourAttendanceRecord =new BLabourAttendanceRecord ();
                    bLabourAttendanceRecord.setInfocollectionName(list.get(i).getName());
                    bLabourAttendanceRecord.setInfocollectionCard(list.get(i).getIdCard());
                    bLabourAttendanceRecord.setAttendanceNumber(list.get(i).getAttendanceNumber());
                    bLabourAttendanceRecord.setCreateBy(DayByYearMonth.get(j));
                    List<BLabourAttendanceRecord> listRecord=iBLabourAttendanceRecordService.selectDetailList(bLabourAttendanceRecord);

                    //获取打卡记录的数量
                    int recordNumber=listRecord.size();//后续需替换成查询记录表中的打卡数量
                    //判断当前日期是否是周末 是周末则不需要考勤 否则需要进行考勤判断
                    if("1".equals(isWeekend(DayByYearMonth.get(j)))){
                        bLabourAttendanceDetail.setInfoAttendance("0");//未出勤
                    }else{
                        if(recordNumber>0){
                            bLabourAttendanceDetail.setInfoAttendance("1");//已出勤
                        }else{
                            bLabourAttendanceDetail.setInfoAttendance("0");//未出勤
                        }
                    }
                    //判断本次生成的统计打卡次数是否与记录中的当日打卡次数相等
                    if(bLabourAttendance.getAttendanceClock()==recordNumber&&recordNumber!=0){
                        bLabourAttendanceDetail.setInfoAbnormal("0");//非异常
                    }else{
                        //周六周日为非异常数据
                        if("1".equals(isWeekend(DayByYearMonth.get(j)))){
                            bLabourAttendanceDetail.setInfoAbnormal("0");//非异常
                        }else{
                            bLabourAttendanceDetail.setInfoAbnormal("1");//异常
                        }
                    }
                    //保存本条数据
                    iBLabourAttendanceDetailService.save(bLabourAttendanceDetail);
                }
            }
        }else{
            falg="2";
        }
        bLabourAttendance.setCreateBy(UserUtil.getUser().getRealname());
        bLabourAttendance.setCreateTime(new Date());
        bLabourAttendance.setId(uuid);
        if("1".equals(falg)){
         save(bLabourAttendance);
        }
        return falg;
    }

    /**
     * 根据年月获取某个月的每一天日期
     *
     * @param yearParam
     * @param monthParam
     * @return
     */
    public static List<String> getDayByYearMonth(int yearParam, int monthParam) {
        List<String> list = new ArrayList<>();
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        aCalendar.set(yearParam, monthParam - 1, 1);
        //年份
        int year = aCalendar.get(Calendar.YEAR);
        //月份
        int month = aCalendar.get(Calendar.MONTH) + 1;
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        for (int i = 1; i <= day; i++) {
            String aDate = null;
            if (month < 10 && i < 10) {
                aDate = year + "-0" + month + "-0" + i;
            }
            if (month < 10 && i >= 10) {
                aDate = year + "-0" + month + "-" + i;
            }
            if (month >= 10 && i < 10) {
                aDate = year + "-" + month + "-0" + i;
            }
            if (month >= 10 && i >= 10) {
                aDate = year + "-" + month + "-" + i;
            }

            list.add(aDate);
        }
        return list;
    }

    /**
     * 获取当日是否是周末
     *
     */
    public  String isWeekend(String bDate)  {
        try {
            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            Date bdate = format1.parse(bDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(bdate);
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                return "1";
            } else {
                return "0";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "3";
        }
    }
    /**
     * 日期转换String 转 Date
     *
     */
    public  Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 新增数据
     *
     */
    @Override
    public String edit(BLabourAttendance bLabourAttendance)
    {
        String editValue=bLabourAttendance.getCreateBy();
        if(editValue!=null&&!"".equals(editValue)){
            JSONArray jsonArray = JSONArray.parseArray(editValue);//转换前台回执数据
            System.out.println("打印数据："+jsonArray);
            for(int i=0;i<jsonArray.size();i++){
                JSONObject json=jsonArray.getJSONObject(i);

                String infocollectionId="";
                String date="";
                String infoAttendance="";
                if(json.containsKey("id")){//验证是否存在该key值
                    infocollectionId=json.getString("id");
                    String[] arr =infocollectionId.split("-");
                    if(arr.length>1){
                        infocollectionId=arr[0];
                        date=arr[1];
                    }
                }
                if(json.containsKey("value")){
                    infoAttendance=json.getString("value");
                }
                if(!"".equals(infocollectionId)&&!"".equals(date)&&!"".equals(infoAttendance)){
                    //去记录表中查询当日是否存在该用户的考勤信息
                    BLabourAttendanceRecord bLabourAttendanceRecord =new BLabourAttendanceRecord ();
                    bLabourAttendanceRecord.setInfocollectionId(infocollectionId);
                    bLabourAttendanceRecord.setCreateBy(bLabourAttendance.getAttendanceMonth()+"-"+date);
                    List<BLabourAttendanceRecord> listRecord=iBLabourAttendanceRecordService.selectDetailListUpdate(bLabourAttendanceRecord);
                    BLabourAttendance attendanceClock= getById(bLabourAttendance.getId());
                    if("1".equals(infoAttendance)){
                        if(listRecord.size()>0){
                            for(int j=0;j<listRecord.size();j++){
                                BLabourAttendanceRecord bLabourAttendanceR=new BLabourAttendanceRecord();
                                bLabourAttendanceR.setId(listRecord.get(j).getId());
                                bLabourAttendanceR.setDataShowJudge("0");
                                iBLabourAttendanceRecordService.updateById(bLabourAttendanceR);
                            }
                        }else{
                            //新增打卡记录表数据用于改变统计
                            LabourInfocollection labourInfocollection=  labourInfocollectionService.getById(infocollectionId);
                            BLabourAttendanceRecord bLabourAttendanceR=new BLabourAttendanceRecord();
                            BLabourAttendanceRecord bLabourAttendanceR2=new BLabourAttendanceRecord();
                            bLabourAttendanceR.setCreateBy(UserUtil.getUser().getRealname());
                            bLabourAttendanceR.setCreateTime(new Date());
                            bLabourAttendanceR.setInfocollectionId(infocollectionId);
                            bLabourAttendanceR.setInfocollectionName(labourInfocollection.getName());
                            bLabourAttendanceR.setInfocollectionCard(labourInfocollection.getIdCard());
                            bLabourAttendanceR.setAttendanceNumber(labourInfocollection.getAttendanceNumber());
                            bLabourAttendanceR.setClockDirection("0");
                            bLabourAttendanceR.setClockTime(StrToDate(bLabourAttendance.getAttendanceMonth()+"-"+date));
                            bLabourAttendanceR.setDataGeneration("1");
                            bLabourAttendanceR2 = bLabourAttendanceR;
                            if("4".equals(attendanceClock.getAttendanceClock())){
                                BLabourAttendanceRecord bLabourAttendanceR3=bLabourAttendanceR;
                                BLabourAttendanceRecord bLabourAttendanceR4=bLabourAttendanceR2;
                                bLabourAttendanceR3.setClockDirection("0");
                                bLabourAttendanceR3.setClockTime(StrToDate(bLabourAttendance.getAttendanceMonth()+"-"+date));
                                bLabourAttendanceR3.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                                iBLabourAttendanceRecordService.save(bLabourAttendanceR3);
                                bLabourAttendanceR4.setClockDirection("1");
                                bLabourAttendanceR4.setClockTime(StrToDate(bLabourAttendance.getAttendanceMonth()+"-"+date));
                                bLabourAttendanceR4.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                                iBLabourAttendanceRecordService.save(bLabourAttendanceR4);
                            }
                            iBLabourAttendanceRecordService.save(bLabourAttendanceR);
                            bLabourAttendanceR2.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                            bLabourAttendanceR2.setClockDirection("1");
                            bLabourAttendanceR2.setClockTime(StrToDate(bLabourAttendance.getAttendanceMonth()+"-"+date));
                            iBLabourAttendanceRecordService.save(bLabourAttendanceR2);
                        }
                    }else if("0".equals(infoAttendance)){
                        if(listRecord.size()>0){
                        for(int j=0;j<listRecord.size();j++){
                            BLabourAttendanceRecord bLabourAttendanceR=new BLabourAttendanceRecord();
                            bLabourAttendanceR.setId(listRecord.get(j).getId());
                            bLabourAttendanceR.setDataShowJudge("1");
                            iBLabourAttendanceRecordService.updateById(bLabourAttendanceR);
                        }
                        }
                    }
                    //更新详情数据中的考勤状态字段
                    BLabourAttendanceDetail data= new BLabourAttendanceDetail();
                    data.setAttendanceId(bLabourAttendance.getId());
                    data.setInfocollectionId(infocollectionId);
                    data.setCreateBy(bLabourAttendance.getAttendanceMonth()+"-"+date);
                    BLabourAttendanceDetail bLabourAttendanceDetail=iBLabourAttendanceDetailService.selectUpdate(data);
                    if(bLabourAttendanceDetail!=null){
                        bLabourAttendanceDetail.setInfoAttendance(infoAttendance);
                        if("1".equals(infoAttendance)){
                            bLabourAttendanceDetail.setInfoAbnormal("0");
                        }else{
                            bLabourAttendanceDetail.setInfoAbnormal("1");
                        }
                        bLabourAttendanceDetail.setUpdateBy(UserUtil.getUser().getRealname());
                        bLabourAttendanceDetail.setUpdateTime(new Date());
                        iBLabourAttendanceDetailService.updateById(bLabourAttendanceDetail);
                    }

                }

            }

        }
        return"";
    }

    public String validation(BLabourAttendance bLabourAttendance){

        String falg ="6";  //默认不存在用户数据，直接驳回
        List<LabourInfocollection> list= new ArrayList<LabourInfocollection>();//选择的组员列表
        //获取班组内全部成员信息
        if(bLabourAttendance.getAttendanceTeamName()!=null&&!"".equals(bLabourAttendance.getAttendanceTeamName())){
            list=labourInfocollectionService.ByTeamName(bLabourAttendance.getAttendanceTeamName());
        }
        if(list.size()==0){
            falg ="5";  //该班组下不存在任何人员，直接驳回
            return falg;
        }
       List<BLabourAttendance> bLabourAttendanceList= getBaseMapper().selectValidationt(bLabourAttendance);
        if(bLabourAttendanceList.size()>0){
            falg ="4";  //该组已存在该月数据，直接驳回
            return falg;
        }
        for(int i=0;i<list.size();i++){
            BLabourAttendanceRecord bLabourAttendanceRecord=new BLabourAttendanceRecord();
            bLabourAttendanceRecord.setInfocollectionId(String.valueOf(list.get(i).getId()));
            bLabourAttendanceRecord.setCreateBy(bLabourAttendance.getAttendanceMonth());
            List<BLabourAttendanceRecord> bLabourAttendanceRecordList=   iBLabourAttendanceRecordService.selectValidation(bLabourAttendanceRecord);
            if(bLabourAttendanceRecordList.size()>0){
                falg ="1";  //存在用户数据，正常通过
                break;
            }
        }

        return falg;
    }
}
