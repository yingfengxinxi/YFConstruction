package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.BO.ProgressImageDisplayBO;
import com.kakarote.build.project.entity.PO.ProgressImageDisplay;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 形象进度展示 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-07-05
 */
public interface ProgressImageDisplayMapper extends BaseMapper<ProgressImageDisplay> {

    List<ProgressImageDisplay> selectByPage(@Param("data") ProgressImageDisplayBO progressImageDisplayBO);
}
