package com.kakarote.build.wifi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.mapper.LabourInfocollectionMapper;
import com.kakarote.build.wifi.entity.BO.WifiVideoBO;
import com.kakarote.build.wifi.entity.PO.WifiTest;
import com.kakarote.build.wifi.entity.PO.WifiVideo;
import com.kakarote.build.wifi.entity.PO.WifiVideoDetail;
import com.kakarote.build.wifi.entity.PO.WifiVideoWatchRecord;
import com.kakarote.build.wifi.mapper.WifiVideoDetailMapper;
import com.kakarote.build.wifi.mapper.WifiVideoMapper;
import com.kakarote.build.wifi.service.IWifiTestService;
import com.kakarote.build.wifi.service.IWifiVideoDetailService;
import com.kakarote.build.wifi.service.IWifiVideoService;
import com.kakarote.build.wifi.service.IWifiVideoWatchRecordService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 教育视频配置--明细表 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-06-01
 */
@Service
public class WifiVideoDetailServiceImpl extends BaseServiceImpl<WifiVideoDetailMapper, WifiVideoDetail> implements IWifiVideoDetailService {

    @Autowired
    private AdminFileService adminFileService;
    @Autowired
    private IWifiVideoService iWifiVideoService;
    @Autowired
    private IWifiTestService iWifiTestService;
    @Autowired
    private IWifiVideoWatchRecordService iWifiVideoWatchRecordService;
    @Autowired
    private LabourInfocollectionMapper labourInfocollectionMapper;

    @Override
    public Map getOneVideoRandom(Map map) {
        String workTypeId = (String) map.get("workTypeId");
        String phone = (String) map.get("answerPhone");
        String name = (String) map.get("answerName");
        LabourInfocollection labourInfocollection = labourInfocollectionMapper.getInfocollectionByNameAndPhone(name,phone);
        if(labourInfocollection == null){
            throw new CrmException(1004, "您的手机号未进行认证，请联系管理员进行信息采集");
        }
        Map mapAll = new HashMap();
        //1、查看启用状态下的数据是否为null ,null的话返回null
        WifiVideoBO wifiVideoBO = new WifiVideoBO();
        wifiVideoBO.setIfStartUse("Y");
        wifiVideoBO.setWorkTypeId(workTypeId);
        BasePage<Map<String, Object>> mapBasePage = iWifiVideoService.selectList(wifiVideoBO);

        if(mapBasePage.getList().size() == 0){
            mapAll.put("code",-1);
            mapAll.put("msg","未配置启用教育模板");
            return mapAll;
        }
        //2、不是null 的话，根据工种id查看启用状态下的教育模式  1：视频；2：答题；3：随机
        Map<String, Object> stringObjectMap = mapBasePage.getList().get(0);
        String educationModel= (String) stringObjectMap.get("educationModel");
        if(educationModel.equals("1")){
            Map videoRandom = getVideoRandom(workTypeId);
            mapAll.put("code",1);
            mapAll.put("msg","视频");
            mapAll.put("data",videoRandom);
        }else if(educationModel.equals("2")){
            mapAll.put("code",2);
            mapAll.put("msg","答题");
        }else {
            //随机 模式下，查看用户最近一条是答题还是视频，若是答题返回视频；若是视频返回答题；
            Integer type = getType(workTypeId, phone);
            if(type == 1){
                Map videoRandom = getVideoRandom(workTypeId);
                mapAll.put("code",1);
                mapAll.put("msg","视频");
                mapAll.put("data",videoRandom);
            }else{
                mapAll.put("code",2);
                mapAll.put("msg","答题");
            }
        }

        return mapAll;

    }

    //随机抽取视频
    public Map getVideoRandom(String workTypeId){
        Map map = new HashMap();
        map.put("workTypeId",workTypeId);
        map.put("flag","Y");
        //1、随机抽取未观看的 启动的视频
        Map oneVideoRandom1 = getBaseMapper().getOneVideoRandom(map);
        if(oneVideoRandom1 != null){

//            Map map2 = new HashMap();
//            map2.put("batchId",(String) oneVideoRandom1.get("batchId"));
//            map2.put("path",(String) oneVideoRandom1.get("vdUrl"));
//            Result<FileEntity> fileEntityResult = adminFileService.queryOneByBatchIdAndPath(map2);
//            FileEntity data1 = fileEntityResult.getData();

            String vdId = oneVideoRandom1.get("fileId").toString();
            Long aLong = Long.valueOf(vdId);
            Result<FileEntity> fileEntityResult = adminFileService.queryById(aLong);
            FileEntity data1 = fileEntityResult.getData();

            oneVideoRandom1.put("url",data1.getUrl());
            return oneVideoRandom1;
        }else{

            map.put("flag",null);
            //2、随机抽取 启动的视频
            Map oneVideoRandom = getBaseMapper().getOneVideoRandom(map);

//            Map map2 = new HashMap();
//            map2.put("batchId",(String) oneVideoRandom.get("batchId"));
//            map2.put("path",(String) oneVideoRandom.get("vdUrl"));
//            Result<FileEntity> fileEntityResult = adminFileService.queryOneByBatchIdAndPath(map2);
//            FileEntity data1 = fileEntityResult.getData();
            if(oneVideoRandom!=null){
                String vdId = oneVideoRandom.get("fileId").toString();
                Long aLong = Long.valueOf(vdId);
                Result<FileEntity> fileEntityResult = adminFileService.queryById(aLong);
                FileEntity data1 = fileEntityResult.getData();
                oneVideoRandom.put("url",data1.getUrl());
            }
            return oneVideoRandom;
        }
    }

    //根据工种和手机号，查看用户最近一条是答题还是视频，若是答题返回视频1；若是视频返回答题2；
    public Integer getType(String workTypeId,String phone){
        Integer type = 2;

        //查询答题 最近的一条数据
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("work_type_id",workTypeId);
        queryWrapper.eq("answer_phone",phone);
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("limit 1");
        WifiTest wifiTest = iWifiTestService.getBaseMapper().selectOne(queryWrapper);

        //查询视频 最近的一条数据
        Map a = new HashMap();
        a.put("workTypeId",workTypeId);
        a.put("watchPeoplePhone",phone);
        Map map = iWifiVideoWatchRecordService.getCreateTimeLast(a);



        if(wifiTest == null){
            return 2;
        }

        if(map == null){
            return 1;
        }

        Date testCreateTime = wifiTest.getCreateTime();
        Date wifiVideoCreateTime = (Date) map.get("createTime");

        long testTime = testCreateTime.getTime();
        long videoTime = wifiVideoCreateTime.getTime();

        if(testTime <= videoTime){
            type = 2;
        }else{
            type = 1;
        }

        return type;
    }
    public WifiVideoDetail selectByBithIdToFileId (String  id){

        WifiVideoDetail wifiVideoDetail = getBaseMapper().selectByBithIdToFileId(id);
        return wifiVideoDetail;
    }
}
