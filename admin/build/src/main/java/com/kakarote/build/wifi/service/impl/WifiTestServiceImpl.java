package com.kakarote.build.wifi.service.impl;


import cn.hutool.core.util.IdUtil;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.mapper.LabourInfocollectionMapper;
import com.kakarote.build.labour.service.ILabourInfocollectionService;
import com.kakarote.build.wifi.entity.BO.RetireTestOptionBO;
import com.kakarote.build.wifi.entity.BO.WifiTestBO;
import com.kakarote.build.wifi.entity.BO.WifiTestPaperRulesBO;
import com.kakarote.build.wifi.entity.PO.RetireTestQuest;
import com.kakarote.build.wifi.entity.PO.WifiTest;
import com.kakarote.build.wifi.entity.PO.WifiTestPaperRules;
import com.kakarote.build.wifi.mapper.WifiTestMapper;
import com.kakarote.build.wifi.service.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 生成试卷 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-05-25
 */
@Service
public class WifiTestServiceImpl extends BaseServiceImpl<WifiTestMapper, WifiTest> implements IWifiTestService {

    @Autowired
    private IWifiTestPaperRulesService IWifiTestPaperRulesService;
    @Autowired
    private IWifiTestPaperRulesDelService IWifiTestPaperRulesDelService;
    @Autowired
    private IRetireTestQuestService IRetireTestQuestService;
    @Autowired
    private IRetireTestOptionService IRetireTestOptionService;
    @Autowired
    private IWifiQuestionsService IWifiQuestionsService;
    @Autowired
    private IWifiQuestOptionService IWifiQuestOptionService;

    @Autowired
    private LabourInfocollectionMapper labourInfocollectionMapper;

    @Override
    public Map<String, Object> createTestPaper(WifiTestBO wifiTestBO) throws Exception {
        //返回数据整理
        Map<String, Object> return_map = new HashMap<>();
        LabourInfocollection labourInfocollection = labourInfocollectionMapper.getInfocollectionByNameAndPhone(wifiTestBO.getAnswerName(),wifiTestBO.getAnswerPhone());
        if(labourInfocollection == null){
            throw new CrmException(1004, "您的手机号未进行认证，请联系管理员进行信息采集");
        }
            //查询当前人员工种对应的试卷生成规则
            WifiTestPaperRulesBO WTPRB = new WifiTestPaperRulesBO();
            WTPRB.setWorkTypeId(wifiTestBO.getWorkTypeId());
            WTPRB.setTenantId(Integer.valueOf(UserUtil.getUser().getTenantId().toString()));
            //获取试卷配置规则
            WifiTestPaperRules TestPaperRule = IWifiTestPaperRulesService.getTestPaperByWorkTypeId(WTPRB);
            if (TestPaperRule != null) {
                boolean flag=true;
                //获取试卷配置规则详情
                List<Map<String, Object>> TestPaperRuleDetail = IWifiTestPaperRulesDelService.getRuleDetail(TestPaperRule);
                //试卷id
                String TestpaperId = IdUtil.simpleUUID();
                for (Map<String, Object> TPRD : TestPaperRuleDetail) {
                    int questionsScore = Integer.parseInt(String.valueOf(TPRD.get("questScore")));//试题分值
                    Map<String, Object> map = new HashMap();
                    map.put("questionsType", TPRD.get("typeId"));//试卷类型
                    map.put("TestPaperType", TPRD.get("questType"));//题型
                    map.put("questionsNum", TPRD.get("questCount"));//试题数量
                    int count = IWifiQuestionsService.TestPaperQueCount(map);
                    if(count==0){
                        flag=false;
                    }else{
                        flag=true;
                    }
                    int[] result = randomCommon(1, count + 1, (Integer.parseInt(String.valueOf(TPRD.get("questCount"))) > count ? count : Integer.parseInt(String.valueOf(TPRD.get("questCount")))));
                    List<Map<String, Object>> TestPaperQue = IWifiQuestionsService.TestPaperQue(map);
                    List<Map<String, Object>> TestPaperQue2 = new ArrayList<>();
                    for (int i = 0; i < TestPaperQue.size(); i++) {
                        int temp = i;
                        for (int j = 0; j < result.length; j++) {
                            if ((temp + 1) == result[j]) {
                                TestPaperQue2.add(TestPaperQue.get(i));
                                break;
                            }
                        }
                    }
                    for (Map<String, Object> TPQ : TestPaperQue2) {
                        String questionId = IdUtil.simpleUUID();
                        TPQ.put("newId", questionId);
                        TPQ.put("questionsScore", questionsScore);
                        TPQ.put("TestpaperId", TestpaperId);
                        TPQ.put("createBy", labourInfocollection.getId());
                        TPQ.put("createTime", new Date());
                        TPQ.put("projectId",labourInfocollection.getProjectId());
                        //插入问题
                        IRetireTestQuestService.insertQue(TPQ);
                        String queId = String.valueOf(TPQ.get("id"));
                        map.clear();
                        map.put("queId", queId);
                        List<Map<String, Object>> TestPaperOpt = IWifiQuestOptionService.TestPaperOpt(map);
                        for (Map<String, Object> TPO : TestPaperOpt) {
                            //插入选项
                            TPO.put("newId", IdUtil.simpleUUID());
                            TPO.put("queId", questionId);
                            TPO.put("createBy", labourInfocollection.getId());
                            TPO.put("createTime", new Date());
                            TPO.put("projectId",labourInfocollection.getProjectId());
                            IRetireTestOptionService.insertOption(TPO);
                        }
                    }
                }
                if(TestPaperRuleDetail.size()!=0){
                    if(flag){
//生成试卷
                        WifiTest WT_insert = new WifiTest();
                        WT_insert.setTestId(TestpaperId);
                        WT_insert.setCreateBy(Long.valueOf(labourInfocollection.getId()));
                        WT_insert.setProjectId(labourInfocollection.getProjectId());
                        WT_insert.setTenantId(labourInfocollection.getTenantId());
                        WT_insert.setCreateTime(new Date());
                        WT_insert.setPaperRulesId(TestPaperRule.getId());
                        WT_insert.setWorkTypeId(String.valueOf(wifiTestBO.getWorkTypeId()));
                        WT_insert.setPaperScore(TestPaperRule.getPaperScore());
                        WT_insert.setPaperCount(TestPaperRule.getPaperCount());
                        WT_insert.setAnswerName(wifiTestBO.getAnswerName());
                        WT_insert.setAnswerPhone(wifiTestBO.getAnswerPhone());
                        WT_insert.setPastScope(TestPaperRule.getPastScope());
                        getBaseMapper().insert(WT_insert);
                        //查询试卷
                        Map<String, Object> map = new HashMap<>();
                        map.put("testId", TestpaperId);
                        Map<String, Object> WT = getBaseMapper().MySelectById(map);
                        //查询问题及选项
                        List<Map<String, Object>> RTQ = IRetireTestQuestService.getAllTestQuest(map);
                        for (Map<String, Object> rtq : RTQ) {
                            Map<String, Object> map2 = new HashMap<>();
                            map2.put("questId", String.valueOf(rtq.get("id")));
                            List<Map<String, Object>> RTO = IRetireTestOptionService.getAllTestOption(map2);
                            rtq.put("problem", RTO);
                        }
                        return_map.put("TestPaper", WT);
                        return_map.put("checkboxData", RTQ);
                    }else{
                        throw new CrmException(1003, "试题库中未找到对应规则的试题");
                    }
                }else{
                    throw new CrmException(1002, "当前工种使用规则详情为空，试卷无法生成");
                }
            } else {
                throw new CrmException(1001, "当前工种无对应规则");
            }
        return return_map;
    }

    @Override
    public Map<String, Object> SubmitTestPaper(Map<String, Object> map) {
        Map<String, Object> TestPaper = (Map) map.get("TestPaper");
        List<Map<String, Object>> checkboxData = (List) map.get("checkboxData");
        String testId = String.valueOf(TestPaper.get("testId"));
        int quest_score = 0;
        for (Map<String, Object> prarm : checkboxData) {
            String if_true = "";//问题回答是否正确
            String true_option = "";//正确选项
            List<Map<String, Object>> problem = (List) prarm.get("problem");
            for (Map<String, Object> option : problem) {
                RetireTestOptionBO RTOB = new RetireTestOptionBO();
                if ("true".equals(String.valueOf(option.get("zt")))) {
                    RTOB.setId(String.valueOf(option.get("id")));
                    RTOB.setIfUserOption(String.valueOf(1));
                } else {
                    RTOB.setId(String.valueOf(option.get("id")));
                    RTOB.setIfUserOption(String.valueOf(0));
                }
                //更新试题选项，用户是否选择
                IRetireTestOptionService.MyUpdateById(RTOB);
                //判断用户选择是否正确
                if (!"0".equals(if_true)) {
                    String zt = "true".equals(String.valueOf(option.get("zt"))) ? "1" : "0";
                    if (zt.equals(String.valueOf(option.get("dc")))) {
                        if_true = "1";
                    } else {
                        if_true = "0";
                    }
                }
                //获取当前问题的正确答案
                if ("1".equals(String.valueOf(option.get("dc")))) {
                    if ("2".equals(String.valueOf(prarm.get("type")))) {
                        if (true_option == "") {
                            true_option = String.valueOf(option.get("value"));
                        } else {
                            true_option = true_option + "," + String.valueOf(option.get("value"));
                        }
                    } else {
                        true_option = String.valueOf(option.get("value"));
                    }
                }
            }
            //更新当前问题回答结果，以及正确选项
            RetireTestQuest RTQ = new RetireTestQuest();
            RTQ.setId(String.valueOf(prarm.get("id")));
            RTQ.setIfTrue(if_true);
            RTQ.setTrueOption(true_option);
            RTQ.setUpdateBy(UserUtil.getUser().getUserId());
            RTQ.setUpdateTime(new Date());
            IRetireTestQuestService.updateById(RTQ);
            prarm.put("trueOption", true_option);
            prarm.put("duicuo", "1".equals(if_true) ? "true" : "false");
            //计算总得分
            if ("1".equals(if_true)) {
                quest_score = quest_score + Integer.parseInt(String.valueOf(prarm.get("questScore")));
            }
        }
        Map<String, Object> WT2 = getBaseMapper().MySelectById(TestPaper);
        if(quest_score>=Integer.valueOf(String.valueOf(WT2.get("pastScope")))){
            TestPaper.put("TestState","0");//通过
        }else{
            TestPaper.put("TestState","1");//不通过
        }
        //更新总得分
        WifiTest WT = new WifiTest();
        WT.setTestId(testId);
        WT.setAnswerId(String.valueOf(UserUtil.getUser().getUserId()));
        WT.setUpdateBy(UserUtil.getUser().getUserId());
        WT.setUpdateTime(new Date());
        WT.setQuestScore(quest_score);
        getBaseMapper().updateById(WT);
        //返回批阅后的试卷
        TestPaper.put("QuestScore", quest_score);
        map.put("TestPaper", TestPaper);
        map.put("checkboxData", checkboxData);
        return map;
    }

    @Override
    public BasePage<Map<String, Object>> getAllTestPaper(WifiTestBO wifiTestBO) {
        wifiTestBO.setOrdersBd("create_time",false);
        return getBaseMapper().selectTestPaperList(wifiTestBO.parse(), wifiTestBO);
    }

    @Override
    public Map<String, Object> getPaperDetail(String testId) {
        Map<String, Object> map = new HashMap<>();
        map.put("testId", testId);
        Map<String, Object> WT = getBaseMapper().MySelectById(map);
        if(WT!=null){
            if(WT.get("questScore")!=null&&WT.get("pastScope")!=null){
               try{
                   if(Integer.valueOf(String.valueOf(WT.get("questScore")))>=Integer.valueOf(String.valueOf(WT.get("pastScope")))){
                       WT.put("TestState","0");//通过
                   }else{
                       WT.put("TestState","1");//不通过
                   }
               }catch (Exception e){
                   e.printStackTrace();
                   log.error("打印错误：" + e);
                   WT.put("TestState","1");//不通过
               }
            }else{
                WT.put("TestState","1");//不通过
            }
        }
        //查询问题及选项
        List<Map<String, Object>> RTQ = IRetireTestQuestService.getAllTestQuest(map);
        for (Map<String, Object> rtq : RTQ) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("questId", String.valueOf(rtq.get("id")));
            List<Map<String, Object>> RTO = IRetireTestOptionService.getAllTestOption(map2);
            rtq.put("problem", RTO);
        }
        //返回数据整理
        Map<String, Object> return_map = new HashMap<>();
        return_map.put("TestPaper", WT);
        return_map.put("checkboxData", RTQ);
        return return_map;
    }

    /**
     * 教育答题统计
     * @return
     */
    @Override
    public Map<String, Object> getMapByWorkTypeIdToEcharts() {
        Map all = new HashMap();
        //1、工种答题占比分析--饼状图
        List<Map> testWorkTypeId = getBaseMapper().getTestWorkTypeId();

        //2、试题得分分析--按试题类型分类，柱状图代表总分，折线图代表平均分
        List<Map> scopeByTestType = getBaseMapper().getScopeByTestType();
        List allScopeList = new ArrayList();
        List avgScopList = new ArrayList();
        List typeNameList = new ArrayList();
        Map scopeByTestType2 = new HashMap();
        for(Map map:scopeByTestType){
//            Integer allScope = Integer.valueOf(String.valueOf(map.get("allScope")));
//            Integer avgScop = Integer.valueOf(String.valueOf(map.get("avgScop")));
            String typeName = String.valueOf(map.get("typeName"));
            allScopeList.add(map.get("allScope"));
            avgScopList.add(map.get("avgScop"));
            typeNameList.add(typeName);

        }
        scopeByTestType2.put("allScope",allScopeList);
        scopeByTestType2.put("typeNameList",typeNameList);
        scopeByTestType2.put("avgScopList",avgScopList);

        //3、答题人员排名，前10--横向柱状图
        List<Map> scopeByTen = getBaseMapper().getScopeByTen();
        List peopleNameList = new ArrayList();
        List scopeList = new ArrayList();
        Map scopeByTen2 = new HashMap();
        for(Map map:scopeByTen){
//            Integer questScore = Integer.valueOf(String.valueOf(map.get("questScore")));
            String answerName = String.valueOf(map.get("answerName"));

            peopleNameList.add(answerName);
            scopeList.add(map.get("questScore"));

        }
        scopeByTen2.put("peopleNameList",peopleNameList);
        scopeByTen2.put("scopeList",scopeList);

        //4、视频观看次数占比--饼状图
        List<Map> videoWatchCounts = getBaseMapper().getVideoWatchCounts();

        all.put("echartsWorkTypeList",testWorkTypeId);
        all.put("scopeByTestType",scopeByTestType2);
        all.put("scopeByTen",scopeByTen2);
        all.put("videoWatchCounts",videoWatchCounts);
        return all;
    }

    public static int[] randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }

}
