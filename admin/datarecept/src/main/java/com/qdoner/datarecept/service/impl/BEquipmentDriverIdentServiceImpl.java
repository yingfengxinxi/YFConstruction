package com.qdoner.datarecept.service.impl;

import cn.hutool.core.date.DateException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.qdoner.datarecept.commons.EnvMonitorCodeEnum;
import com.qdoner.datarecept.entity.PO.BEquipmentAssets;
import com.qdoner.datarecept.entity.PO.BEquipmentDriverIdent;
import com.qdoner.datarecept.entity.PO.ProjectInfo;
import com.qdoner.datarecept.mapper.BEquipmentAssetsMapper;
import com.qdoner.datarecept.mapper.BEquipmentDriverIdentMapper;
import com.qdoner.datarecept.service.EqStatusCommonService;
import com.qdoner.datarecept.service.IBEquipmentDriverIdentService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.qdoner.datarecept.service.IProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 机械设备司机识别 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
@Service
public class BEquipmentDriverIdentServiceImpl extends BaseServiceImpl<BEquipmentDriverIdentMapper, BEquipmentDriverIdent> implements IBEquipmentDriverIdentService {

    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private EqStatusCommonService statusCommonService;

    @Autowired
    private BEquipmentAssetsServiceImpl equipmentAssetsService;

    @Override
    public Result saveData(List<Map<String, Object>> list) {
        try {

            if(list != null && list.size()>0){
                List<BEquipmentDriverIdent> dataList = new ArrayList<>();
                for(Map data:list){
                    BEquipmentDriverIdent equipmentDriverIdent = new BEquipmentDriverIdent();
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
                    equipmentDriverIdent.setTenantId(projectInfo.getTenantId());
                    equipmentDriverIdent.setProjectId(projectId);
                    if (data.get("deviceSn") == null || "".equals(data.get("deviceSn"))) {
                        throw new CrmException(500, "设备编号不能为空");
                    }
                    BEquipmentAssets equipmentAssets = equipmentAssetsService.selectByAssetId(Integer.valueOf(data.get("deviceSn")+""));
                    equipmentDriverIdent.setEquCode(equipmentAssets.getAssetCode());
                    if (data.get("deviceType") == null || "".equals(data.get("deviceType"))) {
                        throw new CrmException(500, "设备类型不能为空");
                    }
                    equipmentDriverIdent.setDeviceType(data.get("deviceType")+"");
                    if (data.get("idCard") == null || "".equals(data.get("idCard"))) {
                        throw new CrmException(500, "身份证号不能为空");
                    }
                    equipmentDriverIdent.setIdCard(data.get("idCard")+"");
                    equipmentDriverIdent.setCertificateId( data.get("specialNo")+"");

                    if (data.get("driverDate") == null || "".equals(data.get("driverDate"))) {
                        throw new CrmException(500, "设备数据时间不能为空");
                    }
                    String regdate=(String)data.get("driverDate");
                    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    equipmentDriverIdent.setStartingDate(dft.parse(regdate));
                    String lastOperationDate=(String)data.get("lastOperationDate");
                    if(lastOperationDate != null){
                        equipmentDriverIdent.setEndingDate(dft.parse(lastOperationDate));
                    }
                    equipmentDriverIdent.setPhotoUrl((String) data.get("photoUrl"));
                    equipmentDriverIdent.setCreateTime(new Date());
                    //getBaseMapper().insert(equipmentDriverIdent);
                    dataList.add(equipmentDriverIdent);
                }
                getBaseMapper().batchSaveData(dataList);
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

    @Override
    public Result offLine(List<Map<String, Object>> list) {
        try {

            if(list != null && list.size()>0){
                for(Map data:list){
                    BEquipmentDriverIdent equipmentDriverIdent = new BEquipmentDriverIdent();
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
                    equipmentDriverIdent.setTenantId(projectInfo.getTenantId());
                    equipmentDriverIdent.setProjectId(projectId);
                    if (data.get("deviceSn") == null || "".equals(data.get("deviceSn"))) {
                        throw new CrmException(500, "设备编号不能为空");
                    }
                    equipmentDriverIdent.setEquCode(data.get("deviceSn")+"");
                    if (data.get("endingDate") == null || "".equals(data.get("endingDate"))) {
                        throw new CrmException(500, "离线时间不能为空");
                    }
                    String regdate=(String)data.get("endingDate");
                    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date monitorTime = dft.parse(regdate);
                    equipmentDriverIdent.setEndingDate(monitorTime);
                    BEquipmentDriverIdent driverIdent =  getBaseMapper().getDriverIdentByCode(equipmentDriverIdent.getEquCode());
                    if(driverIdent.getEndingDate() == null){
                        driverIdent.setEndingDate(equipmentDriverIdent.getEndingDate());
                        getBaseMapper().updateById(driverIdent);
                    }
                    statusCommonService.handleOfflineSatus(Integer.valueOf(equipmentDriverIdent.getEquCode()),"0");
                }
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
