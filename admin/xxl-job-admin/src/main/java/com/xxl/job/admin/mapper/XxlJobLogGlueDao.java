package com.xxl.job.admin.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.xxl.job.admin.core.model.XxlJobLogGlue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * job log for glue
 * @author xuxueli 2016-5-19 18:04:56
 */
@Mapper
public interface XxlJobLogGlueDao {
	@SqlParser(filter = true)
	public int save(XxlJobLogGlue xxlJobLogGlue);
	@SqlParser(filter = true)
	public List<XxlJobLogGlue> findByJobId(@Param("jobId") int jobId);
	@SqlParser(filter = true)
	public int removeOld(@Param("jobId") int jobId, @Param("limit") int limit);
	@SqlParser(filter = true)
	public int deleteByJobId(@Param("jobId") int jobId);
	
}
