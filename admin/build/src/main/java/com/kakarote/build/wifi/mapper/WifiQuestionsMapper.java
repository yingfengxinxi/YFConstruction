package com.kakarote.build.wifi.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.labour.entity.BO.LabourSpeworktypeBO;
import com.kakarote.build.labour.entity.PO.LabourSpeworktype;
import com.kakarote.build.wifi.entity.BO.WifiQuestionsBO;
import com.kakarote.build.wifi.entity.PO.WifiQuestions;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * wifi教育答题--问题表 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
public interface WifiQuestionsMapper extends BaseMapper<WifiQuestions> {

    BasePage<Map<String,Object>> selectList(BasePage<WifiQuestions> page, @Param("data") WifiQuestionsBO wifiQuestionsBO);

    @SqlParser(filter = true)
    List<Map<String,Object>> TestPaperQue( @Param("data") Map<String,Object> map);

    @SqlParser(filter = true)
    int  TestPaperQueCount( @Param("data") Map<String,Object> map);

    @SqlParser(filter = true)
    void deleteQuestionById(@Param("id") String id);
}
