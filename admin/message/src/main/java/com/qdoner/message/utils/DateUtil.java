package com.qdoner.message.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Title: DateUtil
 * @Package com.kakarote.build.utils
 * @Description: 描述
 * @author: ZangYu
 * @date: 2021-07-16 10:46
 */
public class DateUtil {

    public static String YMDHMS_ = "yyyy-MM-dd HH:mm:ss";

    public static String YMDHMS = "yyyyMMddHHmmss";

    public static String YMD = "yyyy-MM-dd";

    public static String HMS = "HH:mm:ss";
    public static void main(String[] args){
        String p = "yyyy-MM-dd HH:00:00";
        String endDate = DateUtil.stringDate(new Date(),p);
        List<String > hoursList = new ArrayList<>();
        String startDate = null;
        try{
            startDate =DateUtil.addDate(endDate,-1,p);
            hoursList = DateUtil.getHoursStrList(startDate,endDate,p);
            hoursList.add(endDate);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(hoursList);

    }
    /**
     * 字符串转Date
     * @param date
     * @param format
     * @return
     * @throws Exception
     */
    public static Date parseDate(String date,String format){
        try {
            DateFormat dft = new SimpleDateFormat(format);
            Date d = dft.parse(date);
            return d;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串格式化
     * @param date
     * @param format
     * @return
     * @throws Exception
     */
    public static String parseStrDate(String date,String format){
        try {
            DateFormat dft = new SimpleDateFormat(format);
            return dft.format(dft.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Date转字符串
     * @param date
     * @param format
     * @return
     * @throws Exception
     */
    public static String stringDate(Date date,String format){
        try {
            DateFormat dft = new SimpleDateFormat(format);
            String d = dft.format(date);
            return d;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //两个时间段间隔的天数
    public static Long getTwoDateNum(String startDate,String endDate) throws Exception{

        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date star = dft.parse(startDate);//开始时间

        if(endDate == null || endDate.equals("") || endDate.equals("null")){//若结束日期为空，获取当前时间
            Date date = new Date();
            endDate = dft.format(date);
        }
        Date endDay=dft.parse(endDate);//结束时间
        Long starTime=star.getTime();
        Long endTime=endDay.getTime();
        Long num=endTime-starTime;//时间戳相差的毫秒数

        return num/24/60/60/1000;
    }

    //通过开始时间、结束时间、数组长度，
    public static List<String> getDateList(String startDate, String endDate, int i) throws Exception{
        List<String> lista = new ArrayList<>();
        lista.add(startDate);

        Long twoDateNum = getTwoDateNum(startDate, endDate);//开始时间、结束时间之间的天数

        int avgNum = (int)(twoDateNum/i);

        for(int j = 0; j < i-2; j++){
            startDate = addDate(startDate, Math.round(avgNum));
            lista.add(startDate);
        }
        lista.add(endDate);

        return lista;
    }

    //日期加上天数，后的日期
    //dayl增加的天数，date传入的日期
    public static String addDate(String date,int dayl) throws ParseException {
        long day = (int)dayl;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date currdate = format.parse(date);

        long time = currdate.getTime(); // 得到指定日期的毫秒数
        day = day*24*60*60*1000; // 要加上的天数转换成毫秒数
        time+=day; // 相加得到新的毫秒数
        Date date1 = new Date(time);// 将毫秒数转换成日期

        String format1 = format.format(date1);
        return format1;
    }

    //dayl增加的天数，date传入的日期
    public static String addDate(String date,int dayl,String p) throws ParseException {
        long day = (int)dayl;
        SimpleDateFormat format = new SimpleDateFormat(p);
        Date currdate = format.parse(date);

        long time = currdate.getTime(); // 得到指定日期的毫秒数
        day = day*24*60*60*1000; // 要加上的天数转换成毫秒数
        time+=day; // 相加得到新的毫秒数
        Date date1 = new Date(time);// 将毫秒数转换成日期

        String format1 = format.format(date1);
        return format1;
    }
    //判断 时间格式
    public static boolean isLegalDate(String sDate) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取两个日期之间的所有日期列表(包含开始与结束日期)
     *
     * @param startDt 开始日期(格式必须与pattern一致)
     * @param endDt   结束日期 (格式必须与pattern一致)
     * @param pattern 日期时间格式(如:yyyy-MM-dd,MM-dd-yyyy等)
     * @return 日期列表(包含开始与结束日期)
     * @throws ParseException
     */
    public static List<String> getDateStrList(String startDt, String endDt, String pattern) throws ParseException{
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        List<String> dateList = new ArrayList<>();

        dateList.add(startDt);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(sd.parse(startDt));
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(sd.parse(endDt));
        startCalendar.add(Calendar.DAY_OF_YEAR, 1);
        while (startCalendar.before(endCalendar)) {
            dateList.add(sd.format(startCalendar.getTime()));
            startCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        dateList.add(endDt);
        return dateList;
    }
    /**
     * 获取两个日期之间的所有日期列表
     *
     * @param startDt 开始日期(格式必须与pattern一致)
     * @param endDt   结束日期 (格式必须与pattern一致)
     * @param pattern 日期时间格式(如:yyyy-MM-dd,MM-dd-yyyy等)
     * @param flag   是否包含开始结束
     * @return 日期列表(包含开始与结束日期)
     * @throws ParseException
     */
    public static List<String> getDateStrList(String startDt, String endDt, String pattern,boolean flag) throws ParseException{
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        List<String> dateList = new ArrayList<>();
        if(flag){
            dateList.add(startDt);
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(sd.parse(startDt));
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(sd.parse(endDt));
        startCalendar.add(Calendar.DAY_OF_YEAR, 1);
        while (startCalendar.before(endCalendar)) {
            dateList.add(sd.format(startCalendar.getTime()));
            startCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        if(flag){
            dateList.add(endDt);
        }
        return dateList;
    }
    /**
     * 获取两个日期之间的所有小时列表(不包含开始与结束日期)
     *
     * @param startDt 开始日期(格式必须与pattern一致)
     * @param endDt   结束日期 (格式必须与pattern一致)
     * @param pattern 日期时间格式(如:yyyy-MM-dd,MM-dd-yyyy等)
     * @return 日期列表(包含开始与结束日期)
     * @throws ParseException
     */
    public static List<String> getHoursStrList(String startDt, String endDt, String pattern) throws ParseException{
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        List<String> dateList = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(sd.parse(startDt));
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(sd.parse(endDt));
        startCalendar.add(Calendar.HOUR_OF_DAY, 1);
        while (startCalendar.before(endCalendar)) {
            dateList.add(sd.format(startCalendar.getTime()));
            startCalendar.add(Calendar.HOUR_OF_DAY, 1);
        }
        return dateList;
    }

}