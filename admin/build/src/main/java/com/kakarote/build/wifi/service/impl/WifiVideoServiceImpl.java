package com.kakarote.build.wifi.service.impl;

import com.alibaba.nacos.common.utils.UuidUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kakarote.build.wifi.entity.BO.WifiVideoBO;
import com.kakarote.build.wifi.entity.PO.WifiVideo;
import com.kakarote.build.wifi.entity.PO.WifiVideoDetail;
import com.kakarote.build.wifi.mapper.WifiVideoDetailMapper;
import com.kakarote.build.wifi.mapper.WifiVideoMapper;
import com.kakarote.build.wifi.service.IWifiVideoDetailService;
import com.kakarote.build.wifi.service.IWifiVideoService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 教育视频配置--主表 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-06-01
 */
@Service
public class WifiVideoServiceImpl extends BaseServiceImpl<WifiVideoMapper, WifiVideo> implements IWifiVideoService {

    @Autowired
    private IWifiVideoDetailService iWifiVideoDetailService;
    @Autowired
    private AdminFileService adminFileService;

    @Override
    public BasePage<Map<String, Object>> selectList(WifiVideoBO wifiVideoBO) {
        wifiVideoBO.setOrdersBd("create_time",false);
        return getBaseMapper().selectList(wifiVideoBO.parse(), wifiVideoBO);
    }

    @Override
    @Transactional
    public WifiVideo saveTable(WifiVideo wifiVideo) {
        wifiVideo.setId(UuidUtils.generateUuid().replace("-",""));
        if (wifiVideo.getBatchIds() == null) {
            throw new CrmException(1001, "视频批次id未找到");
        }
        List<String> StrList = wifiVideo.getBatchIds();
        for(String str:StrList){
            Result<List<Map>> listResult = adminFileService.queryFileListByBatchId(str);
            List<Map> data = listResult.getData();

            if (data.size() == 0) {
                throw new CrmException(1001, "课程库中未找到视频视频");
            }
            for (int i = 0; i < data.size(); i++) {
                Map fileEntity = data.get(i);
                WifiVideoDetail wifiVideoDetail = new WifiVideoDetail();
                wifiVideoDetail.setVid(wifiVideo.getId());
                wifiVideoDetail.setBatchId(str);
//            wifiVideoDetail.setVideoUrl((String) fileEntity.get("pathUrl"));
//            Long fileId = (Long) fileEntity.get("fileId");
//            String s = fileId.toString();
                wifiVideoDetail.setId(UuidUtils.generateUuid().replace("-",""));
                iWifiVideoDetailService.save(wifiVideoDetail);

            }
        }

        //一个工种仅允许存在一条启用模板
        if (wifiVideo.getIfStartUse().equals("Y")) {
            UpdateWrapper queryWrapper = new UpdateWrapper();
            queryWrapper.eq("work_type_id", wifiVideo.getWorkTypeId());
            queryWrapper.set("if_Start_Use", "N");
            update(queryWrapper);
        }

        save(wifiVideo);

        return wifiVideo;
    }

    @Override
    public WifiVideo updateTable(WifiVideo wifiVideo) {

/*        if (wifiVideo.getBatchId() == null) {
            throw new CrmException(1001, "视频批次id未找到,删除当前信息重新上传");
        }*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("vid", wifiVideo.getId());
        iWifiVideoDetailService.getBaseMapper().delete(queryWrapper);
//        String demosub= wifiVideo.getBatchId().substring(1,wifiVideo.getBatchId().length()-1);
//        String StrList[] = demosub.split(",");
        List<String> StrList = wifiVideo.getBatchIds();
        for (String str : StrList) {
            Result<List<Map>> listResult = adminFileService.queryFileListByBatchId(str);
            List<Map> data = listResult.getData();
            if (data.size() == 0) {
                throw new CrmException(1001, "请上传视频");
            }
            for (int i = 0; i < data.size(); i++) {
                Map fileEntity = data.get(i);
                WifiVideoDetail wifiVideoDetail = new WifiVideoDetail();
                wifiVideoDetail.setVid(wifiVideo.getId());
                wifiVideoDetail.setBatchId(str);
//            wifiVideoDetail.setVideoUrl((String) fileEntity.get("pathUrl"));
//                Long fileId = (Long) fileEntity.get("fileId");
//                String s = fileId.toString();
//                wifiVideoDetail.setId(s);
                wifiVideoDetail.setId(UuidUtils.generateUuid().replace("-",""));
                iWifiVideoDetailService.save(wifiVideoDetail);
            }
        }
        //一个工种仅允许存在一条启用模板
        if (wifiVideo.getIfStartUse().equals("Y")) {
            UpdateWrapper wrapper = new UpdateWrapper();
            wrapper.eq("work_type_id", wifiVideo.getWorkTypeId());
            wrapper.set("if_Start_Use", "N");
            update(wrapper);
        }
        updateById(wifiVideo);

        return wifiVideo;
    }

    @Override
    public Map selectById(String id) {
        Map map = new HashMap();
        WifiVideo one = query()
                .eq("id", id)
                .ne("project_id", 0)
                .one();

        List<WifiVideoDetail> list = iWifiVideoDetailService
                .query()
                .eq("vid", id)
                .ne("project_id", 0)
                .orderByDesc("create_time")
                .list();
        map.put("ZHUB", one);
        map.put("ZIB", list);

        return map;
    }
}
