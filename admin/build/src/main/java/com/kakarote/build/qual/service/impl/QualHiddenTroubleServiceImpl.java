package com.kakarote.build.qual.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.qual.entity.PO.QualHiddenTrouble;
import com.kakarote.build.qual.mapper.QualHiddenTroubleMapper;
import com.kakarote.build.qual.service.IQualHiddenTroubleService;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工质量--质量检查管理--安全隐患 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-03-23
 */
@Service
public class QualHiddenTroubleServiceImpl extends BaseServiceImpl<QualHiddenTroubleMapper, QualHiddenTrouble> implements IQualHiddenTroubleService {

    @Override
    public List<Map<String, Object>> selectPieData() {
        List<Map<String, Object>> list = getBaseMapper().selectPieData();
        return list;
    }

    @Override
    public List<Map<String, Object>> selectBarData(Map<String, Object> params) {
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

        List<Map<String, Object>> list = getBaseMapper().selectBarData(params);
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
    public List<Map<String, Object>> selectQualDataByPrinipal() {
        List<Map<String, Object>> list = getBaseMapper().selectQualDataByPrinipal();
        return list;
    }

    @Override
    public Map<String, Object> selectQualQuestionsQuantity() {
        Map<String, Object> questionData = getBaseMapper().selectQualQuestionsQuantity();

        // 总数
        Integer count = Integer.valueOf(String.valueOf(questionData.get("count")));
        // 未完成数
        Integer unfinished = Integer.valueOf(String.valueOf(questionData.get("unfinished")));

        // 计算百分比
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(0);
        if (unfinished == 0) { // 除数为0，百分比直接为100%
            questionData.put("percent", "100%");
        } else {
            double retio = unfinished * 1.0 / count;
            String percent = format.format(retio);
            questionData.put("percent", percent);
        }

        return questionData;
    }
}
