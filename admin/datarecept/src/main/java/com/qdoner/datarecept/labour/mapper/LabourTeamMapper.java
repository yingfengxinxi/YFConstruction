package com.qdoner.datarecept.labour.mapper;

import com.qdoner.datarecept.labour.entity.PO.LabourTeam;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.Map;

/**
 * <p>
 * 劳务管理--班组 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-03-19
 */
public interface LabourTeamMapper extends BaseMapper<LabourTeam> {
    int saveFile(@Param("data") Map<String, Object> fileData);
}
