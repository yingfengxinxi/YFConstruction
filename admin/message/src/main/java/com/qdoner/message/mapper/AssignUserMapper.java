package com.qdoner.message.mapper;

import com.qdoner.message.entity.PO.AssignUser;
import com.kakarote.core.servlet.BaseMapper;
import com.qdoner.message.entity.PO.Queues;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-09-13
 */
public interface AssignUserMapper extends BaseMapper<AssignUser> {

    List<Long> getAssignUser(@Param("data") Map data);
}
