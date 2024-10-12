package com.xxl.job.admin.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.xxl.job.admin.core.model.XxlJobLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * job log
 * @author xuxueli 2016-1-12 18:03:06
 */
@Mapper
public interface XxlJobLogDao {

	// exist jobId not use jobGroup, not exist use jobGroup
	@SqlParser(filter = true)
	public List<XxlJobLog> pageList(@Param("offset") int offset,
									@Param("pagesize") int pagesize,
									@Param("jobGroup") int jobGroup,
									@Param("jobId") int jobId,
									@Param("triggerTimeStart") Date triggerTimeStart,
									@Param("triggerTimeEnd") Date triggerTimeEnd,
									@Param("logStatus") int logStatus);
	@SqlParser(filter = true)
	public int pageListCount(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("jobGroup") int jobGroup,
							 @Param("jobId") int jobId,
							 @Param("triggerTimeStart") Date triggerTimeStart,
							 @Param("triggerTimeEnd") Date triggerTimeEnd,
							 @Param("logStatus") int logStatus);
	@SqlParser(filter = true)
	public XxlJobLog load(@Param("id") long id);
	@SqlParser(filter = true)
	public long save(XxlJobLog xxlJobLog);
	@SqlParser(filter = true)
	public int updateTriggerInfo(XxlJobLog xxlJobLog);
	@SqlParser(filter = true)
	public int updateHandleInfo(XxlJobLog xxlJobLog);
	@SqlParser(filter = true)
	public int delete(@Param("jobId") int jobId);
	@SqlParser(filter = true)
	public Map<String, Object> findLogReport(@Param("from") Date from,
											 @Param("to") Date to);
	@SqlParser(filter = true)
	public List<Long> findClearLogIds(@Param("jobGroup") int jobGroup,
									  @Param("jobId") int jobId,
									  @Param("clearBeforeTime") Date clearBeforeTime,
									  @Param("clearBeforeNum") int clearBeforeNum,
									  @Param("pagesize") int pagesize);
	@SqlParser(filter = true)
	public int clearLog(@Param("logIds") List<Long> logIds);
	@SqlParser(filter = true)
	public List<Long> findFailJobLogIds(@Param("pagesize") int pagesize);
	@SqlParser(filter = true)
	public int updateAlarmStatus(@Param("logId") long logId,
								 @Param("oldAlarmStatus") int oldAlarmStatus,
								 @Param("newAlarmStatus") int newAlarmStatus);
	@SqlParser(filter = true)
	public List<Long> findLostJobIds(@Param("losedTime") Date losedTime);

}
