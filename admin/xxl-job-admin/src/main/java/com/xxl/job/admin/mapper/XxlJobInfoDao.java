package com.xxl.job.admin.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.xxl.job.admin.core.model.XxlJobInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * job info
 * @author xuxueli 2016-1-12 18:03:45
 */
@Mapper
public interface XxlJobInfoDao {
	@SqlParser(filter = true)
	public List<XxlJobInfo> pageList(@Param("offset") int offset,
									 @Param("pagesize") int pagesize,
									 @Param("jobGroup") int jobGroup,
									 @Param("triggerStatus") int triggerStatus,
									 @Param("jobDesc") String jobDesc,
									 @Param("executorHandler") String executorHandler,
									 @Param("author") String author);
	@SqlParser(filter = true)
	public int pageListCount(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("jobGroup") int jobGroup,
							 @Param("triggerStatus") int triggerStatus,
							 @Param("jobDesc") String jobDesc,
							 @Param("executorHandler") String executorHandler,
							 @Param("author") String author);
	@SqlParser(filter = true)
	public int save(XxlJobInfo info);
	@SqlParser(filter = true)
	public XxlJobInfo loadById(@Param("id") int id);
	@SqlParser(filter = true)
	public int update(XxlJobInfo xxlJobInfo);
	@SqlParser(filter = true)
	public int delete(@Param("id") long id);
	@SqlParser(filter = true)
	public List<XxlJobInfo> getJobsByGroup(@Param("jobGroup") int jobGroup);
	@SqlParser(filter = true)
	public int findAllCount();
	@SqlParser(filter = true)
	public List<XxlJobInfo> scheduleJobQuery(@Param("maxNextTime") long maxNextTime, @Param("pagesize") int pagesize );
	@SqlParser(filter = true)
	public int scheduleUpdate(XxlJobInfo xxlJobInfo);
	@SqlParser(filter = true)
	public List<Map<String,Object>> listInfoAll();
}
