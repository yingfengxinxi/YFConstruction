package com.xxl.job.admin.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.xxl.job.admin.core.model.XxlJobUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author xuxueli 2019-05-04 16:44:59
 */
@Mapper
public interface XxlJobUserDao {
	@SqlParser(filter = true)
	public List<XxlJobUser> pageList(@Param("offset") int offset,
                                     @Param("pagesize") int pagesize,
                                     @Param("username") String username,
									 @Param("role") int role);
	@SqlParser(filter = true)
	public int pageListCount(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("username") String username,
							 @Param("role") int role);
	@SqlParser(filter = true)
	public XxlJobUser loadByUserName(@Param("username") String username);
	@SqlParser(filter = true)
	public int save(XxlJobUser xxlJobUser);
	@SqlParser(filter = true)
	public int update(XxlJobUser xxlJobUser);
	@SqlParser(filter = true)
	public int delete(@Param("id") int id);

}
