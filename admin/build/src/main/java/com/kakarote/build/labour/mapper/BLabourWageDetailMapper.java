package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.BO.BLabourWageDetailBO;
import com.kakarote.build.labour.entity.PO.BLabourWageDetail;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工资管理详情 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-04-26
 */
public interface BLabourWageDetailMapper extends BaseMapper<BLabourWageDetail> {



    BasePage<BLabourWageDetail> selectDataList(BasePage<AdminMessage> parse, @Param("data") BLabourWageDetailBO bLabourWageDetailBO);
    public List<BLabourWageDetail> selectByPid(String pid);
    public Integer  deleteByPid(String pid);

    public String  selectCumulativeBalance(BLabourWageDetail bLabourWageDetail);

    public BLabourWageDetail  selectSumPid(String pid);

    List<Map<String,Object>> selectByWageId(String pid);
}
