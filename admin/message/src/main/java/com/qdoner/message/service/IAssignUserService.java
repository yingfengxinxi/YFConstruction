package com.qdoner.message.service;

import com.qdoner.message.entity.PO.AssignUser;
import com.kakarote.core.servlet.BaseService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wudw
 * @since 2021-09-13
 */
public interface IAssignUserService extends BaseService<AssignUser> {
    List<Long> getAssignUser(@Param("data") Map data);
}
