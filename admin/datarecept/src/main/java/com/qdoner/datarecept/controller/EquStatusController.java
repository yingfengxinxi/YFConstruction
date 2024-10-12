package com.qdoner.datarecept.controller;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;
import com.qdoner.datarecept.entity.PO.DeviceProjectRelation;
import com.qdoner.datarecept.job.FluoriteCloudJob;
import com.qdoner.datarecept.mapper.DeviceProjectRelationMapper;
import com.qdoner.datarecept.service.IReceptService;
import com.qdoner.datarecept.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 设备状态
 */
@Slf4j
@RestController
@RequestMapping("/equ")
public class EquStatusController {

    @Autowired
    private DeviceProjectRelationMapper deviceProjectRelationMapper;
    @Autowired
    private FluoriteCloudJob fluoriteCloudJob;

    /**
     * 接收入口
     * 数据需为 JSON格式
     *
     * @param projectId 项目ID
     * @return
     */
    @GetMapping("/{projectId}/status")
    public Result statusUpdate(@PathVariable Integer projectId) {
        if (projectId == null) {
            return Result.error(-1, "项目ID无效");
        }
        List<DeviceProjectRelation> list = deviceProjectRelationMapper.getVideoDeviceAll(projectId);
        if (list.size() < 1) {
            return Result.error(-1, "项目配置错误");
        }
        for (DeviceProjectRelation device : list) {
            try {
                fluoriteCloudJob.center(device);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Result.ok();
    }
}
