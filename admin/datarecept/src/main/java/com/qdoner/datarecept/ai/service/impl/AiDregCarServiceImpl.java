package com.qdoner.datarecept.ai.service.impl;

import cn.hutool.core.date.DateException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qdoner.datarecept.ai.entity.PO.AiDregCar;
import com.qdoner.datarecept.ai.mapper.AiDregCarMapper;
import com.qdoner.datarecept.ai.service.IAiDregCarService;
import com.qdoner.datarecept.api.common.EnvMonitorCodeEnum;
import com.qdoner.datarecept.labour.mapper.LabourTeamMapper;
import com.qdoner.datarecept.entity.PO.ProjectInfo;
import com.qdoner.datarecept.service.IProjectInfoService;
import com.qdoner.datarecept.utils.ApiUtils;
import com.kakarote.core.common.Result;
import com.kakarote.core.config.MybatisPlusConfig;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 渣土车记录 服务实现类
 * </p>
 *
 * @author shao
 * @since 2021-05-24
 */
@Service
public class AiDregCarServiceImpl extends BaseServiceImpl<AiDregCarMapper, AiDregCar> implements IAiDregCarService {

    @Autowired
    private ApiUtils apiUtils;
    @Autowired
    private LabourTeamMapper labourTeamMapper;
    @Autowired
    private IProjectInfoService projectInfoService;

    public final static String PATH="E:\\tpcf\\";

    @Override
    public Result saveData(List<Map<String,Object>> list) {
        try {
            if(list != null && list.size()>0){
                List<AiDregCar> dataList = new ArrayList<>();
                for(Map data:list){
                    AiDregCar aiDregCar = new AiDregCar();
                    if (data.get("projectId") == null || "".equals(data.get("projectId"))) {
                        throw new CrmException(500, "项目id不能为空");
                    }

                    // 判断项目id是否存在

                    Integer projectId = (Integer) data.get("projectId");

                    LambdaQueryWrapper<ProjectInfo> projectQueryWrapper = new LambdaQueryWrapper<>();
                    projectQueryWrapper.eq(ProjectInfo::getId, projectId);
                    projectQueryWrapper.ge(ProjectInfo::getTenantId, "0");
                    List<ProjectInfo> proList = projectInfoService.list(projectQueryWrapper);
                    if (proList == null || proList.size() <= 0) {
                        throw new CrmException(500, "项目id：" + projectId + "不存在,请项目施工单位先在住建委云平台申请后再进行数据推送");
                    }

                    ProjectInfo projectInfo = proList.get(0);
                    aiDregCar.setTenantId(projectInfo.getTenantId());
                    aiDregCar.setProjectId(projectId);
                    if (data.get("deviceSn") == null || "".equals(data.get("deviceSn"))) {
                        throw new CrmException(500, "设备编号不能为空");
                    }
                    aiDregCar.setEquipmentId((Integer) data.get("deviceSn"));
                    if (data.get("direction") == null || "".equals(data.get("direction"))) {
                        throw new CrmException(500, "行进方向不能为空");
                    }
                    if("in".equals(data.get("direction"))){
                        aiDregCar.setDirection("1");
                    }else if("out".equals(data.get("direction"))){
                        aiDregCar.setDirection("0");
                    }
                    if (data.get("licenseNumberPlate") == null || "".equals(data.get("licenseNumberPlate"))) {
                        throw new CrmException(500, "车牌号不能为空");
                    }
                    aiDregCar.setLicensePlateNumber((String)data.get("licenseNumberPlate"));
                    if (data.get("licensePlateColor") == null || "".equals(data.get("licensePlateColor"))) {
                        throw new CrmException(500, "车牌颜色不能为空");
                    }
                    aiDregCar.setLicensePlateColor((String)data.get("licensePlateColor"));
                    if (data.get("carPhoto") == null || "".equals(data.get("carPhoto"))) {
                        throw new CrmException(500, "车辆照片不能为空");
                    }
                    Map<String, Object> fileData = new HashMap<String, Object>();
                    String entryBathid = UUID.randomUUID().toString().replaceAll("-", "");
                    String imgName= UUID.randomUUID().toString().replaceAll("-", "");
                    String path="";
                    Long size=0L;
                    String file=(String)data.get("carPhoto");
                    file = file.replaceAll(" ", "+");
                    Map<String,Object> Base64Data =  apiUtils.convertBase64ToFile(file,PATH,imgName);
                    if((Base64Data.get("imgName") == null || "".equals(Base64Data.get("imgName")))){
                        throw new CrmException(500, "您上传的图片Base64码有问题请重新上传");
                    }
                    imgName=(String)Base64Data.get("imgName");
                    if(Base64Data.get("path")  == null || "".equals(Base64Data.get("path"))){
                        throw new CrmException(500, "您上传的Base64码有问题请重新上传");
                    }
                    path=(String)Base64Data.get("path");
                    if(Base64Data.get("size") == null || "".equals(Base64Data.get("size"))){
                        throw new CrmException(500, "您上传的图片Base64码有问题请重新上传");
                    }
                    size=(Long)Base64Data.get("size");
                    fileData.put("fileId",MybatisPlusConfig.getNextId() + "");
                    fileData.put("name",imgName);
                    fileData.put("size",size);
                    fileData.put("createUserId","11111");
                    fileData.put("createTime","2022-02-23 12:00:00");
                    fileData.put("path",path);
                    fileData.put("fileType","img");
                    fileData.put("type","1");
                    fileData.put("source","");
                    fileData.put("isPublic","0");
                    fileData.put("batchId",entryBathid);
                    fileData.put("tenantId",projectInfo.getTenantId());
                    labourTeamMapper.saveFile(fileData);
                    aiDregCar.setImg(entryBathid);
                    if (data.get("carType") == null || "".equals(data.get("carType"))) {
                        throw new CrmException(500, "车辆类型不能为空");
                    }
                    aiDregCar.setCarType((String)data.get("carType"));
                    aiDregCar.setCarColor((String)data.get("carColor"));
                    if (data.get("reportTime") == null || "".equals(data.get("reportTime"))) {
                        throw new CrmException(500, "设备数据时间不能为空");
                    }
                    String regdate=(String)data.get("reportTime");
                    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    aiDregCar.setTransferTime(dft.parse(regdate));
                    dataList.add(aiDregCar);
                }
                getBaseMapper().batchSaveDregCarData(dataList);
            }else{
                throw new CrmException(500, "您未传输数据主体！");
            }
            return Result.ok();
        }catch (CrmException e) {
            e.printStackTrace();
            log.error("数据接口错误：" + e);
            throw e;
        } catch (DateException e){
            e.printStackTrace();
            log.error("数据接口错误：" + e);
            throw new CrmException(500, "日期时间格式不符合规范!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("数据接口错误：" + e);
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_SYSTEM_ERROR);
        }
    }
}
