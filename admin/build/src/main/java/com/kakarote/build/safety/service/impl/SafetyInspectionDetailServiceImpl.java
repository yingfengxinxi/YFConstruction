package com.kakarote.build.safety.service.impl;

import com.kakarote.build.safety.entity.BO.SafetyInspectionDetailBO;
import com.kakarote.build.safety.entity.PO.SafetyInspectionDetail;
import com.kakarote.build.safety.entity.PO.SafetyInspectionPlan;
import com.kakarote.build.safety.mapper.SafetyInspectionDetailMapper;
import com.kakarote.build.safety.service.ISafetyInspectionDetailService;
import com.kakarote.build.safety.service.ISafetyInspectionPlanService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工安全--安全巡检信息--子表巡检明细 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-04-13
 */
@Service
public class SafetyInspectionDetailServiceImpl extends BaseServiceImpl<SafetyInspectionDetailMapper, SafetyInspectionDetail> implements ISafetyInspectionDetailService {

    @Autowired
    private AdminFileService adminFileService;
    @Autowired
    private IBSafetyDangerSourceService safetyDangerSourceService;
    @Autowired
    private ISafetyInspectionPlanService inspectionPlanService;

    @Override
    public List<Map<String, Object>> selectPieData() {
        List<Map<String, Object>> list = getBaseMapper().selectPieData();
        return list;
    }

    @Override
    public List<Map<String, Object>> selectBarData() {
        List<Map<String, Object>> list = getBaseMapper().selectBarData();
        return list;
    }

    @Override
    public Map<String, Object> selectCurveData() {

        // 查询所有计划
        List<SafetyInspectionPlan> planList = inspectionPlanService.list();

        // echarts所需数据
        List<String> legend = new ArrayList<>();
        List<String> dataX = new ArrayList<>();
        List<Map<String, Object>> series = new ArrayList<>();

        // 查询单个计划近30天内的每个数据
        for (int i = 0; i<planList.size(); i++) {
            SafetyInspectionPlan plan = planList.get(i);
            // 查询
            List<Map<String, Object>> list = getBaseMapper().selectCurveData(plan.getId());

            // 封装说明数据
            legend.add(plan.getPlanName());
            // 封装X轴数据和series
            Map<String, Object> s = new HashMap<>();
            List data = new ArrayList<>();
            s.put("name", plan.getPlanName());
            s.put("type", "line");
            s.put("stack", "总量");
            for (Map map:list) {
                // 第一次循环的时候添加近30天的日期
                if (i == 0) {
                    dataX.add((String) map.get("dateTime"));
                }
                data.add(map.get("value"));
            }
            s.put("data", data);
            series.add(s);

        }

        // 返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("legend", legend);
        result.put("dataX", dataX);
        result.put("series", series);

        return result;
    }

    @Override
    public BasePage<SafetyInspectionDetail> selectDanger(SafetyInspectionDetailBO safetyInspectionDetailBO) {
        if(safetyInspectionDetailBO.getDangerSourceId() != null){
            ArrayList<Integer> ids = new ArrayList<>();
            ids.add(safetyInspectionDetailBO.getDangerSourceId());
            safetyDangerSourceService.getChildId(ids, safetyInspectionDetailBO.getDangerSourceId());
            safetyInspectionDetailBO.setDangerSourceIds(ids);
        }

        BasePage<SafetyInspectionDetail> list = this.getBaseMapper().selectDanger(safetyInspectionDetailBO.parse(), safetyInspectionDetailBO);
        for(SafetyInspectionDetail safetyInspectionDetail:list.getList()){
            Result<List<FileEntity>> result =  adminFileService.queryFileList(safetyInspectionDetail.getBatchId());
            if(result.getCode() == 0){
                safetyInspectionDetail.setImgs(result.getData());
            }
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> selectRectifyDateWarning() {
        Map<String, Object> data = getBaseMapper().selectRectifyDateWarning();
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
    public List<Map<String, Object>> selectSafetyDataByPrinipal() {
        List<Map<String, Object>> list = getBaseMapper().selectSafetyDataByPrinipal();
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
        if (unfinished == 0) { // 除数为0，百分比直接为100%
            questionData.put("percent", "0%");
        } else {
            double retio = unfinished * 1.0 / count;
            String percent = format.format(retio);
            questionData.put("percent", percent);
        }

        return questionData;
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
    public BasePage<SafetyInspectionDetail> selectListByPro(SafetyInspectionDetailBO inspectionDetailBO) {
        BasePage<SafetyInspectionDetail> list = getBaseMapper().selectListByPro(inspectionDetailBO.parse(), inspectionDetailBO);
        return list;
    }

    @Override
    public SafetyInspectionDetail selectDetailByIdPro(Integer id) {
        SafetyInspectionDetail inspectionDetail = getBaseMapper().selectDetailByIdPro(id);
        return inspectionDetail;
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
    public List<Map<String, Object>> selectProDangerRank() {
        List<Map<String, Object>> list = getBaseMapper().selectProDangerRank();
        return list;
    }

    @Override
    public List<Map<String, Object>> selectDangerNumberProportion(Map<String, Object> params) {
        List<Map<String, Object>> list = getBaseMapper().selectDangerNumberProportion(params);
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

    @Override
    public List<Map<String, Object>> selectSafetyLevelAnalysis() {
        List<Map<String, Object>> list = getBaseMapper().selectSafetyLevelAnalysis();
        return list;
    }

    @Override
    public Map<String, Object> selectQuestionNumber() {

        Map<String, Object> data = getBaseMapper().selectRectifyDateWarning();

        return data;
    }

    @Override
    public List<Map<String, Object>> selectHiddenDangerInfo(String iconType) {
        List<Map<String, Object>> list = getBaseMapper().selectHiddenDangerInfo(iconType);
        return list;
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
    public List<Map<String, Object>> proDangerNumList() {
        return getBaseMapper().proDangerNumList();
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
