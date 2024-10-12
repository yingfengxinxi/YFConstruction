package com.kakarote.build.wifi.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.wifi.entity.BO.WifiTestBO;
import com.kakarote.build.wifi.entity.PO.WifiTest;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 生成试卷 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-05-25
 */
public interface WifiTestMapper extends BaseMapper<WifiTest> {

    Map<String,Object> MySelectById(@Param("data") Map<String,Object> map);
    /**
     * 加注解是因为需要对当前sql放行
     * @param
     * @return
     */

    BasePage<Map<String,Object>> selectTestPaperList(BasePage<WifiTest> wt,@Param("data") WifiTestBO wifiTestBO);

    void savePaper(WifiTestBO WTB);

    // 1、工种答题占比分析--饼状图
    List<Map> getTestWorkTypeId();

    //2、试题得分分析--按试题类型分类，柱状图代表总分，折线图代表平均分
    List<Map> getScopeByTestType();

    //3、答题人员排名，前10--横向柱状图
    List<Map> getScopeByTen();

    //4、视频观看次数占比--饼状图
    List<Map> getVideoWatchCounts();

}
