package com.xxl.job.admin.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.xxl.job.admin.core.model.XxlJobGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 16/9/30.
 */
@Mapper
public interface XxlJobGroupDao {

    @SqlParser(filter = true)
    public List<XxlJobGroup> findAll();
    @SqlParser(filter = true)
    public List<XxlJobGroup> findByAddressType(@Param("addressType") int addressType);
    @SqlParser(filter = true)
    public int save(XxlJobGroup xxlJobGroup);
    @SqlParser(filter = true)
    public int update(XxlJobGroup xxlJobGroup);
    @SqlParser(filter = true)
    public int remove(@Param("id") int id);
    @SqlParser(filter = true)
    public XxlJobGroup load(@Param("id") int id);
    @SqlParser(filter = true)
    public List<XxlJobGroup> pageList(@Param("offset") int offset,
                                      @Param("pagesize") int pagesize,
                                      @Param("appname") String appname,
                                      @Param("title") String title);
    @SqlParser(filter = true)
    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("appname") String appname,
                             @Param("title") String title);

}
