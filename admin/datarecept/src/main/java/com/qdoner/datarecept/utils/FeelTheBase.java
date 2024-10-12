package com.qdoner.datarecept.utils;

import org.springframework.util.StringUtils;

import java.math.BigInteger;

/**
 * 进制转换
 * @author hasee
 *
 */
public class FeelTheBase {


    //將10進制轉換為16進制
    public static String encodeHEX(Integer numb) {

        String hex = Integer.toHexString(numb);
        return hex;

    }

    //將16進制字符串轉換為10進制數字
    public static int decodeHEX(String hexs) {
        BigInteger bigint = new BigInteger(hexs, 16);
        int numb = bigint.intValue();
        return numb;
    }

    public static String bigEndianChangeLittleEndian(String str){
        if(!org.springframework.util.StringUtils.isEmpty(str)){
            byte[] bytes1 = hexStrToByteArrs(str);
            //System.out.println("====="+bytes1.length);
            byte[] bytes2 = new byte[str.length() / 2];
            int index = 0;
            for (int i = bytes1.length - 1; i>=0; i--){
                bytes2[index] = bytes1[i];
                index++;
            }
            return bytesToHexString(bytes2);
        }
        return null;
    }

    public static Byte hexStrToByte(String hexStr){
        if(StringUtils.isEmpty(hexStr)){
            return null;
        }
        return Byte.parseByte(hexStr,16);
    }
    /**
     * hexStr 转 Integer
     * @param hexStr
     * @return
     */
    public static Integer hexStrToInteger(String hexStr){
        if(StringUtils.isEmpty(hexStr)){
            return null;
        }
        return Integer.parseInt(hexStr,16);
    }

    /**
     * hexStr 转 BigInteger
     * @param hexStr
     * @return
     */
    public static BigInteger hexStrToBigInteger(String hexStr){
        if(StringUtils.isEmpty(hexStr)){
            return null;
        }
        return new BigInteger(hexStr,16);
    }

    /**
     * 将十六进制的字符串转换成字节数组
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStrToByteArrs(String hexString) {
        if (StringUtils.isEmpty(hexString)) {
            return null;
        }

        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        int index = 0;

        byte[] bytes = new byte[len / 2];

        while (index < len) {
            String sub = hexString.substring(index, index + 2);
            bytes[index / 2] = (byte) Integer.parseInt(sub, 16);
            index += 2;
        }

        return bytes;
    }

    /**
     * 数组转换成十六进制字符串
     *
     * @param bArray
     * @return HexString
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2){
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static String hexStrToString(String hexStr){
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        String returnStr = new String(bytes);
        return returnStr;
    }

    public static Double hexStrToDouble(String hexStr){
        if(StringUtils.isEmpty(hexStr)){
            return null;
        }
        long longBits = Long.valueOf(hexStr,16).longValue();
        return Double.longBitsToDouble(longBits);
    }

    public static void main(String[] args) {
        String numb = "51EFE32DB8FDE540";
//        int hex = decodeHEX(numb);
//        System.out.println("  9999  的16进制为" + hex);
//        System.out.println("    16進制字符 " + hex + " 的10進制數字為   " + decodeHEX(hex));


        System.out.println(hexStrToDouble(numb));
    }
}