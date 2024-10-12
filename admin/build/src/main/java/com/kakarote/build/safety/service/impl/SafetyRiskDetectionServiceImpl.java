package com.kakarote.build.safety.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.build.safety.entity.BO.SafetyRiskDetectionBO;
import com.kakarote.build.safety.entity.PO.SafetyRiskDetection;
import com.kakarote.build.safety.mapper.SafetyRiskDetectionMapper;
import com.kakarote.build.safety.service.ISafetyRiskDetectionService;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.message.MessageEntity;
import com.kakarote.core.message.MessageFeignService;
import com.kakarote.core.message.MessageType;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

/**
 * <p>
 * 安全隐患排查 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2022-06-27
 */
@Service
public class SafetyRiskDetectionServiceImpl extends BaseServiceImpl<SafetyRiskDetectionMapper, SafetyRiskDetection> implements ISafetyRiskDetectionService {

    @Autowired
    private IProjectInfoService projectInfoService;

    @Autowired
    private MessageFeignService messageFeignService;

    @Autowired
    private AdminFileService adminFileService;

    @Override
    public BasePage<SafetyRiskDetection> selectByPage(SafetyRiskDetectionBO riskDetectionBO) {
        riskDetectionBO.setOrdersBd("check_time",false);
        BasePage<SafetyRiskDetection> list = getBaseMapper().selectByPage(riskDetectionBO.parse(), riskDetectionBO);
        return list;
    }

    @Override
    public Result saveInfo(SafetyRiskDetection riskDetection) {
        // 查询当前项目信息
        Long projectId = UserUtil.getUser().getProjectId();
        ProjectInfo projectInfo = projectInfoService.getById(projectId);

        // 设置检查单位和检查单位统一社会信用代码 使用建设单位相关信息
        riskDetection.setExamineUnit(projectInfo.getConstructionUnitName());
        riskDetection.setExamineUnitCode(projectInfo.getConstructionUnitUniscid());

        // 判断是否需要整改 isRectify == 1
        if ("0".equals(riskDetection.getIsRectify())) {
            riskDetection.setInfoState("4");
            boolean result = this.save(riskDetection);
            if (!result) {
                throw new CrmException(500, "保存失败！");
            }
            return R.ok("添加成功");
        }


        boolean result = this.save(riskDetection);
        if (!result) {
            throw new CrmException(500, "保存失败！");
        }

        // 推送消息
        try {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setByUser(UserUtil.getUserId());
            messageEntity.setTitle(riskDetection.getRiskDesc());
            messageEntity.setData(String.valueOf(riskDetection.getId()));
            //List<Long> toUsers = messageFeignService.getQueuesUserList(MessageType.MESSAGE_SECURITY_RISKS);
            List<Long> toUsers = getBaseMapper().getQueuesUserList(riskDetection.getId());
            messageEntity.setToUsers(toUsers);//指定消息推送人
            messageEntity.setCode(MessageType.MESSAGE_SECURITY_RISKS_DETECTION);
            Result sendResult = messageFeignService.send(messageEntity);

            if (sendResult.getCode() != 0) {
                System.out.println("消息推送错误：============" + sendResult.getMsg());
                throw new CrmException(0, "消息推送失败");
            }
            // 成功更新数据状态
            SafetyRiskDetection updateInfo = new SafetyRiskDetection();
            updateInfo.setId(riskDetection.getId());
            updateInfo.setInfoState("1");
            updateById(updateInfo);

        } catch (Exception e) {
            return R.ok("任务下发失败！");
        }


        // 保存数据
        return R.ok("添加成功!");
    }

    @Override
    public boolean updateInfo(SafetyRiskDetection riskDetection) {
        riskDetection.setUpdateTime(new Date());
        riskDetection.setUpdateUserId(UserUtil.getUserId());

        // 如果隐患状态 1：已下发，表示当前操作是整改保存，2：已整改，表示当前操作是复检保存。
        // 0：已检查，需要下发通知
        if ("0".equals(riskDetection.getInfoState())) {

            boolean result = this.updateById(riskDetection);
            if (!result) {
                throw new CrmException(500, "更新失败！");
            }

            // 推送消息
            try {
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setByUser(UserUtil.getUserId());
                messageEntity.setTitle(riskDetection.getRiskDesc());
                messageEntity.setData(String.valueOf(riskDetection.getId()));
                //List<Long> toUsers = messageFeignService.getQueuesUserList(MessageType.MESSAGE_SECURITY_RISKS);
                List<Long> toUsers = getBaseMapper().getQueuesUserList(riskDetection.getId());
                messageEntity.setToUsers(toUsers);//指定消息推送人
                messageEntity.setCode(MessageType.MESSAGE_SECURITY_RISKS_DETECTION);
                Result sendResult = messageFeignService.send(messageEntity);

                if (sendResult.getCode() != 0) {
                    System.out.println("消息推送错误：============" + sendResult.getMsg());
                    throw new CrmException(0, "消息推送失败");
                }

                // 成功更新数据状态
                riskDetection.setId(riskDetection.getId());
                riskDetection.setInfoState("1");

            } catch (Exception e) {
                throw new CrmException(500, "下发失败！");
            }
        } else if ("1".equals(riskDetection.getInfoState())) {
            riskDetection.setInfoState("2");
        } else if ("2".equals(riskDetection.getInfoState())) {
            riskDetection.setInfoState("3");
        }

        return this.updateById(riskDetection);
    }

    @Override
    public Map<String, Object> selectQuestionNumber() {
        Map<String, Object> data = getBaseMapper().selectQuestionNumber();
        return data;
    }

    @Override
    public List<Map<String, Object>> selectSafetyHiddenDanger(Map<String, Object> params) {
        List<Map<String, Object>> list = getBaseMapper().selectSafetyHiddenDanger(params);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectSafetyLevelAnalysis() {
        List<Map<String, Object>> list = getBaseMapper().selectSafetyLevelAnalysis();
        return list;
    }

    @Override
    public List<Map<String, Object>> selectNumByType(Map<String, Object> params) {
        String type = (String) params.get("type");

        if (type == null || type.equals("")) {
            throw new CrmException(500, "查询类型type不能为空，1：查询最近7天；2：查询最近30天；3：查询最近12个月；");
        }

        if (type.equals("1")) {
            params.put("format", "%Y-%m-%d");
            params.put("num", "6");
            params.put("dateType", "day");
        } else if (type.equals("2")) {
            params.put("format", "%Y-%m-%d");
            params.put("num", "29");
            params.put("dateType", "day");
        } else if (type.equals("3")) {
            params.put("format", "%Y-%m");
            params.put("num", "11");
            params.put("dateType", "month");
        }

        List<Map<String, Object>> list = getBaseMapper().selectNumByType(params);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectSafetyDataByPrinipal() {
        List<Map<String, Object>> list = getBaseMapper().selectSafetyDataByPrinipal();
        return list;
    }

    @Override
    public List<Map<String, Object>> selectPieData() {
        List<Map<String, Object>> list = getBaseMapper().selectPieData();
        return list;
    }

    @Override
    public Map<String, Object> selectSafetyQuestionsQuantity() {
        Map<String, Object> questionData = getBaseMapper().selectSafetyQuestionsQuantity();

        // 总数
        Integer count = Integer.valueOf(String.valueOf(questionData.get("count")));
        // 未完成数
        Integer unfinished = Integer.valueOf(String.valueOf(questionData.get("unfinished")));

        // 计算百分比
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(0);
        if (unfinished == 0) { // 除数为0，百分比直接为0%
            questionData.put("percent", "100%");
        } else {
            double retio = unfinished * 1.0 / count;
            String percent = format.format(retio);
            questionData.put("percent", percent);
        }

        return questionData;
    }

    @Override
    public List<Map<String, Object>> selectBarData() {
        List<Map<String, Object>> list = getBaseMapper().selectBarData();
        return list;
    }

    @Override
    public Map<String, Object> selectCurveData() {
        // 查询30天数据
        List<Map<String,Object>> list = getBaseMapper().selectCurveData();

        // echarts所需数据
        List<String> legend = new ArrayList<>();
        List<String> dataX = new ArrayList<>();
        List<Map<String, Object>> series = new ArrayList<>();

        legend.add("一般隐患事故");
        legend.add("重大隐患事故");

        List data1 = new ArrayList();
        List data2 = new ArrayList();
        for (int i = 0; i<list.size(); i++) {
            dataX.add((String) list.get(i).get("clickDate"));
            data1.add(list.get(i).get("ordinaryValue"));
            data2.add(list.get(i).get("greatValue"));
        }

        Map<String,Object> series1 = new HashMap<>();
        series1.put("name", "一般隐患事故");
        series1.put("type", "line");
        series1.put("stack", "总量");
        series1.put("data", data1);
        Map<String,Object> series2 = new HashMap<>();
        series2.put("name", "重大隐患事故");
        series2.put("type", "line");
        series2.put("stack", "总量");
        series2.put("data", data2);

        series.add(series1);
        series.add(series2);

        // 返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("legend", legend);
        result.put("dataX", dataX);
        result.put("series", series);

        return result;
    }

    @Override
    public List<Map<String, Object>> selectRectifyDateWarning() {
        Map<String, Object> data = getBaseMapper().selectQuestionNumber();
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(null);
        }

        Integer index = null;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            if ("count".equals(entry.getKey())) {
                map.put("name", "隐患总个数");
                index = 0;
            } else if ("underwayNumber".equals(entry.getKey())) {
                map.put("name", "隐患整改中个数");
                index = 2;
            } else if ("timeoutNumber".equals(entry.getKey())) {
                map.put("name", "隐患整改超时个数");
                index = 3;
            } else if ("completeNumber".equals(entry.getKey())) {
                map.put("name", "隐患整改完成个数");
                index = 1;
            }

            map.put("value", entry.getValue());
            list.set(index, map);
        }


        return list;
    }

    @Override
    public BasePage<SafetyRiskDetection> selectTimeoutList(SafetyRiskDetectionBO riskDetectionBO) {
        riskDetectionBO.setOrdersBd("create_time", false);
        BasePage<SafetyRiskDetection> list = getBaseMapper().selectTimeoutList(riskDetectionBO.parse(), riskDetectionBO);
        return list;
    }

    @Override
    public Result dangeTimeoutPush(Integer id) {

        // 查询安全隐患排查信息
        SafetyRiskDetection riskDetection = getBaseMapper().selectById(id);
        if (riskDetection == null) {
            throw new CrmException(500, "安全隐患排查信息不存在，请刷新页面！");
        }

        // 推送消息
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setByUser(UserUtil.getUserId());
        messageEntity.setTitle(riskDetection.getRiskDesc());
        messageEntity.setData(String.valueOf(riskDetection.getId()));
        //List<Long> toUsers = messageFeignService.getQueuesUserList(MessageType.MESSAGE_SECURITY_RISKS);
        List<Long> toUsers = getBaseMapper().getQueuesUserList(riskDetection.getId());
        messageEntity.setToUsers(toUsers);//指定消息推送人
        messageEntity.setCode(MessageType.MESSAGE_SECURITY_RISKS);
        messageFeignService.send(messageEntity);

        return Result.ok();
    }

    @Override
    public BasePage<Map<String, Object>> selectQuestionList(Map<String, Object> params) {
        PageEntity page=new PageEntity();
        page.setPage(Integer.valueOf(String.valueOf(params.get("page"))));
        page.setLimit(Integer.valueOf(String.valueOf(params.get("limit"))));
        BasePage<Map<String, Object>> data = getBaseMapper().selectQuestionList(page.parse(),params);
        return data;
    }

    @Override
    public BasePage<SafetyRiskDetection> selectPhotoList(SafetyRiskDetectionBO riskDetectionBO) {
        BasePage<SafetyRiskDetection> list = getBaseMapper().selectByPage(riskDetectionBO.parse(), riskDetectionBO);
        for(SafetyRiskDetection safetyRiskDetection:list.getList()){
            Result<List<FileEntity>> result =  adminFileService.queryFileList(safetyRiskDetection.getBatchId());
            if(result.getCode() == 0){
                safetyRiskDetection.setImgs(result.getData());
            }
        }
        return list;
    }

    @Override
    public BasePage<SafetyRiskDetection> selectListByPro(SafetyRiskDetectionBO riskDetectionBO) {
        riskDetectionBO.setOrdersBd("a.create_time",false);
        BasePage<SafetyRiskDetection> list = getBaseMapper().selectListByPro(riskDetectionBO.parse(), riskDetectionBO);
        return list;
    }

    @Override
    public Map<String, Object> selectProDangerNumber() {
        Map<String, Object> map = getBaseMapper().selectProDangerNumber();

        // 总隐患数
        Integer count = Integer.valueOf(String.valueOf(map.get("count")));
        // 完成数
        Integer completeNumber = Integer.valueOf(String.valueOf(map.get("completeNumber")));

        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(0);
        if (count == 0) { // 除数为0，百分比直接为100%
            map.put("percent", "100%");
        } else {
            double retio = completeNumber * 1.0 / count;
            String percent = format.format(retio);
            map.put("percent", percent);
        }
        return map;
    }

    @Override
    public Map<String, Object> selectProDangerRectifyEfficiency() {

        // 查询安全隐患平均整改时间
        Map<String, Object> map = getBaseMapper().selectAvgRectifyTime();

        // 查询安全隐患解除数量   存在复检时间的便是已解除的
        Map<String, Object> resultMap = getBaseMapper().selectQuantityCompletion();
        BigDecimal avgTime = (BigDecimal) map.get("avgTime");
        resultMap.put("avgTime", avgTime);

        return resultMap;
    }

    @Override
    public List<Map<String, Object>> selectDangerNumberProportion(Map<String, Object> params) {
        List<Map<String, Object>> list = getBaseMapper().selectDangerNumberProportion(params);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectTrendAnalyses(String type) {
        Map<String, Object> params = new HashMap<>();
        params = this.dateMap(type);

        if (type.equals("1")) {
            params.put("formatStyle", "%m.%d");
        } else if (type.equals("2")) {
            params.put("formatStyle", "%m.%d");
        } else if (type.equals("3")) {
            params.put("formatStyle", "%Y.%m");
        }

        List<Map<String, Object>> list = getBaseMapper().selectTrendAnalyses(params);
        return list;
    }

    @Override
    public Map<String, Object> selectProDangerLevelProportion() {
        Map<String, Object> map = getBaseMapper().selectProDangerLevelProportion();
        return map;
    }

    @Override
    public Map<String, Object> selectProDangerTypeProportion() {
        Map<String, Object> map = getBaseMapper().selectProDangerTypeProportion();
        return map;
    }

    @Override
    public List<Map<String, Object>> selectProDangerRank() {
        List<Map<String, Object>> list = getBaseMapper().selectProDangerRank();
        return list;
    }

    @Override
    public List<Map<String, Object>> selectProAgingAnalysis() {
        List<Map<String, Object>> list = getBaseMapper().selectProAgingAnalysis();

        for (Map<String, Object> map:list) {

            // 总隐患数
            Integer total = Integer.valueOf(String.valueOf(map.get("total")));
            // 完成数
            Integer completeCount = Integer.valueOf(String.valueOf(map.get("completeCount")));

            NumberFormat format = NumberFormat.getPercentInstance();
            format.setMaximumFractionDigits(0);
            if (total == 0) { // 除数为0，百分比直接为100%
                map.put("percent", "100%");
            } else {
                double retio = completeCount * 1.0 / total;
                String percent = format.format(retio);
                map.put("percent", percent);
            }

        }

        return list;
    }

    /**
     * @param type type=1 查询前一周的时间（包括今天）type=2 查询前一月的时间（包括今天）type=3 查询前一年的月份（包括当月）
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
        }
        return params;
    }
}
