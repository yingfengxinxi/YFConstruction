package com.qdoner.datarecept.ai.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.qdoner.datarecept.ai.entity.PO.AiBareSoilData;
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

    @SqlParser(filter = true)
    int batchSaveData(@Param("list") List<AiBareSoilData> bareSoilDataList);
}
