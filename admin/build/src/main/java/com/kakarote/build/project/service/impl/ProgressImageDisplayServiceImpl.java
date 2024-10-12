package com.kakarote.build.project.service.impl;

import com.kakarote.build.project.entity.BO.ProgressImageDisplayBO;
import com.kakarote.build.project.entity.PO.ProgressImageDisplay;
import com.kakarote.build.project.mapper.ProgressImageDisplayMapper;
import com.kakarote.build.project.service.IProgressImageDisplayService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 形象进度展示 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-07-05
 */
@Service
public class ProgressImageDisplayServiceImpl extends BaseServiceImpl<ProgressImageDisplayMapper, ProgressImageDisplay> implements IProgressImageDisplayService {

    @Autowired
    private AdminFileService adminFileService;

    @Override
    public List<ProgressImageDisplay> selectByPage(ProgressImageDisplayBO progressImageDisplayBO) {
        List<ProgressImageDisplay> list = getBaseMapper().selectByPage(progressImageDisplayBO);

        // 返回文件
        for (ProgressImageDisplay progressImageDisplay : list) {
            Result<List<FileEntity>> result =  adminFileService.queryFileList(progressImageDisplay.getBatchId());
            if(result.getCode() == 0){
                progressImageDisplay.setImgs(result.getData());
            }
        }
        return list;
    }
}
