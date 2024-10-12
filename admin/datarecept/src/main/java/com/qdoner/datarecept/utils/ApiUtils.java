package com.qdoner.datarecept.utils;

import com.kakarote.core.exception.CrmException;
import com.qdoner.datarecept.entity.PO.ProjectInfo;
import com.qdoner.datarecept.service.IProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ApiUtils {

    @Autowired
    private IProjectInfoService projectInfoService;

    //初始化数据校验 Map数据源
    public ProjectInfo dataValidation(Map<String, Object> parpam){
        Map<String, Object> parpamData = new HashMap<String, Object>();
        if (parpam.get("projectId") == null || "".equals(parpam.get("projectId"))) {
            throw new CrmException(500, "项目id不能为空");
        }
        // 判断项目id是否存在
        Integer projectId = Integer.valueOf((String) parpam.get("projectId"));
        parpamData.put("projectId",projectId);
        List<ProjectInfo> proList = projectInfoService.projectInfoListGetJk(parpamData);
        if (proList == null || proList.size() <= 0) {
            throw new CrmException(500, "项目id：" + projectId + "不存在,请项目施工单位先在住建委云平台申请后再进行数据推送");
        }
        Object otime= parpam.get("time");
        if (otime == null || "".equals(otime)) {
            throw new CrmException(500, "time不能为空");
        }else{
            boolean legalDate = isLegalDate(otime.toString().trim(),"yyyy-MM-dd HH:mm:ss");
            if(!legalDate){
                throw new CrmException(500, "time字段时间格式不正确，应为：yyyy-MM-dd HH:mm:ss");
            }
        }
        return proList.get(0);
    }


    //日期类数据校验 Map 数据源   key 校验字段   msg 回执描述的字段名称   format 校验的时间格式
    public void dateValidation(Map<String, Object> parpam,String key ,String msg,String format){
        if (parpam.get(key) != null && !"".equals(parpam.get(key))) {
            Object otime= parpam.get(key);
            boolean legalDate = isLegalDate(otime.toString().trim(),format);
                if(!legalDate){
                throw new CrmException(500, msg+"字段时间格式不正确，应为"+format);
             }
        }else{
            throw new CrmException(500, msg+"不能为空");
        }
    }

    //判断 时间格式
    private static boolean isLegalDate(String sDate,String format ) {
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 对字节数组字符串进行Base64解码并生成图片 imgStr Base64 码数据   imageName   文件路径与文件名称  例：F:\\2.jpg
     */
    public static Long GenerateImage(String imgStr, String imageName) { // 对字节数组字符串进行Base64解码并生成图片
        Long size =0L;
        if (imgStr == null) // 图像数据为空
            return size;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
            String imgFilePath = imageName;// 新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            File f= new File(imageName);
            if (f.exists() && f.isFile()){
                size=f.length();
            }
            return size;
        } catch (Exception e) {
            return size;
        }

    }

    /**
     * 将base64字符串，生成文件，并返回存储路径
     */
    public static Map<String,Object> convertBase64ToFile(String fileBase64String, String filePath, String fileName) {
        Map<String, Object> fileData = new HashMap<String, Object>();
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            String PictureType = getBase64PictureType(fileBase64String);
            if (PictureType.length() == 3) {
                fileBase64String = fileBase64String.substring(22, fileBase64String.length());
            } else {
                fileBase64String = fileBase64String.substring(23, fileBase64String.length());
            }
            // System.out.println("截取之后的base64==="+fileBase64String);
            File dir = new File(filePath);
            if (!dir.exists()) {// 判断文件目录是否存在
                System.out.println("//不存在");
                dir.mkdirs();
            }else {
                System.out.println("//目录存在");
            }
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bfile = decoder.decodeBuffer(fileBase64String);
            String PATH = filePath + fileName + "." + PictureType;
            System.out.println(PATH);
            file = new File(PATH);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
            Long size =0L;
            if (file.exists() && file.isFile()){
                size=file.length();
            }
            fileData.put("imgName",fileName + "." + PictureType);
            fileData.put("path",PATH);
            fileData.put("size",size);
            return fileData;
        } catch (Exception e) {
            e.printStackTrace();
            return fileData;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static String getBase64PictureType(String str) {
        String[] head = str.split(",");
        String[] head2 = head[0].split(";");
        String PictureType = head2[0].substring(11, head2[0].length());
        return PictureType;
    }

    //品茗接口或逆运算
    public static List<Integer> orInverseRun(Integer alarm){
        char[] aaa = Integer.toBinaryString(alarm).toCharArray();
        List<Integer> list = new ArrayList<>();
        int j = 0;
        for (int i = aaa.length - 1; i >= 0; i--) {
            if(aaa[i] == '1'){
                Double c=Math.pow(2, j);
                list.add(c.intValue());
            }
            j++;
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println(5 | 3);
    }
}
