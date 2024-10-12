package com.xxl.job.admin.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.xxl.job.admin.core.model.XxlJobLogReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * job log
 * @author xuxueli 2019-11-22
 */
@Mapper
public interface XxlJobLogReportDao {
	@SqlParser(filter = true)
	public int save(XxlJobLogReport xxlJobLogReport);
	@SqlParser(filter = true)
	public int update(XxlJobLogReport xxlJobLogReport);
	@SqlParser(filter = true)
	public List<XxlJobLogReport> queryLogReport(@Param("triggerDayFrom") Date triggerDayFrom,
												@Param("triggerDayTo") Date triggerDayTo);
	@SqlParser(filter = true)
	public XxlJobLogReport queryLogReportTotal();

}
