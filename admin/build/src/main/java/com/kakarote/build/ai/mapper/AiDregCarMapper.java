package com.kakarote.build.ai.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.ai.entity.BO.AiDregCarBO;
import com.kakarote.build.ai.entity.PO.AiDregCar;
import com.kakarote.build.ai.entity.PO.BAiDregCarCleaning;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 渣土车记录 Mapper 接口
 * </p>
 *
 * @author shao
 * @since 2021-05-24
 */
public interface AiDregCarMapper extends BaseMapper<AiDregCar> {

    BasePage<AiDregCar> selectByPage(BasePage<AiDregCar> parse, @Param("data") AiDregCarBO aiDregCarBO);

    @SqlParser(filter=true)
    int batchSaveData(@Param("list") List<BAiDregCarCleaning> list);

    @SqlParser(filter=true)
    int batchSaveDregCarData(@Param("list") List<AiDregCar> list);
}
