package com.kakarote.build.project.service;

import com.kakarote.build.project.entity.BO.ProgressImageDisplayBO;
import com.kakarote.build.project.entity.PO.ProgressImageDisplay;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 形象进度展示 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-07-05
 */
public interface IProgressImageDisplayService extends BaseService<ProgressImageDisplay> {

    List<ProgressImageDisplay> selectByPage(ProgressImageDisplayBO progressImageDisplayBO);
}
