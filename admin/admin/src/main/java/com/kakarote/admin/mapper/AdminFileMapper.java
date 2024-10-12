package com.kakarote.admin.mapper;

import com.kakarote.admin.entity.PO.AdminFile;
import com.kakarote.core.servlet.BaseMapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 附件表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface AdminFileMapper extends BaseMapper<AdminFile> {

    int add(AdminFile adminFile);

    List<AdminFile> selectListWx(String batchId);
    AdminFile  getByIdWx(Long fileId);
}
