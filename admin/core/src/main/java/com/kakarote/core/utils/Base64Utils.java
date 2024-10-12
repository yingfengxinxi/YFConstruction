package com.kakarote.core.utils;

import com.aliyuncs.utils.StringUtils;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;


public class Base64Utils {

    /**
    * @Description:根据文件路径获取图片Base64码
    * @author shz
    * @date 2022-5-18 15:27
    * @param 
    * @return 
    */
    public static String fileToBase64String(String path) throws IOException{
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        in = new FileInputStream(path);
        data = new byte[in.available()];
        in.read(data);
        in.close();
        return Base64.encodeBase64String(data);
    }
    /**
    * @Description:根据文件获取图片Base64码
    * @author shz
    * @date 2022-8-26 10:50
    * @param 
    * @return 
    */
    public static String fileToBase64String(File file) throws IOException{
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        in = new FileInputStream(file);
        data = new byte[in.available()];
        in.read(data);
        in.close();
        return Base64.encodeBase64String(data);
    }
    
    /**
    * @Description:通过图片Base64码获取图片信息
    * @author shz
    * @date 2022-5-18 15:35
    * @param 
    * @return 
    */
    public static BufferedImage getImageInfo(String chart) throws IOException{
        String base64Image = chart.replaceFirst("data:image/png;base64,", "");
        if(StringUtils.isNotEmpty(chart)){
            byte [] decoder = new BASE64Decoder().decodeBuffer(base64Image);
            InputStream is=new ByteArrayInputStream(decoder);
            return ImageIO.read(is);
        }
        return null;
    }
    public static String getUrlToImgToStr(String imgFile){
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(imgFile);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            // 将内容放到内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        return Base64.encodeBase64String(data.toByteArray());
    }
    public static void base64ToFile(String destPath,String base64, String fileName) {
        File file = null;
        //创建文件目录
        String filePath=destPath;
        File  dir=new File(filePath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        BufferedOutputStream bos = null;
        java.io.FileOutputStream fos = null;
        try {
            byte[] bytes = java.util.Base64.getDecoder().decode(base64);
            file=new File(filePath+"/"+fileName);
            fos = new java.io.FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static InputStream base64ToInputStream(String base64) {
        ByteArrayInputStream stream = null;
        byte[] bytes = java.util.Base64.getDecoder().decode(base64);
        stream = new ByteArrayInputStream(bytes);
        return stream;
    }


    public static void main(String[] args)  throws IOException{
        System.out.println(getUrlToImgToStr("https://p0.ssl.img.360kuai.com/t01f4443e0b94251c30.webp"));
    }
}
