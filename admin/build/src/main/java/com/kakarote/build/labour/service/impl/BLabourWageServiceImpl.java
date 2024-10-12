package com.kakarote.build.labour.service.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.BO.BLabourWageBO;
import com.kakarote.build.labour.entity.PO.*;
import com.kakarote.build.labour.mapper.BLabourAttendanceMapper;
import com.kakarote.build.labour.mapper.BLabourAttendanceRecordMapper;
import com.kakarote.build.labour.mapper.BLabourWageDetailMapper;
import com.kakarote.build.labour.mapper.BLabourWageMapper;
import com.kakarote.build.labour.service.IBLabourAttendanceDetailService;
import com.kakarote.build.labour.service.IBLabourWageDetailService;
import com.kakarote.build.labour.service.IBLabourWageService;
import com.kakarote.build.labour.service.ILabourInfocollectionService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminDictData;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;

import java.math.BigDecimal;
import java.net.URLEncoder;
import org.apache.poi.ss.usermodel.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工资管理 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-04-26
 */
@Service
public class BLabourWageServiceImpl extends BaseServiceImpl<BLabourWageMapper, BLabourWage> implements IBLabourWageService {


    @Autowired
    private ILabourInfocollectionService labourInfocollectionService;

    @Autowired
    private IBLabourWageDetailService iBLabourWageDetailService;

    @Autowired
    private IBLabourAttendanceDetailService iBLabourAttendanceDetailService;

    @Autowired
    private BLabourAttendanceMapper bLabourAttendanceMapper;
    @Autowired
    private BLabourWageDetailMapper bLabourWageDetailMapper;
    @Autowired
    private AdminService AdminService;

    @Autowired
    private BLabourAttendanceRecordMapper bLabourAttendanceRecordMapper;


    @Override
    public BasePage<BLabourWage> selectList(BLabourWageBO bLabourWageBO){
        bLabourWageBO.setOrdersBd("wage_month",false);
        BasePage<BLabourWage> adminMessageBasePage = getBaseMapper().selectDataList(bLabourWageBO.parse(),bLabourWageBO);
        return adminMessageBasePage;
    }
    @Override
    public String add(BLabourWage bLabourWage){
        bLabourWage.setCreateBy(UserUtil.getUser().getRealname());
        bLabourWage.setCreateTime(new Date());
        List<LabourInfocollection> list= new ArrayList<LabourInfocollection>();//选择的组员列表
        //获取班组内全部成员信息
        if(bLabourWage.getWageTeamName()!=null&&!"".equals(bLabourWage.getWageTeamName())){
            list=labourInfocollectionService.ByTeamName(bLabourWage.getWageTeamName());
            String count=String.valueOf(list.size());
            bLabourWage.setInfocollectionNumber(count);
        }
        for(int i=0;i<list.size();i++){
            //获取当前用户在对应年月的出勤总数
            BLabourAttendanceDetail bLabourAttendanceDetail =new BLabourAttendanceDetail();
            bLabourAttendanceDetail.setCreateBy(bLabourWage.getWageMonth());
            bLabourAttendanceDetail.setInfocollectionId(String.valueOf(list.get(i).getId()));
            String  ManDays=  iBLabourAttendanceDetailService.selectWageDetail(bLabourAttendanceDetail);
            if(ManDays==null||"".equals(ManDays)){
                ManDays="0";
            }

            //获取当前用户所选年月的累计余额
            BLabourWageDetail Cumulative=new BLabourWageDetail();
            Cumulative.setCreateBy(bLabourWage.getWageMonth());
            Cumulative.setInfocollectionId(String.valueOf(list.get(i).getId()));
            String CumulativeBalance=iBLabourWageDetailService.selectCumulativeBalance(Cumulative);
            if(CumulativeBalance==null||"".equals(CumulativeBalance)){
                CumulativeBalance="0";
            }

            BLabourWageDetail bLabourWageDetail= new BLabourWageDetail();
            bLabourWageDetail.setCreateBy(UserUtil.getUser().getRealname());
            bLabourWageDetail.setCreateTime(new Date());
            bLabourWageDetail.setInfocollectionId(String.valueOf(list.get(i).getId()));
            bLabourWageDetail.setInfocollectionName(list.get(i).getName());
            bLabourWageDetail.setInfocollectionCard(list.get(i).getIdCard());
            bLabourWageDetail.setWageId(bLabourWage.getId());
            bLabourWageDetail.setWageDetailTeamName(bLabourWage.getWageTeamName());
            bLabourWageDetail.setManDays(Integer.valueOf(ManDays));
            bLabourWageDetail.setIssueDate(StrToDate(bLabourWage.getWageDate()));
            bLabourWageDetail.setCumulativeBalance(Double.valueOf(CumulativeBalance));
            bLabourWageDetail.setWageDetailMonth(bLabourWage.getWageMonth());
            if(list.get(i).getDailyWage()!=null&&!"".equals(list.get(i).getDailyWage())){
                Double DayPrice=Double.valueOf(list.get(i).getDailyWage());
                bLabourWageDetail.setDayPrice(DayPrice);
                bLabourWageDetail.setWageDetailShould(DayPrice*  Integer.valueOf(ManDays));
            }
            // 新增 用户获取打卡总工时逻辑
            BLabourAttendanceRecord bLabourAttendanceRecord  = new BLabourAttendanceRecord();
            bLabourAttendanceRecord.setInfocollectionId(String.valueOf(list.get(i).getId()));
            bLabourAttendanceRecord.setCreateBy(bLabourWage.getWageMonth());
            List<BLabourAttendanceRecord> bLabourAttendanceList= bLabourAttendanceRecordMapper.selectValidation(bLabourAttendanceRecord);
            double workHours =0;
            Date  dksj=null ;
            for(int j=0;j<bLabourAttendanceList.size();j++){

                // 判断当前循环条数是否是进场数据以本地逻辑为主0进场 1出场 （全国工人的字典与本地是反的 1进场  0出场）
                if("0".equals(bLabourAttendanceList.get(j).getClockDirection())){
                    dksj= bLabourAttendanceList.get(j).getClockTime(); //如果是进场数据则复制给循环变量
                    // 否则判断是否是出厂数据并且进场数据已赋值
                }else if("1".equals(bLabourAttendanceList.get(j).getClockDirection())&&dksj!=null){
                    // 如果是出场数据则且进场数据不为空的话、获取两个时间的相差小时数
                    Long  Hours = differenceTime(dateToString(dksj),dateToString( bLabourAttendanceList.get(j).getClockTime()));
                    //随后将计算出来的小时数据加入到全局变量中
                    workHours=workHours +(double)Hours;
                    //最后将进场数据值重新赋为null等待下次循环出进场数据后再次赋值
                    dksj=null ;
                }
            }
            BigDecimal workHoursCf =new BigDecimal(workHours);
            bLabourWageDetail.setWorkHours(workHoursCf);
            // 新增 用户获取打卡总工时逻辑

            iBLabourWageDetailService.save(bLabourWageDetail);

        }
        save(bLabourWage);
        return "";
    }
    @Override
    public String edit(BLabourWage bLabourWage){
        BLabourWage bLabourWageUpdate =new BLabourWage ();
        bLabourWageUpdate.setId(bLabourWage.getId());
        bLabourWageUpdate.setUpdateBy(UserUtil.getUser().getRealname());
        bLabourWageUpdate.setUpdateTime(new Date());

        String editValue=bLabourWage.getUpdateBy();
        BLabourWageDetail bLabourWageDetail=new BLabourWageDetail();
        if(editValue!=null&&!"".equals(editValue)){
            JSONArray jsonArray = JSONArray.parseArray(editValue);//转换前台回执数据
            System.out.println("打印数据："+jsonArray);
            for(int i=0;i<jsonArray.size();i++){
                JSONObject json=jsonArray.getJSONObject(i);
                String detailId="";
                String value="";
                if(json.containsKey("id")){//验证是否存在该key值
                    detailId=json.getString("id");
                    bLabourWageDetail.setId(detailId);
                }
                if(json.containsKey("value")){//验证是否存在该key值
                    value=json.getString("value");
                    JSONObject valueJson= JSONObject.parseObject(value);
                    String dayPrice="";
                    String manDays="";
                    String quantitiesPrice="";
                    String quantities="";
                    String wageDetailShould="";
                    String wageDetailAdvance="";
                    String monthBalance="";
                    String actualAmount="";
                    String isBackPay="";
                    String backPayMonth="";
                    String thirdPayRollCode="";
                    if(valueJson.containsKey("dayPrice")){//验证是否存在该key值
                        dayPrice=valueJson.getString("dayPrice");
                        if(dayPrice==null||"".equals(dayPrice)){
                            dayPrice="0";
                        }
                        bLabourWageDetail.setDayPrice(Double.valueOf(dayPrice));
                    }
                    if(valueJson.containsKey("manDays")){//验证是否存在该key值
                        manDays=valueJson.getString("manDays");
                        if(manDays==null||"".equals(manDays)){
                            manDays="0";
                        }
                        bLabourWageDetail.setManDays(Integer.valueOf(manDays));
                    }
                    if(valueJson.containsKey("quantitiesPrice")){//验证是否存在该key值
                        quantitiesPrice=valueJson.getString("quantitiesPrice");
                        if(quantitiesPrice==null||"".equals(quantitiesPrice)){
                            quantitiesPrice="0";
                        }
                        bLabourWageDetail.setQuantitiesPrice(Double.valueOf(quantitiesPrice));
                    }
                    if(valueJson.containsKey("quantities")){//验证是否存在该key值
                        quantities=valueJson.getString("quantities");
                        if(quantities==null||"".equals(quantities)){
                            quantities="0";
                        }
                        bLabourWageDetail.setQuantities(Integer.valueOf(quantities));
                    }
                    if(valueJson.containsKey("wageDetailShould")){//验证是否存在该key值
                        wageDetailShould=valueJson.getString("wageDetailShould");
                        if(wageDetailShould==null||"".equals(wageDetailShould)){
                            wageDetailShould="0";
                        }
                        bLabourWageDetail.setWageDetailShould(Double.valueOf(wageDetailShould));
                    }
                    if(valueJson.containsKey("wageDetailAdvance")){//验证是否存在该key值
                        wageDetailAdvance=valueJson.getString("wageDetailAdvance");
                        if(wageDetailAdvance==null||"".equals(wageDetailAdvance)){
                            wageDetailAdvance="0";
                        }
                        bLabourWageDetail.setWageDetailAdvance(Double.valueOf(wageDetailAdvance));
                    }
                    if(valueJson.containsKey("monthBalance")){//验证是否存在该key值
                        monthBalance=valueJson.getString("monthBalance");
                        if(monthBalance==null||"".equals(monthBalance)){
                            monthBalance="0";
                        }
                        bLabourWageDetail.setMonthBalance(Double.valueOf(monthBalance));
                    }
                    if(valueJson.containsKey("actualAmount")){//验证是否存在该key值
                        actualAmount=valueJson.getString("actualAmount");
                        if(actualAmount==null||"".equals(actualAmount)){
                            actualAmount="0";
                        }
                        bLabourWageDetail.setActualAmount(Double.valueOf(actualAmount));
                    }

                    if(valueJson.containsKey("isBackPay")){//验证是否存在该key值
                        isBackPay=valueJson.getString("isBackPay");
                        if(isBackPay==null||"".equals(isBackPay)){
                            isBackPay="0";
                        }
                        bLabourWageDetail.setIsBackPay(isBackPay);
                    }

                    if(valueJson.containsKey("backPayMonth")){//验证是否存在该key值
                        backPayMonth=valueJson.getString("backPayMonth");
                        if(backPayMonth==null||"".equals(backPayMonth)){
                            backPayMonth="";
                        }
                        bLabourWageDetail.setBackPayMonth(backPayMonth);
                    }

                    if(valueJson.containsKey("thirdPayRollCode")){//验证是否存在该key值
                        thirdPayRollCode=valueJson.getString("thirdPayRollCode");
                        if(thirdPayRollCode==null||"".equals(thirdPayRollCode)){
                            thirdPayRollCode="";
                        }
                        bLabourWageDetail.setThirdPayRollCode(thirdPayRollCode);
                    }

                    iBLabourWageDetailService.updateById(bLabourWageDetail);
                }

            }

        }
        BLabourWageDetail SumPid= iBLabourWageDetailService.selectSumPid(bLabourWage.getId());
        if(SumPid!=null){
            bLabourWageUpdate.setWageManDays(SumPid.getManDays());
            bLabourWageUpdate.setWageQuantities(SumPid.getQuantities());
            bLabourWageUpdate.setWageShould(SumPid.getWageDetailShould());
            bLabourWageUpdate.setWageDvance(SumPid.getWageDetailAdvance());
            bLabourWageUpdate.setWageaCumulativeBalance(SumPid.getCumulativeBalance());
        }
        updateById(bLabourWageUpdate);
        return "";
    }
    @Override
    public String validation(BLabourWage bLabourWage){
        String falg ="6";  //默认不存在用户数据，直接驳回
        List<LabourInfocollection> list= new ArrayList<LabourInfocollection>();//选择的组员列表
        //获取班组内全部成员信息
        if(bLabourWage.getWageTeamName()!=null&&!"".equals(bLabourWage.getWageTeamName())){
            list=labourInfocollectionService.ByTeamName(bLabourWage.getWageTeamName());
        }
        if(list.size()==0){
            falg ="5";  //该班组下不存在任何人员，直接驳回
            return falg;
        }
        List<BLabourWage> bLabourWageList= getBaseMapper().selectValidationt(bLabourWage);
        if(bLabourWageList.size()>0){
            falg ="4";  //该组已存在该月数据，直接驳回
            return falg;
        }
        BLabourAttendance bLabourAttendance=new BLabourAttendance ();
        bLabourAttendance.setAttendanceMonth(bLabourWage.getWageMonth());
        bLabourAttendance.setAttendanceTeamName(bLabourWage.getWageTeamName());
        List<BLabourAttendance> bLabourAttendanceList= bLabourAttendanceMapper.selectValidationt(bLabourAttendance);
        if(bLabourAttendanceList.size()>0){
            falg ="1";  //该组下存在考勤数据
        }

        return falg;

    }

    @Override
    public  void export(String ids,HttpServletResponse response) {
        String[]  strs=ids.split(",");
        List<Map<String,Object>> exportList=new ArrayList<>();
       List<AdminDictData> list_dict= AdminService.dictType("b_bankAccount").getData();
        for(String str:strs){
            Map<String,Object> BLW=getBaseMapper().selectById(str);
            List<Map<String,Object>> BLWD=bLabourWageDetailMapper.selectByWageId(String.valueOf(BLW.get("id")));
            //循环匹配字典项
            for(Map<String,Object> BLWDMap:BLWD){
                for(AdminDictData ADD:list_dict){
                    if(BLWDMap.get("bankAccount")!=null){
                        if(BLWDMap.get("bankAccount").equals(ADD.getDictValue())){
                            BLWDMap.put("bankAccount",ADD.getDictLabel());
                        }
                    }
                }
            }
            BLW.put("details",BLWD);
            exportList.add(BLW);
        }
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            int i=0;
            for(Map<String,Object> excel:exportList){
                if(i==0){
                    writer.renameSheet(i,String.valueOf(excel.get("wageMonth")));
                }
                i++;
                writer.setSheet(String.valueOf(excel.get("wageMonth")));
                //定义标题
                writer.merge(11, String.valueOf(excel.get("wageMonth"))+"  薪资明细");
                writer.merge(1,2,0,0,"人员姓名",true);
                writer.merge(1,2,1,1,"开户银行",true);
                writer.merge(1,2,2,2,"银行卡号",true);
                writer.merge(1,1,3,4,"点工(日工)",true);
                writer.merge(1,1,5,6,"包工",true);
                writer.merge(1,2,7,7,"发放日期",true);
                writer.merge(1,2,8,8,"应发工资(元)",true);
                writer.merge(1,2,9,9,"预支工资(元)",true);
                writer.merge(1,2,10,10,"本月余额(元)",true);
                writer.merge(1,2,11,11,"本月余额(元)",true);
                //自定义副标题标题别名
                writer.addHeaderAlias("infocollectionName", "人员姓名");
                writer.addHeaderAlias("bankAccount", "开户银行");
                writer.addHeaderAlias("bankCardNumber", "银行卡号");
                writer.addHeaderAlias("dayPrice", "日单价");//点工日单价
                writer.addHeaderAlias("manDays", "工日（天）");
                writer.addHeaderAlias("quantitiesPrice", "工程量单价");//包工工程量单价
                writer.addHeaderAlias("quantities", "工程量");
                writer.addHeaderAlias("issueDate", "发放日期");
                writer.addHeaderAlias("wageDetailShould", "应发工资(元");
                writer.addHeaderAlias("wageDetailAdvance", "预支工资(元)");
                writer.addHeaderAlias("monthBalance", "本月余额(元)");
                writer.addHeaderAlias("cumulativeBalance", "累计剩余(元)");
                writer.setOnlyAlias(true);//Excel中只有设置了别名的字段
                writer.setCurrentRow(writer.getCurrentRow()+1);
                writer.write((List<Map<String,Object>>)excel.get("details"), true);
                    Cell cell = writer.getCell(0, 0);
                    CellStyle cellStyle = cell.getCellStyle();
                    cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());//设置图案颜色
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//设置图案样式
                    Font font = writer.createFont();
                    font.setBold(true);
                    font.setFontHeightInPoints((short) 8);
                    cellStyle.setFont(font);
                    cellStyle.setWrapText(true);
                    cell.setCellStyle(cellStyle);
                    //设置列的宽度
                writer.setColumnWidth(0,11);
                writer.setColumnWidth(1,11);
                writer.setColumnWidth(2,20);
                writer.setColumnWidth(3,11);
                writer.setColumnWidth(4,11);
                writer.setColumnWidth(5,11);
                writer.setColumnWidth(6,11);
                writer.setColumnWidth(7,15);
                writer.setColumnWidth(8,15);
                writer.setColumnWidth(9,15);
                writer.setColumnWidth(10,15);
                writer.setColumnWidth(11,15);

                for(int k=0;k<((List<Map<String,Object>>)excel.get("details")).size();k++){
                    if(k<3){
                        //设置标题行固定高度
                        writer.setRowHeight(k, 20);
                    }else{
                        //设置数据行高
                        writer.setRowHeight(k, 15);
                    }
                }
            }
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            String title = "工资明细";
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(title, "UTF-8")+".xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("导出工资明细错误");
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
    public static String dateToString(Date date) {
        String str = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        str =format.format(date);
        return str;
    }

    public static Long differenceTime(String startTime,String endTime){
        //时间格式，自己可以随便定义
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //一天的毫秒数
        long nd = 1000*60*60*24;
        //一小时的毫秒数
        long nh = 1000*60*60;
        //一分钟的毫秒数
        long nm = 1000*60;
        //一秒钟的毫秒数
        long ns = 1000;

        long diff = 0;
        try{
            //获取两个时间的毫秒时间差
            diff = format.parse(endTime).getTime() - format.parse(startTime).getTime();
        }catch (Exception e){
            e.printStackTrace();
        }
        //计算相差的天数
        long day = diff/nd;
        long k=0;
        if(day>0){
            k=24* day ;
        }
        //计算相差的小时
        long hour = diff%nd/nh;
        //计算相差的分钟
        long min = diff%nd%nh/nm;
        //计算相差的秒2
        long sec = diff%nd%nh%nm/ns;
        hour= hour+k;
        System.err.println("时间相差："+day+"天"+hour+"小时"+min+"分钟"+sec+"秒。");
        return hour;
    }

}
