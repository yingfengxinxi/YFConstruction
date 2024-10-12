package com.kakarote.build.wifi.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceRecord;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.utils.ApiUtils;
import com.kakarote.build.wifi.entity.BO.WifiQuestionsBO;
import com.kakarote.build.wifi.entity.PO.WifiQuestOption;
import com.kakarote.build.wifi.entity.PO.WifiQuestions;
import com.kakarote.build.wifi.mapper.WifiQuestionsMapper;
import com.kakarote.build.wifi.service.IWifiQuestOptionService;
import com.kakarote.build.wifi.service.IWifiQuestionsService;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * wifi教育答题--问题表 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@Service
public class WifiQuestionsServiceImpl extends BaseServiceImpl<WifiQuestionsMapper, WifiQuestions> implements IWifiQuestionsService {

    @Autowired
    private IWifiQuestOptionService iWifiQuestOptionService;

    @Override
    public BasePage<Map<String,Object>> selectList(WifiQuestionsBO wifiQuestionsBO) {
        wifiQuestionsBO.setOrdersBd("a.create_time",false);
        return getBaseMapper().selectList(wifiQuestionsBO.parse(),wifiQuestionsBO);
    }

    @Override
    public List<Map<String, Object>> TestPaperQue(Map<String, Object> map) {
        return getBaseMapper().TestPaperQue(map);
    }

    @Override
    public int TestPaperQueCount(Map<String, Object> map) {
        return getBaseMapper().TestPaperQueCount(map);
    }

    @Override
    public JSONObject excelImport(MultipartFile file,String typeId) throws Exception{
        List<WifiQuestions> listAdd = new ArrayList<>();
        List<WifiQuestOption> listAddZI = new ArrayList<>();

        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());

        List<Map<String, Object>> list = reader.read(1, 2, Integer.MAX_VALUE);

        for(int i= 0; i < list.size(); i++){

            Map<String, Object> map = list.get(i);

            WifiQuestions wifiQuestions = new WifiQuestions();

            //试题类别
            if (typeId == null || typeId.equals("")) {
                throw new CrmException(1001, "请选择试题类别");
            }

            //题型
            Object OquestType = map.get("题型(1:单选，2:多选，3:填空，4:问答)(*)");
            if (OquestType == null || OquestType.equals("")) {
                throw new CrmException(1001, "题型不能为空");
            }
            String questType = "";
            if(OquestType instanceof String){
                questType = (String)OquestType;
            }else if(OquestType instanceof Long){
                Long a = (Long)OquestType;
                questType = String.valueOf(a);
            }else{
                throw new CrmException(1001, "题型数据类型不正确");
            }


            //试题内容
            Object OquestContent = map.get("试题内容(*)");
            if (OquestContent == null || OquestContent.equals("")) {
                throw new CrmException(1001, "试题内容不能为空");
            }
            String questContent = "";
            if(OquestContent instanceof String){
                questContent = (String)OquestContent;
            }else if(OquestContent instanceof Long){
                Long a = (Long)OquestContent;
                questContent = String.valueOf(a);
            }else{
                throw new CrmException(1001, "试题内容数据类型不正确");
            }

            //试题详解
            Object OquestExplanation = map.get("试题详解(*)");
            if (OquestExplanation == null || OquestExplanation.equals("")) {
                throw new CrmException(1001, "试题详解不能为空");
            }
            String questExplanation = "";
            if(OquestExplanation instanceof String){
                questExplanation = (String)OquestExplanation;
            }else if(OquestExplanation instanceof Long){
                Long a = (Long)OquestExplanation;
            }else{
                throw new CrmException(1001, "试题详解数据类型不正确");
            }

            String id = UUID.randomUUID().toString();

            //子表
            int sizeZ = (map.size()-3)/2;
            if(sizeZ > 0){
                for(int i2 = 1; i2 <= sizeZ; i2++){
                    WifiQuestOption wifiQuestOption = new WifiQuestOption();
                    Object OanswerFlag = map.get("选项(A、B...)及答案标记(1:是;0:否)-" + i2);
                    Object Ooptions = map.get("试题选项-" + i2);
                    if((OanswerFlag == null || OanswerFlag.equals("")) && (Ooptions == null || Ooptions.equals(""))){
                        continue;
                    }

                    if((Ooptions != null && !Ooptions.equals("")) && (OanswerFlag == null || OanswerFlag.equals(""))){
                        throw new CrmException(1001, "选项(A、B...)及答案标记(1:是;0:否)-"+i2 + "不能为空");
                    }
                    if((OanswerFlag != null && !OanswerFlag.equals("")) && (Ooptions == null || Ooptions.equals(""))){
                        throw new CrmException(1001, "试题选项-"+i2 + "不能为空");
                    }

                    String answerFlag = "";
                    String options = "";
                    if(OanswerFlag instanceof String){
                        answerFlag = (String)OanswerFlag;
                    }else{
                        throw new CrmException(1001, "选项(A、B...)及答案标记(1:是;0:否)-"+i2 + "格式不正确，正确格式例如=> A:1");
                    }

                    if(Ooptions instanceof String){
                        options = (String)Ooptions;
                    }else if(Ooptions instanceof Long){
                        Long a = (Long)Ooptions;
                        options = String.valueOf(a);
                    }else{
                        options = String.valueOf(Ooptions);
                        //throw new CrmException(1001, "试题选项-"+i2 + "数据类型不正确");
                    }

                    String[] split = answerFlag.split(":");
                    if(split.length != 2){
                        throw new CrmException(1001, "选项(A、B...)及答案标记(1:是;0:否)-"+i2 + "格式不正确，正确格式例如=> A:1");
                    }

                    wifiQuestOption.setAnswerFlag(split[0]);//试题库选项标记
                    wifiQuestOption.setOptionsFlag(split[1]);//答案标记
                    wifiQuestOption.setOptions(options);
                    wifiQuestOption.setQuestId(id);

                    listAddZI.add(wifiQuestOption);
                }
            }

            wifiQuestions.setId(id);
            wifiQuestions.setTypeId(typeId);//试题类别
            wifiQuestions.setQuestType(questType);//题型(1:单选，2:多选，3:填空，4:问答)
            wifiQuestions.setQuestContent(questContent);//试题内容
            wifiQuestions.setQuestExplanation(questExplanation);//试题详解

            listAdd.add(wifiQuestions);

        }
        //保存主表
        saveBatch(listAdd);
        //保存子表
        iWifiQuestOptionService.saveBatch(listAddZI);


        JSONObject result = new JSONObject().fluentPut("totalSize", listAdd.size()).fluentPut("errSize", 0);

        return result;
    }

    @Override
    public void deleteQuestionById(String id) {
        getBaseMapper().deleteQuestionById(id);
    }


    /**
     * excel导入试题本地使用版（测试）
     *
     * @param file file
     */
    @Override
    public JSONObject excelImport2(MultipartFile file,String typeId) throws Exception{
        List<WifiQuestions> listAdd = new ArrayList<>();
        List<WifiQuestOption> listAddZI = new ArrayList<>();

        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());

        List<Map<String, Object>> list = reader.read(1, 2, Integer.MAX_VALUE);

        for(int i= 0; i < list.size(); i++){

            Map<String, Object> map = list.get(i);

            WifiQuestions wifiQuestions = new WifiQuestions();

            //试题类别
            if (typeId == null || typeId.equals("")) {
                throw new CrmException(1001, "请选择试题类别");
            }

            //题型
            Object OquestType = map.get("题型(1:单选，2:多选，3:填空，4:问答)(*)");
            if (OquestType == null || OquestType.equals("")) {
                throw new CrmException(1001, "题型不能为空");
            }
            String questType = "";
            if(OquestType instanceof String){
                questType = (String)OquestType;
            }else if(OquestType instanceof Long){
                Long a = (Long)OquestType;
                questType = String.valueOf(a);
            }else{
                throw new CrmException(1001, "题型数据类型不正确");
            }

            //试题内容
            Object OquestContent = map.get("试题内容(*)");
            if (OquestContent == null || OquestContent.equals("")) {
                throw new CrmException(1001, "试题内容不能为空");
            }
            String questContent = "";
            if(OquestContent instanceof String){
                questContent = (String)OquestContent;
            }else if(OquestContent instanceof Long){
                Long a = (Long)OquestContent;
                questContent = String.valueOf(a);
            }else{
                throw new CrmException(1001, "试题内容数据类型不正确");
            }

            //试题详解
            Object OquestExplanation = map.get("试题详解(*)");
            if (OquestExplanation == null || OquestExplanation.equals("")) {
                throw new CrmException(1001, "试题详解不能为空");
            }
            String questExplanation = "";
            if(OquestExplanation instanceof String){
                questExplanation = (String)OquestExplanation;
            }else if(OquestExplanation instanceof Long){
                Long a = (Long)OquestExplanation;
            }else{
                throw new CrmException(1001, "试题详解数据类型不正确");
            }
            //答案
            Object OquestAnswerList = map.get("答案(*)");
            if(OquestAnswerList == null || OquestAnswerList.equals("")){
                throw new CrmException(1001, "答案不能为空");
            }

            String id = UUID.randomUUID().toString();
            String questExplanationStr[] ;
            String explanationStr="";  //正确答案的选项 单选时无需在处理去除空格直接比对即可   多选需要在进行下分
            questExplanationStr= questExplanation.split("正确答案：");
            if(questExplanationStr.length>1){
                explanationStr =questExplanationStr[1];
                explanationStr=explanationStr.trim();
            }else{
                throw new CrmException(1001, "试题详解数据类型不正确");
            }

            String answerList =(String)OquestAnswerList;//答案的大文本需要处理成对应答案
            List<String> listDa=new ArrayList();
            String a=answerList.split("A．")[1];
            String b=answerList.split("B．")[1];
            String c=answerList.split("C．")[1];
            String d=answerList.split("D．")[1];
            String wentiA=a.split("B．")[0];
            listDa.add(wentiA);
            String wentiB=b.split("C．")[0];
            listDa.add(wentiB);
            String wentiC=c.split("D．")[0];
            listDa.add(wentiC);
            listDa.add(d);
            for(int k=0;k<listDa.size();k++){
                WifiQuestOption wifiQuestOption = new WifiQuestOption();
                if("1".equals(questType)){  //单选
                    if("A".equals(explanationStr)&& k==0){
                        wifiQuestOption.setOptionsFlag("1");//答案标记
                    }else if("B".equals(explanationStr)&& k==1){
                        wifiQuestOption.setOptionsFlag("1");//答案标记
                    }else if("C".equals(explanationStr)&& k==2){
                        wifiQuestOption.setOptionsFlag("1");//答案标记
                    }else if("D".equals(explanationStr)&& k==3){
                        wifiQuestOption.setOptionsFlag("1");//答案标记
                    }else{
                        wifiQuestOption.setOptionsFlag("0");//答案标记
                    }
                }else if("2".equals(questType)){//多选

                }
                if(k==0){
                    wifiQuestOption.setAnswerFlag("A");//试题库选项标记
                }else if(k==1){
                    wifiQuestOption.setAnswerFlag("B");//试题库选项标记
                }else if(k==2){
                    wifiQuestOption.setAnswerFlag("C");//试题库选项标记
                }else if(k==3){
                    wifiQuestOption.setAnswerFlag("D");//试题库选项标记
                }
                wifiQuestOption.setOptions(listDa.get(k));
                wifiQuestOption.setQuestId(id);
                listAddZI.add(wifiQuestOption);
            }
            wifiQuestions.setId(id);
            wifiQuestions.setTypeId(typeId);//试题类别
            wifiQuestions.setQuestType(questType);//题型(1:单选，2:多选，3:填空，4:问答)
            wifiQuestions.setQuestContent(questContent);//试题内容
            wifiQuestions.setQuestExplanation(questExplanation);//试题详解

            listAdd.add(wifiQuestions);

        }
        //保存主表
        saveBatch(listAdd);
        //保存子表
        iWifiQuestOptionService.saveBatch(listAddZI);


        JSONObject result = new JSONObject().fluentPut("totalSize", listAdd.size()).fluentPut("errSize", 0);

        return result;
    }

    /**
     * excel导入试题本地使用第三版（测试）
     *
     * @param file file
     */
    @Override
    public JSONObject excelImport3(MultipartFile file,String typeId) throws Exception{
        List<WifiQuestions> listAdd = new ArrayList<>();
        List<WifiQuestOption> listAddZI = new ArrayList<>();

        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());

        List<Map<String, Object>> list = reader.read(1, 2, Integer.MAX_VALUE);

        for(int i= 0; i < list.size(); i++){

            Map<String, Object> map = list.get(i);

            WifiQuestions wifiQuestions = new WifiQuestions();

            //试题类别
            if (typeId == null || typeId.equals("")) {
                throw new CrmException(1001, "请选择试题类别");
            }

            //题型
            Object OquestType = map.get("题型(1:单选，2:多选，3:填空，4:问答)(*)");
            if (OquestType == null || OquestType.equals("")) {
                throw new CrmException(1001, "题型不能为空");
            }
            String questType = "";
            if(OquestType instanceof String){
                questType = (String)OquestType;
            }else if(OquestType instanceof Long){
                Long a = (Long)OquestType;
                questType = String.valueOf(a);
            }else{
                throw new CrmException(1001, "题型数据类型不正确");
            }

            //试题内容
            Object OquestContent = map.get("试题内容(*)");
            if (OquestContent == null || OquestContent.equals("")) {
                throw new CrmException(1001, "试题内容不能为空");
            }
            String questContent = "";
            if(OquestContent instanceof String){
                questContent = (String)OquestContent;
            }else if(OquestContent instanceof Long){
                Long a = (Long)OquestContent;
                questContent = String.valueOf(a);
            }else{
                throw new CrmException(1001, "试题内容数据类型不正确");
            }
            String answerList=questContent.split("A．")[1];
            questContent=questContent.split("A．")[0];
            String explanationStr=answerList.split("正确答案:")[1]; //正确答案的选项 单选时无需在处理去除空格直接比对即可   多选需要在进行下分
            answerList=answerList.split("正确答案:")[0];
            String id = UUID.randomUUID().toString();
            List<String> explanationStrList =new ArrayList<String>();
            if("2".equals(questType)){ //对多选的正确答案进行处理筛选
                explanationStr=explanationStr.trim();
                for(int j=0;j<explanationStr.length();j++){
                    explanationStrList.add(explanationStr.substring(j,j+1));
                }
            }

            List<String> listDa=new ArrayList();
            String b=answerList.split("B．")[1];
            String c=answerList.split("C．")[1];
            String d=answerList.split("D．")[1];
            String wentiA=answerList.split("B．")[0];
            listDa.add(wentiA);
            String wentiB=b.split("C．")[0];
            listDa.add(wentiB);
            String wentiC=c.split("D．")[0];
            listDa.add(wentiC);
            if("1".equals(questType)){
                listDa.add(d); //单选的D答案
            }else if("2".equals(questType)){
                listDa.add(d.split("E．")[0]);  //多选的D答案
                listDa.add(d.split("E．")[1]);  //多选的E答案
            }else{
                listDa.add(d);
            }

            for(int k=0;k<listDa.size();k++){
                WifiQuestOption wifiQuestOption = new WifiQuestOption();
                if("1".equals(questType)){  //单选
                    if("A".equals(explanationStr)&& k==0){
                        wifiQuestOption.setOptionsFlag("1");//答案标记
                    }else if("B".equals(explanationStr)&& k==1){
                        wifiQuestOption.setOptionsFlag("1");//答案标记
                    }else if("C".equals(explanationStr)&& k==2){
                        wifiQuestOption.setOptionsFlag("1");//答案标记
                    }else if("D".equals(explanationStr)&& k==3){
                        wifiQuestOption.setOptionsFlag("1");//答案标记
                    }else{
                        wifiQuestOption.setOptionsFlag("0");//答案标记
                    }
                }else if("2".equals(questType)){//多选
                    wifiQuestOption.setOptionsFlag("0");//答案标记
                    for(int j=0;j<explanationStrList.size();j++){
                        if("A".equals(explanationStrList.get(j))&& k==0){
                            wifiQuestOption.setOptionsFlag("1");//答案标记
                        }else if("B".equals(explanationStrList.get(j))&& k==1){
                            wifiQuestOption.setOptionsFlag("1");//答案标记
                        }else if("C".equals(explanationStrList.get(j))&& k==2){
                            wifiQuestOption.setOptionsFlag("1");//答案标记
                        }else if("D".equals(explanationStrList.get(j))&& k==3){
                            wifiQuestOption.setOptionsFlag("1");//答案标记
                        }else if("E".equals(explanationStrList.get(j))&& k==4){
                            wifiQuestOption.setOptionsFlag("1");//答案标记
                        }
                    }
                }
                if(k==0){
                    wifiQuestOption.setAnswerFlag("A");//试题库选项标记
                }else if(k==1){
                    wifiQuestOption.setAnswerFlag("B");//试题库选项标记
                }else if(k==2){
                    wifiQuestOption.setAnswerFlag("C");//试题库选项标记
                }else if(k==3){
                    wifiQuestOption.setAnswerFlag("D");//试题库选项标记
                }else if(k==4){
                    wifiQuestOption.setAnswerFlag("E");//试题库选项标记
                }
                wifiQuestOption.setOptions(listDa.get(k));
                wifiQuestOption.setQuestId(id);
                listAddZI.add(wifiQuestOption);
            }
            wifiQuestions.setId(id);
            wifiQuestions.setTypeId(typeId);//试题类别
            wifiQuestions.setQuestType(questType);//题型(1:单选，2:多选，3:填空，4:问答)
            wifiQuestions.setQuestContent(questContent);//试题内容
            wifiQuestions.setQuestExplanation("正确答案："+explanationStr);//试题详解

            listAdd.add(wifiQuestions);

        }
        //保存主表
        saveBatch(listAdd);
        //保存子表
        iWifiQuestOptionService.saveBatch(listAddZI);


        JSONObject result = new JSONObject().fluentPut("totalSize", listAdd.size()).fluentPut("errSize", 0);

        return result;
    }

    public static void main(String[] args) {
        List<String> explanationStrList =new ArrayList<String>();
        String explanationStr ="ABE";
        for(int j=0;j<explanationStr.length();j++){
            explanationStrList.add(explanationStr.substring(j,j+1));
        }
        System.out.println(explanationStrList);

    }
}
