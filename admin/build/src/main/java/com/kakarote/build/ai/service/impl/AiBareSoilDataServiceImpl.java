package com.kakarote.build.ai.service.impl;

import com.kakarote.build.ai.entity.BO.AiBareSoilDataBO;
import com.kakarote.build.ai.entity.PO.AiBareSoilData;
import com.kakarote.build.ai.mapper.AiBareSoilDataMapper;
import com.kakarote.build.ai.service.IAiBareSoilDataService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 裸土报警数据 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2022-01-06
 */
@Service
public class AiBareSoilDataServiceImpl extends BaseServiceImpl<AiBareSoilDataMapper, AiBareSoilData> implements IAiBareSoilDataService {

    @Override
    public BasePage<AiBareSoilData> selectByPage(AiBareSoilDataBO aiBareSoilDataBO) {
        BasePage<AiBareSoilData> list = this.getBaseMapper().selectByPage(aiBareSoilDataBO.parse(), aiBareSoilDataBO);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectReportData(Map<String, Object> params) {
        List<Map<String, Object>> list = getBaseMapper().selectReportData(params);
        return list;
    }

    @Override
    public Map<String, Object> selectAlertNumber() {
        return getBaseMapper().selectAlertNumber();
    }

    @Override
    public List<Map<String, Object>> selectAlertTrend(Map<String, Object> params) {
        if (params.get("type") == null) {
            throw new CrmException(3001, "type值不可为空");
        }
        Map<String, Object> map = null;
        if ("1".equals(String.valueOf(params.get("type")))) {//七天
            map = dateMap("1");
        } else if ("2".equals(String.valueOf(params.get("type")))) {//30天
            map = dateMap("2");
        }
        return getBaseMapper().selectAlertTrend(map);
    }

    @Override
    public List<Map<String, Object>> selectProjectAlertNumber() {
        return getBaseMapper().selectProjectAlertNumber();
    }

    @Override
    public BasePage<Map<String, Object>> selectPageNj(AiBareSoilDataBO aiBareSoilDataBO) {
        return getBaseMapper().selectPageNj(aiBareSoilDataBO.parse(), aiBareSoilDataBO);
    }

    @Override
    public List<Map<String, Object>> selectTypeProportion() {
        return getBaseMapper().selectTypeProportion();
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
