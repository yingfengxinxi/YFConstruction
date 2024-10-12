package com.kakarote.build.ai.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.ai.entity.BO.AiBareSoilDataBO;
import com.kakarote.build.ai.entity.PO.AiBareSoilData;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 裸土报警数据 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2022-01-06
 */
public interface AiBareSoilDataMapper extends BaseMapper<AiBareSoilData> {

    BasePage<AiBareSoilData> selectByPage(BasePage<Object> parse, @Param("data") AiBareSoilDataBO aiBareSoilDataBO);

    @SqlParser(filter = true)
    int batchSaveData(@Param("list") List<AiBareSoilData> bareSoilDataList);

    List<Map<String, Object>> selectReportData(@Param("data") Map<String, Object> params);

    Map<String, Object> selectAlertNumber();

    List<Map<String, Object>> selectAlertTrend(@Param("data") Map<String, Object> map);

    List<Map<String, Object>> selectProjectAlertNumber();

    BasePage<Map<String, Object>> selectPageNj(BasePage<Object> parse, @Param("data") AiBareSoilDataBO aiBareSoilDataBO);

    List<Map<String, Object>> selectTypeProportion();
}
