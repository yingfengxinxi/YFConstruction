package com.kakarote.build.labour.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceDetailBO;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceRecordBO;
import com.kakarote.build.labour.entity.PO.*;
import com.kakarote.build.labour.mapper.BLabourAttendanceRecordMapper;
import com.kakarote.build.labour.service.IBLabourAttendanceConfigurationService;
import com.kakarote.build.labour.service.IBLabourAttendanceRecordService;
import com.kakarote.build.labour.service.ILabourInfocollectionService;
import com.kakarote.build.labour.service.ILabourSubcontractorService;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hpsf.Decimal;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import java.util.Collections;
import java.text.DecimalFormat;

/**
 * <p>
 * 考勤记录管理 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
@Service
@Slf4j
public class BLabourAttendanceRecordServiceImpl extends BaseServiceImpl<BLabourAttendanceRecordMapper, BLabourAttendanceRecord> implements IBLabourAttendanceRecordService {


    @Autowired
    private ILabourInfocollectionService labourInfocollectionService;

    @Autowired
    private IBLabourAttendanceConfigurationService iBLabourAttendanceConfigurationService;

    @Autowired
    private IProjectInfoService iProjectInfoService;

    @Autowired
    private ILabourSubcontractorService subcontractorService;
    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BLabourAttendanceRecord> selectList(BLabourAttendanceRecordBO bLabourAttendanceRecordBO)
    {
        BasePage<BLabourAttendanceRecord> adminMessageBasePage = getBaseMapper().selectDataList(bLabourAttendanceRecordBO.parse(),bLabourAttendanceRecordBO);
        return adminMessageBasePage;
    }

    public List<BLabourAttendanceRecord> selectDetailList(BLabourAttendanceRecord bLabourAttendanceRecord){

        List<BLabourAttendanceRecord> list =getBaseMapper().selectDetailList(bLabourAttendanceRecord);
        return list ;
    }

    public List<BLabourAttendanceRecord> selectByMonth(BLabourAttendanceRecord bLabourAttendanceRecord){

        List<BLabourAttendanceRecord> list =getBaseMapper().selectByMonth(bLabourAttendanceRecord);
        return list ;
    }

    public List<BLabourAttendanceRecord> selectDetailListUpdate(BLabourAttendanceRecord bLabourAttendanceRecord){

        List<BLabourAttendanceRecord> list =getBaseMapper().selectDetailListUpdate(bLabourAttendanceRecord);
        return list ;
    }

    public List<BLabourAttendanceRecord> selectValidation(BLabourAttendanceRecord bLabourAttendanceRecord){

        List<BLabourAttendanceRecord> list =getBaseMapper().selectValidation(bLabourAttendanceRecord);
        return list ;
    }

    /**
     * excel导入员工
     *
     * @param file file
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject excelImport(MultipartFile file) {
        List<List<Object>> errList = new ArrayList<>();
        String filePath = getFilePath(file);
        AtomicReference<Integer> num = new AtomicReference<>(0);
        ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {

            if (rowIndex > 1) {
                LabourInfocollection data= new LabourInfocollection ();
                //姓名
                Object name = rowList.get(0);
                if(name == null || name.equals("")) {
                    throw new CrmException(1001, "姓名不能为空");
                }
                //身份证号
                Object idCard = rowList.get(1);
                if(idCard == null || idCard.equals("")){
                    throw new CrmException(1001, "身份证号不能为空");
                }

                //考勤卡号
                Object attendanceNumber = rowList.get(2);
//                if(attendanceNumber == null || attendanceNumber.equals("")){
//                    throw new CrmException(1001, "考勤卡号不能为空");
//                }

                data.setName(name.toString().trim());
                data.setIdCard(idCard.toString().trim());
                data.setAttendanceNumber(String.valueOf(attendanceNumber));
                LabourInfocollection labourInfocollection=  labourInfocollectionService.selectInfoRecord(data);
                BLabourAttendanceRecord bLabourAttendanceRecord = new BLabourAttendanceRecord ();
                if(labourInfocollection!=null){
                    bLabourAttendanceRecord.setInfocollectionId(String.valueOf(labourInfocollection.getId()));
                    bLabourAttendanceRecord.setAttendanceRecordTeamName(String.valueOf(labourInfocollection.getTeamName()));
                    //项目隔离取消时若获取不到项目id则解除，若项目隔离未取消则注掉
                   // bLabourAttendanceRecord.setProjectId(labourInfocollection.getProjectId());
                }
                bLabourAttendanceRecord.setCreateBy(UserUtil.getUser().getRealname());
                bLabourAttendanceRecord.setCreateTime(new Date());
                bLabourAttendanceRecord.setInfocollectionName(name.toString().trim());
                bLabourAttendanceRecord.setInfocollectionCard(idCard.toString().trim());
                bLabourAttendanceRecord.setAttendanceNumber(attendanceNumber.toString().trim());
                String ClockDirection = rowList.get(3).toString().trim();
                if(ClockDirection == null || ClockDirection.equals("")){
                    throw new CrmException(1001, "打卡方向不能为空");
                }
                if (StrUtil.isNotEmpty(ClockDirection)) {
                    if ("进场".equals(ClockDirection)) {
                        bLabourAttendanceRecord.setClockDirection("0");
                    } else {
                        bLabourAttendanceRecord.setClockDirection("1");
                    }
                }

                Object otime = rowList.get(4);
                if(otime == null || otime.equals("")){
                    throw new CrmException(1001, "打卡时间不能为空");
                }
                String  time= "";
                if(otime instanceof String){
                    time=otime.toString().trim();
                    boolean legalDate = isLegalDate(time);
                    if(!legalDate){
                        throw new CrmException(1001, "打卡时间格式不正确，应为：yyyy-MM-dd HH:mm:ss");
                    }
                }else if(otime instanceof Date){
                    Date date = (Date)otime;

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                    time = simpleDateFormat.format(date);
                }else{
                    throw new CrmException(1001, "打卡时间格式不正确，应为：yyyy-MM-dd HH:mm:ss");
                }

                bLabourAttendanceRecord.setClockTime(StrToDate(time));

                // 判断是否为null
                String bodyTemperature = rowList.get(5).toString().trim();
                if (bodyTemperature != null && !bodyTemperature.equals("")) {
                    bLabourAttendanceRecord.setBodyTemperature(bodyTemperature);
                }
                String alcoholicity = rowList.get(6).toString().trim();
                if (alcoholicity != null && !alcoholicity.equals("")) {
                    bLabourAttendanceRecord.setAlcoholicity(alcoholicity);
                }
                String bloodPressure = rowList.get(7).toString().trim();
                if (bloodPressure != null && !bloodPressure.equals("")) {
                    bLabourAttendanceRecord.setBloodPressure(bloodPressure);
                }
                String bloodOxygen = rowList.get(8).toString().trim();
                if (bloodOxygen != null && !bloodOxygen.equals("")) {
                    bLabourAttendanceRecord.setBloodOxygen(bloodOxygen);
                }

                /*String image = rowList.get(9).toString().trim();
                if (image != null && !image.equals("")) {
                    bLabourAttendanceRecord.setImage(image);
                }

                String channel = rowList.get(10).toString().trim();
                if (channel != null && !channel.equals("")) {
                    bLabourAttendanceRecord.setChannel(channel);
                }

                String attendType = rowList.get(11).toString().trim();
                if (attendType != null && !attendType.equals("")) {
                    bLabourAttendanceRecord.setAttendType(attendType);
                }

                String lng = rowList.get(12).toString().trim();
                if (lng != null && !lng.equals("")) {
                    BigDecimal decimal = new BigDecimal(lng);
                    bLabourAttendanceRecord.setLng(decimal);
                }

                String lat = rowList.get(13).toString().trim();
                if (lat != null && !lat.equals("")) {
                    BigDecimal decimal = new BigDecimal(lat);
                    bLabourAttendanceRecord.setLat(decimal);
                }*/
                bLabourAttendanceRecord.setDataGeneration("0");
                save(bLabourAttendanceRecord);
            }
        });
        FileUtil.del(filePath);
        JSONObject result = new JSONObject().fluentPut("totalSize", num.get()).fluentPut("errSize", 0);
       if (errList.size() > 0) {
            BigExcelWriter writer = null;
            try {
                String token = IdUtil.simpleUUID();
                writer = ExcelUtil.getBigWriter(FileUtil.getTmpDirPath() + "/" + token);
                writer.merge(6, "系统用户导入模板(*)为必填项");
                for (int i = 0; i < 7; i++) {
                    writer.setColumnWidth(i, 20);
                }
                writer.setDefaultRowHeight(20);
                Cell cell = writer.getCell(0, 0);
                CellStyle cellStyle = cell.getCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font font = writer.createFont();
                font.setBold(true);
                font.setFontHeightInPoints((short) 16);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                writer.writeHeadRow(Arrays.asList("错误信息", "姓名(*)", "身份证号(*)", "考勤卡号(*)", "打卡方向", "打卡时间"));
                writer.write(errList);
                result.fluentPut("errSize", errList.size()).fluentPut("token", token);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
        return result;
    }

    @Override
    public BasePage<Map<String, Object>> getAttendanceDetailList(BLabourAttendanceRecordBO bLabourAttendanceRecordBO) {

        // 判断时间范围是否为nul，为null默认查询今天的数据
        if (bLabourAttendanceRecordBO.getStartTime() == null && bLabourAttendanceRecordBO.getEndTime() == null) {
            Date date = DateUtil.parseDate(DateUtil.today());
            bLabourAttendanceRecordBO.setStartTime(date);
        }

        bLabourAttendanceRecordBO.setTenantId(Integer.valueOf(String.valueOf(UserUtil.getUser().getTenantId())));
        // 查询数据
        BasePage<Map<String, Object>> list = getBaseMapper().getAttendanceDetailList(bLabourAttendanceRecordBO.parse(),bLabourAttendanceRecordBO);

        return list;
    }

    @Override
    public Map<String, Object> selectEntryAndExit() {

        Map<String, Object> data = new HashMap<>();
        // 创建查询对象
        Map<String, Object> params1 = new HashMap<>();
        Map<String, Object> params2 = new HashMap<>();
        // 查询当天进出场数据
        params1.put("clockTime", DateUtil.today());
        // 拷贝
        params2.putAll(params1);

        // 查询进场数据
        params1.put("clockDirection", "0");
        List<Map<String, Object>> entryList = getBaseMapper().selectEntryAndExit(params1);
        // 查询出场数据
        params2.put("clockDirection", "1");
        List<Map<String, Object>> exitList = getBaseMapper().selectEntryAndExit(params2);

        data.put("entryList", entryList);
        data.put("exitList", exitList);

        // 查询出场数据
        return data;
    }

    private String getFilePath(MultipartFile file) {
        String dirPath = FileUtil.getTmpDirPath();
        try {
            InputStream inputStream = file.getInputStream();
            File fromStream = FileUtil.writeFromStream(inputStream, dirPath + "/" + IdUtil.simpleUUID() + file.getOriginalFilename());
            return fromStream.getAbsolutePath();
        } catch (IOException e) {
            throw new CrmException(SystemCodeEnum.SYSTEM_UPLOAD_FILE_ERROR);
        }
    }

    /**
     * 日期转换String 转 Date
     *
     */
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期转换String 转 Date
     *
     */
    public static Date StrToDateYYYYmmdd(String str) {

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
     * 日期转换String 转 Date
     *
     */
    public static String  DateToString(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = "";
        try {
            str = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    //判断 时间格式
    private static boolean isLegalDate(String sDate) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public void  demoDataAdd(){
        LabourInfocollection data= new LabourInfocollection ();
        data.setWorkRole("20");
        List<LabourInfocollection> labourInfocollection=  labourInfocollectionService.selectInfoRecordAll(data);
        List<String> date=new ArrayList<>();
        date.addAll(getMonthFullDay(2022,4));
        //date.addAll(getMonthFullDay(2021,9));
        //date.addAll(getMonthFullDay(2021,10));
        Random r = new Random();
        int fen  =0;
        int miao=0;
        for(int i=0;i<labourInfocollection.size();i++){

            for(int j=0;j<date.size();j++){
                BLabourAttendanceRecord bLabourAttendanceRecordJc = new BLabourAttendanceRecord ();
                bLabourAttendanceRecordJc.setInfocollectionId(String.valueOf(labourInfocollection.get(i).getId()));
                bLabourAttendanceRecordJc.setAttendanceRecordTeamName(String.valueOf(labourInfocollection.get(i).getTeamName()));
                bLabourAttendanceRecordJc.setProjectId(labourInfocollection.get(i).getProjectId());
                bLabourAttendanceRecordJc.setCreateBy(UserUtil.getUser().getRealname());
                bLabourAttendanceRecordJc.setCreateTime(new Date());
                bLabourAttendanceRecordJc.setInfocollectionName(labourInfocollection.get(i).getName());
                bLabourAttendanceRecordJc.setInfocollectionCard(labourInfocollection.get(i).getIdCard());
                bLabourAttendanceRecordJc.setAttendanceNumber(labourInfocollection.get(i).getAttendanceNumber());
                bLabourAttendanceRecordJc.setClockDirection("0");
                fen  =r.nextInt(59);
                String fenStringjc="";
                if(fen>=10){
                    fenStringjc=String.valueOf(fen);
                }else{
                    fenStringjc="0"+String.valueOf(fen);
                }
                miao  =r.nextInt(59);
                String miaoStringjc="";
                if(miao>=10){
                    miaoStringjc=String.valueOf(miao);
                }else{
                    miaoStringjc="0"+String.valueOf(miao);
                }
                String dateTimeJc =date.get(j) +" 08:"+fenStringjc+":"+miaoStringjc;
                bLabourAttendanceRecordJc.setClockTime(StrToDate(dateTimeJc));
                bLabourAttendanceRecordJc.setBodyTemperature("36.5°");
                bLabourAttendanceRecordJc.setAlcoholicity("133mg/100mL");
                bLabourAttendanceRecordJc.setBloodPressure("1mmHg");
                bLabourAttendanceRecordJc.setBloodOxygen("97%");
                bLabourAttendanceRecordJc.setChannel("东北部通道1");
                bLabourAttendanceRecordJc.setAttendType("1");
                BigDecimal decimaljc = new BigDecimal("116.397128");
                bLabourAttendanceRecordJc.setLng(decimaljc);
                BigDecimal decimaljc2 = new BigDecimal("39.916527");
                bLabourAttendanceRecordJc.setLat(decimaljc2);
                bLabourAttendanceRecordJc.setDataGeneration("0");

                save(bLabourAttendanceRecordJc);
                BLabourAttendanceRecord bLabourAttendanceRecordCc = new BLabourAttendanceRecord ();
                bLabourAttendanceRecordCc.setInfocollectionId(String.valueOf(labourInfocollection.get(i).getId()));
                bLabourAttendanceRecordCc.setAttendanceRecordTeamName(String.valueOf(labourInfocollection.get(i).getTeamName()));
                bLabourAttendanceRecordCc.setProjectId(labourInfocollection.get(i).getProjectId());
                bLabourAttendanceRecordCc.setCreateBy(UserUtil.getUser().getRealname());
                bLabourAttendanceRecordCc.setCreateTime(new Date());
                bLabourAttendanceRecordCc.setInfocollectionName(labourInfocollection.get(i).getName());
                bLabourAttendanceRecordCc.setInfocollectionCard(labourInfocollection.get(i).getIdCard());
                bLabourAttendanceRecordCc.setAttendanceNumber(labourInfocollection.get(i).getAttendanceNumber());
                bLabourAttendanceRecordCc.setClockDirection("1");
                fen  =r.nextInt(59);
                String fenStringcc="";
                if(fen>=10){
                    fenStringcc=String.valueOf(fen);
                }else{
                    fenStringcc="0"+String.valueOf(fen);
                }
                miao  =r.nextInt(59);
                String miaoStringcc="";
                if(miao>=10){
                    miaoStringcc=String.valueOf(miao);
                }else{
                    miaoStringcc="0"+String.valueOf(miao);
                }
                String dateTimeCc =date.get(j) +" 18:"+fenStringcc+":"+miaoStringcc;
                bLabourAttendanceRecordCc.setClockTime(StrToDate(dateTimeCc));
                bLabourAttendanceRecordCc.setBodyTemperature("36.5°");
                bLabourAttendanceRecordCc.setAlcoholicity("133mg/100mL");
                bLabourAttendanceRecordCc.setBloodPressure("1mmHg");
                bLabourAttendanceRecordCc.setBloodOxygen("97%");
                bLabourAttendanceRecordCc.setChannel("东北部通道1");
                bLabourAttendanceRecordCc.setAttendType("1");
                BigDecimal decimalcc = new BigDecimal("116.397128");
                bLabourAttendanceRecordCc.setLng(decimalcc);
                BigDecimal decimalcc2 = new BigDecimal("39.916527");
                bLabourAttendanceRecordCc.setLat(decimalcc2);
                bLabourAttendanceRecordCc.setDataGeneration("0");


                save(bLabourAttendanceRecordCc);

            }

        }
    }



    /**
     *  java 获取 获取某年某月 所有日期（yyyy-mm-dd格式字符串）
     * @param year
     * @param month
     * @return
     */
    public static List<String> getMonthFullDay(int year , int month){
        SimpleDateFormat dateFormatYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
        List<String> fullDayList = new ArrayList<>(32);
        List<String> fullDayListZs = new ArrayList<>(32);
        // 获得当前日期对象
        Calendar cal = Calendar.getInstance();
        cal.clear();// 清除信息
        cal.set(Calendar.YEAR, year);
        // 1月从0开始
        cal.set(Calendar.MONTH, month-1 );
        // 当月1号
        cal.set(Calendar.DAY_OF_MONTH,1);
        int count = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int j = 1; j <= count ; j++) {
            String week=dateFormatYYYYMMDD.format(cal.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            String weekZc ="";
            if("2021-09-18".equals(week)||"2021-09-26".equals(week)||"2021-10-09".equals(week)){
                weekZc="星期一";
            }else{
                weekZc = sdf.format(StrToDateYYYYmmdd(week));
            }

            if("星期六".equals(weekZc)||"星期日".equals(weekZc)
                    ||"2021-09-20".equals(week)||"2021-09-21".equals(week)||"2021-10-01".equals(week)
                    ||"2021-10-04".equals(week)||"2021-10-05".equals(week)||"2021-10-06".equals(week)
                    ||"2021-10-07".equals(week)){

            }else{
                fullDayListZs.add(dateFormatYYYYMMDD.format(cal.getTime()));
            }
            fullDayList.add(dateFormatYYYYMMDD.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH,1);
        }
        return fullDayListZs;
    }




    @Override
    public Map<String, Object> selectPositionAttendance() {

        Map<String, Object> data = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("clockTime",DateToString(new Date()));
        params.put("tenantId",UserUtil.getUser().getTenantId());
        Map<String, Object> kqrs = getBaseMapper().selectPositionAttendance(params);//本日考勤人数
        Map<String, Object> zrs = getBaseMapper().selectPositionAttendanceZs(params);//考勤总人数

        //项目总工程师
        data.put("xmzgcscqs",kqrs.get("xmzgcs"));
        data.put("xmzgcscql",cql(kqrs.get("xmzgcs").toString(),zrs.get("xmzgcs").toString()));
        data.put("xmzgcsqqs",qqs(kqrs.get("xmzgcs").toString(),zrs.get("xmzgcs").toString()));
        data.put("xmzgcszs",zrs.get("xmzgcs"));

        //项目经理
        data.put("xmjlcqs",kqrs.get("xmjl"));
        data.put("xmjlcql",cql(kqrs.get("xmjl").toString(),zrs.get("xmjl").toString()));
        data.put("xmjlqqs",qqs(kqrs.get("xmjl").toString(),zrs.get("xmjl").toString()));
        data.put("xmjlzs",zrs.get("xmjl"));

        //工程经理
        data.put("gcjlcqs",kqrs.get("gcjl"));
        data.put("gcjlcql",cql(kqrs.get("gcjl").toString(),zrs.get("gcjl").toString()));
        data.put("gcjlqqs",qqs(kqrs.get("gcjl").toString(),zrs.get("gcjl").toString()));
        data.put("gcjlzs",zrs.get("gcjl"));

        //施工员
        data.put("sgycqs",kqrs.get("sgy"));
        data.put("sgycql",cql(kqrs.get("sgy").toString(),zrs.get("sgy").toString()));
        data.put("sgyqqs",qqs(kqrs.get("sgy").toString(),zrs.get("sgy").toString()));
        data.put("sgyzs",zrs.get("sgy"));

        //质量员
        data.put("zlycqs",kqrs.get("xmzgcs"));
        data.put("zlycql",cql(kqrs.get("xmzgcs").toString(),zrs.get("xmzgcs").toString()));
        data.put("zlyqqs",qqs(kqrs.get("xmzgcs").toString(),zrs.get("xmzgcs").toString()));
        data.put("zlyzs",zrs.get("xmzgcs"));

        //安全员
        data.put("aqycqs",kqrs.get("aqy"));
        data.put("aqycql",cql(kqrs.get("aqy").toString(),zrs.get("aqy").toString()));
        data.put("aqyqqs",qqs(kqrs.get("aqy").toString(),zrs.get("aqy").toString()));
        data.put("aqyzs",zrs.get("aqy"));

        //材料员
        data.put("clycqs",kqrs.get("cly"));
        data.put("clycql",cql(kqrs.get("cly").toString(),zrs.get("cly").toString()));
        data.put("clyqqs",qqs(kqrs.get("cly").toString(),zrs.get("cly").toString()));
        data.put("clyzs",zrs.get("cly"));


        // 查询出场数据
        return data;
    }

    public String  cql(String cqrs,String zrs ){ //获取出勤率
        int cqrsInt=Integer.valueOf(cqrs);
        int zrsInt=Integer.valueOf(zrs);
        NumberFormat numberFormat = NumberFormat.getInstance();

        numberFormat.setMaximumFractionDigits(2);
        String result = "";
        if(zrsInt == 0) {
            result = "0";
        } else {
            result = numberFormat.format((float) cqrsInt / (float) zrsInt * 100);
        }

        return result;
    }
    public String  qqs(String cqrs,String zrs ){ //获取缺勤人数
        int cqrsInt=Integer.valueOf(cqrs);
        int zrsInt=Integer.valueOf(zrs);
        int qqs=zrsInt-cqrsInt;
        return String.valueOf(qqs);
    }

    public List<BLabourAttendanceRecord> selectDynamic(){
        List<BLabourAttendanceRecord> list =getBaseMapper().selectDynamic(UserUtil.getUser().getTenantId().toString());
        return list ;
    }
    // 项目缺勤率获取 ========================================分割线开始=======================================================
    @Override
    public List<Map<String,Object>> projectAttendanceRate(){
        List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
        List<Map> listProject = iProjectInfoService.slelctAllAttendance(UserUtil.getUser().getTenantId().toString());
        for(int xm=0;xm<listProject.size();xm++){
            Map xmData =listProject.get(xm);
            BLabourAttendanceRecord bLabourAttendanceRecord=new BLabourAttendanceRecord();
            LabourInfocollection labourInfocollection =new LabourInfocollection();
            labourInfocollection.setProjectId(Integer.valueOf(xmData.get("id").toString()));
            labourInfocollection.setWorkRole("10");
            List<LabourInfocollection>  labourInfocollectionList=  labourInfocollectionService.byManageType(labourInfocollection);
            double  qqlzj=0;
            for(int i=0;i<labourInfocollectionList.size();i++){
                if(labourInfocollectionList.get(i).getEnterDate() != null) {
                    List<String> days =getDays(labourInfocollectionList.get(i).getEnterDate(),DateToString(new Date())); // 获取当前人员从进场时间，到现在共计应打卡日期的时间组
                    if(!days.isEmpty()) {
                        bLabourAttendanceRecord.setInfocollectionId(labourInfocollectionList.get(i).getId().toString());
                        String cqs=getBaseMapper().approach(bLabourAttendanceRecord,days); //统计当前人员的出勤总数
                        String qql=cql(String.valueOf(days.size()-Integer.valueOf(cqs)) ,String.valueOf(days.size())); //获取缺勤率率
                        qqlzj=qqlzj+Double.valueOf(qql);
                    }
                }
            }
            String averageQql ="0";
            if( labourInfocollectionList.size()>0){
                double dRet = qqlzj / labourInfocollectionList.size();
                 averageQql = String.format("%.2f", dRet);
            }
            Map<String,Object> data =new HashMap();
            data.put("projectName",xmData.get("projectName"));
            data.put("averageQql",averageQql);
            list.add(data);
        }
        return list ;
    }

    // 项目缺勤率获取 ========================================分割线结束=======================================================

    // 项目30天出勤率获取 ========================================分割线开始=======================================================
    @Override
    public List<Map<String,Object>> projectAttendanceRateGo(){
        List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
        List<Map> listProject = iProjectInfoService.slelctAllAttendance(UserUtil.getUser().getTenantId().toString());
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -30);
        String endDate = new SimpleDateFormat("yyyy-MM-dd").format(now.getTime());
        for(int xm=0;xm<listProject.size();xm++){
            Map xmData =listProject.get(xm);
            BLabourAttendanceRecord bLabourAttendanceRecord=new BLabourAttendanceRecord();
            LabourInfocollection labourInfocollection =new LabourInfocollection();
            labourInfocollection.setProjectId(Integer.valueOf(xmData.get("id").toString()));
            labourInfocollection.setWorkRole("10");
            List<LabourInfocollection>  labourInfocollectionList=  labourInfocollectionService.byManageType(labourInfocollection);
            double  cqlzj=0;
            for(int i=0;i<labourInfocollectionList.size();i++){
                List<String> days =getDays(endDate,DateToString(new Date())); // 获取30天前日期，到现在共计应打卡日期的时间组
                if(!days.isEmpty()) {
                    bLabourAttendanceRecord.setInfocollectionId(labourInfocollectionList.get(i).getId().toString());
                    String cqs=getBaseMapper().approach(bLabourAttendanceRecord,days); //统计当前人员的出勤总数
                    String cql=cql(cqs ,String.valueOf(days.size())); //获取出勤率
                    cqlzj=cqlzj+Double.valueOf(cql);
                }
            }
            String averageQql = "0";
            if(labourInfocollectionList.size()>0){
                double dRet = cqlzj / labourInfocollectionList.size();
                 averageQql = String.format("%.2f", dRet);
            }
            Map<String,Object> data =new HashMap();
            data.put("projectName",xmData.get("projectName"));
            data.put("averageQql",averageQql);
            list.add(data);
        }
        return list ;
    }

    // 项目30天出勤率获取 ========================================分割线结束=======================================================

    // 考勤率获取 ========================================分割线开始=======================================================

    @Override
    public List<BLabourAttendanceRecord> AttendanceRate(){
        List<BLabourAttendanceRecord> list =new ArrayList<>();

        BLabourAttendanceRecord bLabourAttendanceRecord=new BLabourAttendanceRecord();
        LabourInfocollection labourInfocollection =new LabourInfocollection();
        labourInfocollection.setWorkRole("10");
       List<LabourInfocollection>  labourInfocollectionList=  labourInfocollectionService.byManageType(labourInfocollection);
       for(int i=0;i<labourInfocollectionList.size();i++){
           List<String> days =getDays(labourInfocollectionList.get(i).getEnterDate(),DateToString(new Date())); // 获取当前人员从进场时间，到现在共计应打卡日期的时间组
/*           String daySize="";
           for(int j=0;j<days.size();j++){  // 将时间组转换为查询的日期条件
               if("".equals(daySize)){
                   daySize= "'"+ days.get(j)+"'";
               }else{
                   daySize=daySize+","+ "'"+ days.get(j)+"'";
               }
           }*/
           bLabourAttendanceRecord.setInfocollectionId(labourInfocollectionList.get(i).getId().toString());
           //bLabourAttendanceRecord.setAttendanceNumber(daySize);
           String cqs=getBaseMapper().approach(bLabourAttendanceRecord,days); //统计当前人员的出勤总数
           String qql=cql(String.valueOf(days.size()-Integer.valueOf(cqs)) ,String.valueOf(days.size())); //获取缺勤率率

           BLabourAttendanceRecord data =new BLabourAttendanceRecord();
           data.setAttendanceNumber(qql);
           data.setInfocollectionName(labourInfocollectionList.get(i).getName());
           data.setInfocollectionCard(labourInfocollectionList.get(i).getNote());
           list.add(data);
       }
       if(!list.isEmpty()) {
           Collections.sort(list, new Comparator<BLabourAttendanceRecord>() {
               @Override
               public int compare(BLabourAttendanceRecord u1, BLabourAttendanceRecord u2) {
                  double diff =  Double.valueOf(u1.getAttendanceNumber()) - Double.valueOf(u2.getAttendanceNumber());
                   if (diff > 0) {
                       return -1;// （小的放在第一则 该处为1）
                   }else if (diff < 0) {
                       return 1;//  （小的放在第一则 该处为-1）
                   }
                   return 0; //相等为0
               }
           });
       }
        return list ;
    }



    public  List<String> getDays(String startTime, String endTime) {

        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        BLabourAttendanceConfiguration bLabourAttendanceConfiguration =new BLabourAttendanceConfiguration();
        bLabourAttendanceConfiguration.setConfigurationType("0");
        List<BLabourAttendanceConfiguration> weekList = iBLabourAttendanceConfigurationService.dataList(bLabourAttendanceConfiguration);//获取上班的星期
        String [] weekListSize=new String [7];
        for(int i=0;i<weekList.size();i++){
            weekListSize[i]=weekList.get(i).getConfigurationContent();
        }

        bLabourAttendanceConfiguration.setConfigurationType("1");
        List<BLabourAttendanceConfiguration> holidayDaysList = iBLabourAttendanceConfigurationService.dataList(bLabourAttendanceConfiguration);//获取节假日的全部日期
        bLabourAttendanceConfiguration.setConfigurationType("2");
        bLabourAttendanceConfiguration.setSpecialType("1");
        List<BLabourAttendanceConfiguration> specialDaysListSb = iBLabourAttendanceConfigurationService.dataList(bLabourAttendanceConfiguration);//获取特殊日期上班
        bLabourAttendanceConfiguration.setSpecialType("2");
        List<BLabourAttendanceConfiguration> specialDaysListbSb = iBLabourAttendanceConfigurationService.dataList(bLabourAttendanceConfiguration);//获取特殊日期不上班
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                String falg="0";//判断该条日期是否需要录入到考勤中   0需要    1不需要
                String week=dateFormat.format(tempStart.getTime());
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                String weekZc ="";
                weekZc = sdf.format(StrToDateYYYYmmdd(week));
                boolean flag = Arrays.asList(weekListSize).contains(weekZc); //判断当前日期是否在上班日期中  是返回true  否返回false

                if(flag){ //判断是上班日期则前往验证是否存在于节假日日期中
                    for(int j=0;j<holidayDaysList.size();j++){  //判断当前日期是否存在于节假日日期中
                      List <String> holidayDays=  holidayDays(DateToString(holidayDaysList.get(j).getGoWorkDate()),DateToString(holidayDaysList.get(j).getAfterWorkDate()));
                      if(holidayDays.contains(week)){//判断若当前日期存在于节假日日期中，则将考勤判断更新为不需要考勤判断
                          falg="1";
                          break;
                      }
                    }
                    for(int j=0;j<specialDaysListbSb.size();j++){  //判断当前日期是否存在于特殊日期中的不上班日期
                        List <String> holidayDays=  holidayDays(DateToString(specialDaysListbSb.get(j).getGoWorkDate()),DateToString(specialDaysListbSb.get(j).getAfterWorkDate()));
                        if(holidayDays.contains(week)){//判断若当前日期存在于特殊日期中的不上班日期中，则将考勤判断更新为不需要考勤判断
                            falg="1";
                            break;
                        }
                    }
                }else{  //否则说明当前日期不是上班日期 （如周六、周日之类的主要看设置）
                    falg="1"; //不上班日期默认赋值为不计入考勤
                    for(int j=0;j<specialDaysListSb.size();j++){  //判断当前日期是否存在于特殊日期中的上班日期
                        List <String> specialDays=  holidayDays(DateToString(specialDaysListSb.get(j).getGoWorkDate()),DateToString(specialDaysListSb.get(j).getAfterWorkDate()));
                        if(specialDays.contains(week)){ //判断若当前日期存在于特殊日期中的上班日期中，则将考勤判断更新为需要考勤判断
                            falg="0";
                            break;
                        }
                    }
                }
                if("0".equals(falg)){
                    days.add(dateFormat.format(tempStart.getTime()));
                }
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    public static List<String> holidayDays(String startTime, String endTime) {

        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    // 考勤率获取 ========================================分割线结束=======================================================
    @Override
    public Map<String,Object> peopleNumber(){

        Map<String,Object> data =new HashMap<>();
        BLabourAttendanceRecord bLabourAttendanceRecord=new BLabourAttendanceRecord();
        bLabourAttendanceRecord.setCreateBy(DateToString(new Date()));
        LabourInfocollection labourInfocollection =new LabourInfocollection();
        List<LabourInfocollection>  labourInfocollectionList=  labourInfocollectionService.byManageType2(labourInfocollection);//已入场非零工的总劳务人数
        data.put("lwzrs",labourInfocollectionList.size());
        List<String> idList =new ArrayList<String>();
        for(int i=0;i<labourInfocollectionList.size();i++){
            idList.add(labourInfocollectionList.get(i).getId().toString());
        }
        String cqs=getBaseMapper().peopleNumberXcssrs(bLabourAttendanceRecord,idList);
        data.put("xcssrs",cqs);

        labourInfocollection.setWorkRole("10");
        List<LabourInfocollection>  manageTypeList=  labourInfocollectionService.byManageType2(labourInfocollection);//所有的管理人员
        data.put("glryrs",manageTypeList.size());
        idList =new ArrayList<String>();
        for(int i=0;i<manageTypeList.size();i++){
            idList.add(manageTypeList.get(i).getId().toString());
        }
        String manageTypecqs=getBaseMapper().peopleNumber(bLabourAttendanceRecord,idList);//获取管理人员的出勤人数
        String manageTypecql=cql(manageTypecqs ,String.valueOf(manageTypeList.size())); //获取管理人员出勤率
        data.put("glrycql",manageTypecql);

        labourInfocollection.setWorkRole("20");
        List<LabourInfocollection>  teamNameList=  labourInfocollectionService.byManageType2(labourInfocollection);//所有的建筑工人
        data.put("jzgrrs",teamNameList.size());
        idList =new ArrayList<String>();
        for(int i=0;i<teamNameList.size();i++){
            idList.add(teamNameList.get(i).getId().toString());
        }
        String teamNamecqs=getBaseMapper().peopleNumber(bLabourAttendanceRecord,idList);//获取建筑工人的出勤人数
        String teamNamecql=cql(teamNamecqs ,String.valueOf(teamNameList.size())); //获取建筑工人出勤率
        data.put("jzgrcql",teamNamecql);

        return data ;
    }

    @Override
    public Map<String,Object> workersConstitute(){ //获取建筑工人构成分析全部数据详情
        Map<String,Object> dataCount =new HashMap<>();
        Map<String,Object> data =new HashMap<>();
        Map<String,Object> params =new HashMap<>();
        params.put("dqtime",DateToString(new Date()));
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.YEAR, -1); //年份减1
        Date lastMonth = ca.getTime(); //结果
        params.put("tbtime",DateToString(lastMonth));
        Calendar ca2 = Calendar.getInstance();//得到一个Calendar的实例
        ca2.setTime(new Date()); //设置时间为当前时间
        ca2.add(Calendar.MONTH, -1); //求前一月
        Date Month = ca2.getTime(); //结果
        params.put("hbtime",DateToString(Month));
        params.put("tenantId",UserUtil.getUser().getTenantId());
        data =  getBaseMapper().workersConstitute(params);// 全部人员的总数 、男性数、女性数、同比数、环比数
        String zrs =data.get("zs").toString();
        String nanrs =data.get("nan").toString();
        String nantbrs =data.get("nantb").toString();
        String nanhbrs =data.get("nanhb").toString();
        String nvrs =data.get("nv").toString();
        String nvtbrs =data.get("nvtb").toString();
        String nvhbrs =data.get("nvhb").toString();

        dataCount.put("lssgrs",zrs);// 历史人数
        dataCount.put("nxzb",cql(nanrs ,zrs));//男性占比
        dataCount.put("nvxzb",cql(nvrs ,zrs));//女性占比
        if("0".equals(nantbrs)){
            dataCount.put("nantb","100");//男性同比率
        }else{
            dataCount.put("nantb",cql(String.valueOf(Integer.valueOf(nanrs)-Integer.valueOf(nantbrs)) ,nantbrs));//男性同比率
        }
        if("0".equals(nanhbrs)){
            dataCount.put("nanhb","100");//男性环比率
        }else{
            dataCount.put("nanhb",cql(String.valueOf(Integer.valueOf(nanrs)-Integer.valueOf(nanhbrs)) ,nanhbrs));//男性环比率
        }
        if("0".equals(nvtbrs)){
            dataCount.put("nvtb","100");//女性同比率
        }else{
            dataCount.put("nvtb",cql(String.valueOf(Integer.valueOf(nvrs)-Integer.valueOf(nvtbrs)) ,nvtbrs));//女性同比率
        }
        if("0".equals(nvhbrs)){
            dataCount.put("nvhb","100");//女性环比率
        }else{
            dataCount.put("nvhb",cql(String.valueOf(Integer.valueOf(nvrs)-Integer.valueOf(nvhbrs)) ,nvhbrs));//女性环比率
        }


        data =new HashMap<>();
        data =  getBaseMapper().ageConstitute(params);// 获取年龄数据
        List<BLabourAttendanceRecord> list = new ArrayList<BLabourAttendanceRecord>();
        BLabourAttendanceRecord  BLabourAttendanceRecord=new BLabourAttendanceRecord();
        dataCount.put("nl1",data.get("nl1").toString());//20-30 人数
        BLabourAttendanceRecord=new BLabourAttendanceRecord();
        BLabourAttendanceRecord.setAttendanceNumber(data.get("nl1").toString());
        BLabourAttendanceRecord.setInfocollectionName("20-30岁");
        list.add(BLabourAttendanceRecord);
        dataCount.put("nl2",data.get("nl2").toString());//30-40 人数
        BLabourAttendanceRecord=new BLabourAttendanceRecord();
        BLabourAttendanceRecord.setAttendanceNumber(data.get("nl2").toString());
        BLabourAttendanceRecord.setInfocollectionName("30-40岁");
        list.add(BLabourAttendanceRecord);
        dataCount.put("nl3",data.get("nl3").toString());//40-50 人数
        BLabourAttendanceRecord=new BLabourAttendanceRecord();
        BLabourAttendanceRecord.setAttendanceNumber(data.get("nl3").toString());
        BLabourAttendanceRecord.setInfocollectionName("40-50岁");
        list.add(BLabourAttendanceRecord);
        dataCount.put("nl4",data.get("nl4").toString());//50-60 人数
        BLabourAttendanceRecord=new BLabourAttendanceRecord();
        BLabourAttendanceRecord.setAttendanceNumber(data.get("nl4").toString());
        BLabourAttendanceRecord.setInfocollectionName("50-60岁");
        list.add(BLabourAttendanceRecord);
        dataCount.put("nl5",data.get("nl5").toString());//60以上 人数
        BLabourAttendanceRecord=new BLabourAttendanceRecord();
        BLabourAttendanceRecord.setAttendanceNumber(data.get("nl4").toString());
        BLabourAttendanceRecord.setInfocollectionName("60岁以上");
        list.add(BLabourAttendanceRecord);
        Collections.sort(list, new Comparator<BLabourAttendanceRecord>() { //重新排序来获取当前人数最多的年龄段说明
            @Override
            public int compare(BLabourAttendanceRecord u1, BLabourAttendanceRecord u2) {
                double diff =  Double.valueOf(u1.getAttendanceNumber()) - Double.valueOf(u2.getAttendanceNumber());
                if (diff > 0) {
                    return -1;// （小的放在第一则 该处为1）
                }else if (diff < 0) {
                    return 1;//  （小的放在第一则 该处为-1）
                }
                return 0; //相等为0
            }
        });
        dataCount.put("ageInstructions",list.get(0).getInfocollectionName());
        List<Map<String, Object>> schooling =  getBaseMapper().schoolingConstitute(params);// 获取学历数据
        dataCount.put("schooling",schooling);
        List<BLabourAttendanceRecord> list2 = new ArrayList<BLabourAttendanceRecord>();
        for(int i=0;i<schooling.size();i++){
            Map <String, Object> xh =new HashMap<>();
            xh=schooling.get(i);
            BLabourAttendanceRecord=new BLabourAttendanceRecord();
            BLabourAttendanceRecord.setAttendanceNumber(xh.get("zs").toString());
            BLabourAttendanceRecord.setInfocollectionName(xh.get("mc").toString());
            list2.add(BLabourAttendanceRecord);
        }
        Collections.sort(list2, new Comparator<BLabourAttendanceRecord>() {//重新排序来获取当前人数最多的文化程度说明
            @Override
            public int compare(BLabourAttendanceRecord u1, BLabourAttendanceRecord u2) {
                double diff =  Double.valueOf(u1.getAttendanceNumber()) - Double.valueOf(u2.getAttendanceNumber());
                if (diff > 0) {
                    return -1;// （小的放在第一则 该处为1）
                }else if (diff < 0) {
                    return 1;//  （小的放在第一则 该处为-1）
                }
                return 0; //相等为0
            }
        });
        dataCount.put("schoolingInstructions",list2.get(0).getInfocollectionName());


        return dataCount ;
    }

    @Override
    public Map<String,Object> workerTypeConstitute(){ //获取建筑工人工种分析全部数据详情
        Map<String,Object> dataCount =new HashMap<>();
        Map<String,Object> data =new HashMap<>();
        Map<String,Object> params =new HashMap<>();
        params.put("tenantId",UserUtil.getUser().getTenantId());
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.YEAR, -1); //年份减1
        Date lastMonth = ca.getTime(); //结果
        params.put("tbtime",DateToString(lastMonth));
        Calendar ca2 = Calendar.getInstance();//得到一个Calendar的实例
        ca2.setTime(new Date()); //设置时间为当前时间
        ca2.add(Calendar.MONTH, -1); //求前一月
        Date Month = ca2.getTime(); //结果
        params.put("hbtime",DateToString(Month));
        List<Map<String, Object>> workerTypeList =  getBaseMapper().workerTypeList(params);
        int zs=0;
        for(int i=0;i<workerTypeList.size();i++){
            zs=zs+Integer.valueOf(workerTypeList.get(i).get("zs").toString());
            //params.put("mc",workerTypeList.get(i).get("workTypeId").toString());
            Map<String, Object> workerTypeListTbHb=  getBaseMapper().workerTypeListTbHb(params);
            workerTypeList.get(i).put("name",workerTypeList.get(i).get("mc").toString());
            workerTypeList.get(i).put("value",workerTypeList.get(i).get("zs").toString());
            if(i == 0){
                workerTypeList.get(i).put("color","#00D0DC");
            }else if(i == 1){
                workerTypeList.get(i).put("color","#5B97F9");
            }else if(i == 2){
                workerTypeList.get(i).put("color","#F3BC40");
            }else if(i == 3){
                workerTypeList.get(i).put("color","#27D35A");
            }else if(i == 4){
                workerTypeList.get(i).put("color","#5850F3");
            }
            if(!"0".equals(workerTypeListTbHb.get("tb").toString())){
                workerTypeList.get(i).put("value1",cql(
                        String.valueOf(Integer.valueOf(workerTypeList.get(i).get("zs").toString())
                                -Integer.valueOf(workerTypeListTbHb.get("tb").toString())) ,workerTypeListTbHb.get("tb").toString()));//同比
            }else{
                workerTypeList.get(i).put("value1","100");
            }
            if(!"0".equals(workerTypeListTbHb.get("hb").toString())){
                workerTypeList.get(i).put("value2",cql(
                        String.valueOf(Integer.valueOf(workerTypeList.get(i).get("zs").toString())
                                -Integer.valueOf(workerTypeListTbHb.get("hb").toString())) ,workerTypeListTbHb.get("hb").toString()));//同比
            }else{
                workerTypeList.get(i).put("value2","100");
            }

        }
        for(int i=0;i<workerTypeList.size();i++){
            workerTypeList.get(i).put("bfb",cql(workerTypeList.get(i).get("zs").toString(),String.valueOf(zs)));
        }
        dataCount.put("workerTypeList",workerTypeList);
        return dataCount ;
    }

    @Override
    public Map<String,Object> wageConstitute(){ //获取工资统计数据详情
        Map<String,Object> dataCount =new HashMap<>();
        Map<String,Object> params =new HashMap<>();
        List<String> mouth =new ArrayList<>();
        List<String> yfgz =new ArrayList<>();
        List<String> sfgz =new ArrayList<>();
        for(int i=6;i>0;i--){
            Calendar ca2 = Calendar.getInstance();//得到一个Calendar的实例
            ca2.setTime(new Date()); //设置时间为当前时间
            ca2.add(Calendar.MONTH, -i); //求前i月
            mouth.add(new SimpleDateFormat("yyyy-MM").format(ca2.getTime()));
        }
        params.put("tenantId",UserUtil.getUser().getTenantId());
        for(int i=0;i<mouth.size();i++){
            params.put("rq",mouth.get(i));
            Map<String,Object> data =getBaseMapper().wageConstitute(params);
            yfgz.add(data.get("yfgz").toString());
            sfgz.add(data.get("sfgz").toString());
        }

        dataCount.put("mouth",mouth);
        dataCount.put("yfgz",yfgz);
        dataCount.put("sfgz",sfgz);
        return dataCount ;
    }

    @Override
    public Map<String,Object> defaultWageConstitute(){ //获取拖欠工资统计数据详情
        List<BLabourAttendanceRecord>  bLabourAttendanceRecord =new ArrayList<BLabourAttendanceRecord>();
        Map<String,Object> dataCount =new HashMap<>();
        Map<String,Object> params =new HashMap<>();
        Calendar ca2 = Calendar.getInstance();//得到一个Calendar的实例
        ca2.setTime(new Date()); //设置时间为当前时间
        ca2.add(Calendar.MONTH, -1); //求前1月
        params.put("tenantId",UserUtil.getUser().getTenantId());
        params.put("datetime",new SimpleDateFormat("yyyy-MM").format(ca2.getTime()));
        List<LabourSubcontractor> list = subcontractorService.selcetIdsAndName();
        for(int i=0;i<list.size();i++){
            params.put("pid",list.get(i).getId());
            Map<String,Object> data = getBaseMapper().defaultWageConstitute(params);
            BLabourAttendanceRecord record =new BLabourAttendanceRecord();
            record.setInfocollectionName(list.get(i).getRealName());
            String yfgz=data.get("yfgz").toString();
            String sfgz=data.get("sfgz").toString();
            double k=Double.valueOf(yfgz) -Double.valueOf(sfgz);
            if(k!=0){
                record.setAttendanceNumber(getNumberWan(String.valueOf(k)));
            }else{
                record.setAttendanceNumber("0");
            }
            bLabourAttendanceRecord.add(record);
        }
        Collections.sort(bLabourAttendanceRecord, new Comparator<BLabourAttendanceRecord>() {//重新排序来获取拖欠工资统计数据详情
            @Override
            public int compare(BLabourAttendanceRecord u1, BLabourAttendanceRecord u2) {
                double diff =  Double.valueOf(u1.getAttendanceNumber()) - Double.valueOf(u2.getAttendanceNumber());
                if (diff > 0) {
                    return -1;// （小的放在第一则 该处为1）
                }else if (diff < 0) {
                    return 1;//  （小的放在第一则 该处为-1）
                }
                return 0; //相等为0
            }
        });
        dataCount.put("defaultWageList",bLabourAttendanceRecord);
        return dataCount ;
    }

    @Override
    public List<Map<String, Object>> selectAttendanceNumber() {
        // 近30天数据
        Map<String, Object> map = null;
        map = dateMap("2");

        return getBaseMapper().selectAttendanceNumber(map);
    }

    @Override
    public BasePage<BLabourAttendanceRecord> selectDynamicNew(Map<String,Object> param) {
        //本方法无分页时，默认取前12条数据
        PageEntity page=new PageEntity();
        if(param.get("page")==null){
            page.setPage(0);
        }else{
            page.setPage(Integer.valueOf(String.valueOf(param.get("page"))));
        }
        if(param.get("limit")==null){
            page.setLimit(12);
        }else{
            page.setLimit(Integer.valueOf(String.valueOf(param.get("limit"))));
        }
        BasePage<BLabourAttendanceRecord> list =getBaseMapper().selectDynamicNew(page.parse(),param);
        return list ;
    }

    @Override
    public BasePage<Map<String, Object>> peopleInfoDetails(Map<String, Object> params) {
        String type=String.valueOf(params.get("type"));
        LabourInfocollection labour =new LabourInfocollection();
        PageEntity page=new PageEntity();
        page.setPage(Integer.valueOf(String.valueOf(params.get("page"))));
        page.setLimit(Integer.valueOf(String.valueOf(params.get("limit"))));
        //0：现场实时人数，1：劳务总人数，2普通工人，3：管理人员
        if("0".equals(type)){
            List<LabourInfocollection> list=labourInfocollectionService.byManageType2(labour);//已入场非零工的总劳务人数
            List<String> idList =new ArrayList<String>();
            for(int i=0;i<list.size();i++){
                idList.add(list.get(i).getId().toString());
            }
            BLabourAttendanceRecord bLabourAttendanceRecord=new BLabourAttendanceRecord();
            bLabourAttendanceRecord.setCreateBy(DateToString(new Date()));
            BasePage<Map<String,Object>>  bp=getBaseMapper().peopleInfos(page.parse(),bLabourAttendanceRecord,idList);
            return bp;
        }else   if("1".equals(type)){
            BasePage<Map<String,Object>>  list=labourInfocollectionService.byManageType3(page.parse(),labour);//已入场非零工的总劳务人数
            return list;
        }else   if("2".equals(type)){
            labour.setWorkRole("20");
            BasePage<Map<String,Object>>  list=  labourInfocollectionService.byManageType3(page.parse(),labour);//所有的建筑工人
            return list;
        }else   if("3".equals(type)){
            labour.setWorkRole("10");
            BasePage<Map<String,Object>>  list=  labourInfocollectionService.byManageType3(page.parse(),labour);//所有的管理人员
            return list;
        }
        return null;
    }

    @Override
    public Map selectByUserId(Integer id) {
        return getBaseMapper().selectByUserId(id);
    }

    @Override
    public void addClock(BLabourAttendanceRecord record) {
        record.setClockTime(new Date());
        UserInfo user = UserUtil.getUser();
        Map map = getBaseMapper().selectInfocollectionByUserId(user.getUserId().intValue());
        record.setInfocollectionId(map.get("id")+"");
        record.setInfocollectionCard(map.get("idCard")+"");
        record.setInfocollectionName(map.get("name")+"");
        save(record);
    }

    public static void main(String[] args) {

        System.out.println(getNumberWan("256.33")+"万元");
    }

    public static String getNumberWan(String value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        // 转换为万元（除以10000）
        BigDecimal decimal = bigDecimal.divide(new BigDecimal("10000"));
        // 保留两位小数
       // DecimalFormat formater = new DecimalFormat("0");
        // 四舍五入
        //formater.setRoundingMode(RoundingMode.HALF_UP);

        // 格式化完成之后得出结果
        String rs = decimal.toString();
        return rs;
    }

    /**
     * @param type type=1 查询前一周的时间（包括今天）type=2 查询前一月的时间（包括今天）type=3 查询前一年的月份（包括当月） type = 4 最近24小时 (包括当前小时)
     * @return
     */
    private Map<String, Object> dateMap(String type) {
        Map<String, Object> params = new HashMap<>();
        if (type.equals("1")) {
            params.put("format", "%Y-%m-%d");
            params.put("num", "7");
            params.put("dateType", "day");
        } else if (type.equals("2")) {
            params.put("format", "%Y-%m-%d");
            params.put("num", "30");
            params.put("dateType", "day");
        } else if (type.equals("3")) {
            params.put("format", "%Y-%m");
            params.put("num", "12");
            params.put("dateType", "month");
        } else  if (type.equals("4")) {
            params.put("format", "%Y-%m-%d %H");
            params.put("num", "24");
            params.put("dateType", "hour");
        }
        return params;
    }

}
