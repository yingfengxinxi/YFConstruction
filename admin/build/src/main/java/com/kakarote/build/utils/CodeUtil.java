package com.kakarote.build.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 生成单据编号
 */
public class CodeUtil {

    private static String parttern_date = "yyyyMMdd";

    private static String parttern_datetime = "yyyy-MM-dd HH:mm:ss";

    /**
     * 根据当前编号获取下一编号
     *
     * @param curCode
     * @return
     */
    public static String getCode(String curCode) {
        String currDateStr = getDateStr(new Date());
        if (curCode == null) {
            return currDateStr + "-001";
        }
        //下一编号
        String nextCode = currDateStr;
        String[] codes = curCode.split("\\-");
        if (currDateStr.equals(codes[0])) {//今日
            int nextNumber = Integer.valueOf(codes[1]) + 1;
            if (nextNumber < 10) {
                nextCode += "-00" + nextNumber;
            } else if (nextNumber < 100) {
                nextCode += "-0" + nextNumber;
            }else{
                nextCode += "-" + nextNumber;
            }
        } else {//今日以前
            nextCode += "-001";
        }
        return nextCode;
    }

    /**
     * 日期转字符串
     *
     * @param date
     * @return
     */
    private static String getDateStr(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(parttern_date);
        return formatter.format(date);
    }

    /**
     * 日期时间转字符串
     *
     * @param date
     * @return
     */
    public static String getDateTimeStr(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(parttern_datetime);
        return formatter.format(date);
    }
}
