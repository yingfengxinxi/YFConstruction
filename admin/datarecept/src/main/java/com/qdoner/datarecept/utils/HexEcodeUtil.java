package com.qdoner.datarecept.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 16进制转换工具
 */
@Slf4j
public class HexEcodeUtil {

    //16进制数字字符集
    public static final String HEXMAXSTRING = "0123456789ABCDEF";
    public static final String HEXMINSTRING = "0123456789abcdef";

    /**
     * byte[]转16进制Str
     *
     * @param byteArray
     */
    public static String ByteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null)
            return null;
        char[] hexArray = HEXMAXSTRING.toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int i = 0; i < byteArray.length; i++) {
            int temp = byteArray[i] & 0xFF;
            hexChars[i * 2] = hexArray[temp >>> 4];
            hexChars[i * 2 + 1] = hexArray[temp & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Str转16进制Str
     *
     * @param str
     * @return
     */
    public static String StrToHexStr(String str) {
        //根据默认编码获取字节数组
        byte[] bytes = str.getBytes();
        StringBuilder stringBuilder = new StringBuilder(bytes.length * 2);
        //将字节数组中每个字节拆解成2位16进制整数
        for (int i = 0; i < bytes.length; i++) {
            stringBuilder.append("0x");
            stringBuilder.append(HEXMAXSTRING.charAt((bytes[i] & 0xf0) >> 4));
            stringBuilder.append(HEXMAXSTRING.charAt((bytes[i] & 0x0f) >> 0));
            //去掉末尾的逗号
            if (i != bytes.length - 1)
                stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }

    /**
     * 16进制Str转byte[]
     *
     * @param hexStr 不带空格、不带0x、不带逗号的16进制Str,如:06EEF7F1
     * @return
     */
    public static byte[] HexStrToByteArray(String hexStr) {
        byte[] byteArray = new byte[hexStr.length() / 2];
        for (int i = 0; i < byteArray.length; i++) {
            String subStr = hexStr.substring(2 * i, 2 * i + 2);
            byteArray[i] = ((byte) Integer.parseInt(subStr, 16));
        }
        return byteArray;
    }

    /**
     * 温度湿度数据转换
     */
    public static Map<String, String> HexToRead(String info) {
        HashMap<String, String> hashMap = new HashMap<>();

        return hashMap;
    }
    /**
     * @description 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
    /**
     * 将16进制字符串转换为byte数组
     *
     * @param hexItr 16进制字符串
     * @return
     */
    public static byte[] hexItr2Arr(String hexItr) {
        try {
            return Hex.decodeHex(hexItr);
        } catch (DecoderException e) {
            log.info("16进制字符串转byte异常!");
            e.printStackTrace();
        }
        return null;
    }

    static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static String hexEncode(byte[] buffer) {
        return new BigInteger(1, buffer).toString(10);
    }

    public static String toBinary(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++)
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }

    /**
     * 16进制转字符
     * @param s
     * @return
     */
    public static String toStringHex2(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "GBK");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static Date analysisDate(String time) {
        try {
            time = hexadecimalToHexEncode(time.substring(0, 2)) + hexadecimalToHexEncode(time.substring(2, 4)) + hexadecimalToHexEncode(time.substring(4, 6)) + hexadecimalToHexEncode(time.substring(6, 8)) + hexadecimalToHexEncode(time.substring(8, 10)) + hexadecimalToHexEncode(time.substring(10, 12));
            SimpleDateFormat ft = new SimpleDateFormat("yyMMddHHmmss");
            return ft.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取16进制时间
     *
     * @return
     */
    public static String getHexTime(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddHHmmss");
        String dateStr = ft.format(date);
        String hexDateTime = "";
        String year = dateStr.substring(0, 2);
        hexDateTime += ten2Hex(Integer.toHexString(Integer.parseInt(year) & 0XFF));
        String month = dateStr.substring(2, 4);
        hexDateTime += ten2Hex(Integer.toHexString(Integer.parseInt(month) & 0XFF));
        String day = dateStr.substring(4, 6);
        hexDateTime += ten2Hex(Integer.toHexString(Integer.parseInt(day) & 0XFF));//日
        String hou = dateStr.substring(6, 8);
        hexDateTime += ten2Hex(Integer.toHexString(Integer.parseInt(hou) & 0XFF));
        String min = dateStr.substring(8, 10);
        hexDateTime += ten2Hex(Integer.toHexString(Integer.parseInt(min) & 0XFF));
        String sec = dateStr.substring(10, 12);
        hexDateTime += ten2Hex(Integer.toHexString(Integer.parseInt(sec) & 0XFF));
        return hexDateTime;
    }

    /**
     * 不足两位 高位补零
     * @return
     */
    private static String ten2Hex(String str){
        int strLen = str.length();
        if (strLen < 2) {
            while (strLen < 2) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);// 左补0
                // sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }
    public static String hexadecimalToHexEncode(String str) {
        String restr = hexEncode(HexStrToByteArray(str));
        if (restr.length() == 1) {
            restr = "0" + restr;
        }
        return restr;
    }



    /**
     * 将十六进制的字符串转换成二进制的字符串
     *
     * @param hexString
     * @return
     */
    public static String hexStrToBinaryStr(String hexString) {

        if (hexString == null || hexString.equals("")) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        // 将每一个十六进制字符分别转换成一个四位的二进制字符
        for (int i = 0; i < hexString.length(); i++) {
            String indexStr = hexString.substring(i, i + 1);
            String binaryStr = Integer.toBinaryString(Integer.parseInt(indexStr, 16));
            while (binaryStr.length() < 4) {
                binaryStr = "0" + binaryStr;
            }
            sb.append(binaryStr);
        }

        return sb.toString();
    }

    /**
     * 将二进制转换为10进制
     * @param bi ：待转换的二进制
     * @return
     */
    public  static  Integer Biannary2Decimal(int bi){
        String binStr = bi+"";
        Integer sum = 0;
        int len = binStr.length();
        for (int i=1;i<=len;i++){
            //第i位 的数字为：
            int dt = Integer.parseInt(binStr.substring(i-1,i));
            sum+=(int)Math.pow(2,len-i)*dt;
        }
        return  sum;
    }

    public static byte[] hex2byte(String hex) {

        String digital = "0123456789ABCDEF";
        String hex1 = hex.replace(" ", "");
        char[] hex2char = hex1.toCharArray();
        byte[] bytes = new byte[hex1.length() / 2];
        byte temp;
        for (int p = 0; p < bytes.length; p++) {
            temp = (byte) (digital.indexOf(hex2char[2 * p]) * 16);
            temp += digital.indexOf(hex2char[2 * p + 1]);
            bytes[p] = (byte) (temp & 0xff);
        }
        return bytes;
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.formatDateStr(analysisDate("111111111111"),DateUtil.YMDHMS_));
    }
}
