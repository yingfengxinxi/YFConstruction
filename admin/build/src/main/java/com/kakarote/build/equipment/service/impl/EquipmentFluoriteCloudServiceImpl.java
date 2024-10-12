package com.kakarote.build.equipment.service.impl;/**
 * @ClassName EquipmentFluoriteCloudServiceImpl
 * @Author wnj58
 * @Date 2022/6/17 15:30
 * @purpose
 * @Version 1.0
 **/

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.equipment.entity.BO.EquipmentFluoriteCloudBO;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.equipment.entity.PO.EquipmentElevatorEffect;
import com.kakarote.build.equipment.entity.PO.EquipmentFluoriteCloud;
import com.kakarote.build.equipment.mapper.EquipmentElevatorEffectMapper;
import com.kakarote.build.equipment.mapper.EquipmentFluoriteCloudMapper;
import com.kakarote.build.equipment.service.EquipmentFluoriteCloudService;
import com.kakarote.build.equipment.service.IEquipmentAssetsService;
import com.kakarote.build.gb28181.entity.PO.DeviceProjectRelation;
import com.kakarote.build.gb28181.service.YsyDockingService;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wnj58
 * @since 2022-06-17
 */
@Service
public class EquipmentFluoriteCloudServiceImpl extends BaseServiceImpl<EquipmentFluoriteCloudMapper, EquipmentFluoriteCloud> implements EquipmentFluoriteCloudService {

    @Autowired
    private IEquipmentAssetsService assetsService;

    @Override
    public BasePage<Map> getList(EquipmentFluoriteCloudBO equipmentFluoriteCloudBO) {
        BasePage<Map> list=getBaseMapper().getAssetLilst(equipmentFluoriteCloudBO.parse(),equipmentFluoriteCloudBO);
        return list;
    }

    @Override
    public Map getDetails(String id) {
        return getBaseMapper().getDetails(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateState(EquipmentFluoriteCloud equipmentFluoriteCloud) {
        //更新设备名称
        String assetName = equipmentFluoriteCloud.getAssetName();
        EquipmentAssets assets = new EquipmentAssets();
        assets.setAssetId(equipmentFluoriteCloud.getAssetId());
        assets.setAssetName(assetName);
        assetsService.updateById(assets);
        return getBaseMapper().updateState(equipmentFluoriteCloud);
    }

    @Override
    public BasePage<Map> getAIEquipmentList(EquipmentFluoriteCloudBO equipmentFluoriteCloudBO) {
        return getBaseMapper().getAIEquipmentList(equipmentFluoriteCloudBO.parse(),equipmentFluoriteCloudBO);
    }

    @Override
    public boolean addAIEquipment(EquipmentFluoriteCloudBO equipmentFluoriteCloudBO) {
        return getBaseMapper().addAIEquipment(equipmentFluoriteCloudBO);
    }

    @Override
    public boolean delAIEquipment(String id) {
        return getBaseMapper().delAIEquipment(id);
    }

    @Autowired
    private YsyDockingService ysyDockingService;
    /**
     * excel导入萤石设备
     *
     * @param file file
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject excelImport(MultipartFile file) {

        String filePath = getFilePath(file);
        AtomicReference<Integer> num = new AtomicReference<>(0);
        //获取设备最大排序序号
        LambdaQueryWrapper<EquipmentAssets> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(EquipmentAssets::getSort);
        queryWrapper.last("limit 1");
        EquipmentAssets oldAssetMaxSort = assetsService.getOne(queryWrapper);
        //获取设备类型第
        Map map=getBaseMapper().getEquipmentType();
        ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {

            if (rowIndex > 1) {

                EquipmentAssets newAssets=new EquipmentAssets();
                //设备编号
                Object EquipmentNo = rowList.get(0);
                if (EquipmentNo == null || EquipmentNo.equals("")) {
                    throw new CrmException(1001, "设备编号不能为空");
                }else {
                    String[] nos=String.valueOf(EquipmentNo).split("-");
                    if(nos.length<4)throw new CrmException(1003,"格式错误,正确格式为：区划代码+工程类型代码+项目编号+视频位置编码");
                }
                //设备名称
                Object EquipmentName = rowList.get(1);
                if (EquipmentName == null || EquipmentName.equals("")) {
                    throw new CrmException(1001, "设备名称不能为空");
                }
                //品牌
                Object EquipmentBrand  = rowList.get(2);
                if (EquipmentBrand == null || EquipmentBrand.equals("")) {
                    throw new CrmException(1001, EquipmentName + "的品牌不能为空");
                }
                //型号
                Object EquipmentModel = rowList.get(3);
                if (EquipmentModel == null || EquipmentModel.equals("")) {
                    throw new CrmException(1001, EquipmentName + "的型号不能为空");
                }
                //项目id
                Object projectId = rowList.get(8);
                if (projectId == null || projectId.equals("")) {
                    throw new CrmException(1001, EquipmentName + "的项目id不能为空");
                }

                newAssets.setAssetCode(String.valueOf(EquipmentNo).trim());
                newAssets.setAssetName(String.valueOf(EquipmentName).trim());
                newAssets.setTypeId(Integer.valueOf(String.valueOf(map.get("typeId"))));
                newAssets.setBrand(String.valueOf(EquipmentBrand).trim());
                newAssets.setModel(String.valueOf(EquipmentModel).trim());
                newAssets.setStatus("1");

                newAssets.setSort(oldAssetMaxSort.getSort()+10);
                newAssets.setCreateTime(new Date());
                newAssets.setCreateUserId(UserUtil.getUserId());
                newAssets.setTenantId(oldAssetMaxSort.getTenantId());
                newAssets.setProjectId(Integer.valueOf(String.valueOf(projectId).trim()));
                //返回新添加设备的主键（id）
                Integer newAssetId = null;
                int ok=assetsService.getBaseMapper().insert(newAssets);
                if( ok > 0 ) {
                    newAssetId=newAssets.getAssetId();
                    if(newAssetId!=null){
                        String deviceId="33011081992397348766";
                        //IPC序列号
                        Object IPC_serialNumber = rowList.get(4);
                        if (IPC_serialNumber == null || IPC_serialNumber.equals("")) {
                            throw new CrmException(1001, EquipmentName + "的IPC序列号不能为空");
                        }
                        //IPC  license
                        Object IPC_license = rowList.get(5);
                        if (IPC_license == null || IPC_license.equals("")) {
                            throw new CrmException(1001, EquipmentName + "的IPC license不能为空");
                        }
                        //NVR 序列号
                        Object NVR_serialNumber = rowList.get(6);
                        if (NVR_serialNumber == null || NVR_serialNumber.equals("")) {
                            throw new CrmException(1001, EquipmentName + "的NVR序列号不能为空");
                        }
                        //NVR   license
                        Object NVR_license = rowList.get(7);
                        if (NVR_license == null || NVR_license.equals("")) {
                            throw new CrmException(1001, EquipmentName + "的NVR license不能为空");
                        }
                        DeviceProjectRelation dpr = new DeviceProjectRelation();
                        dpr.setDeviceId(deviceId);//国标id
                        dpr.setDeviceChannelId(String.valueOf(IPC_serialNumber).trim());//通道id
                        dpr.setIpcLicense(String.valueOf(IPC_license).trim());//通道密码
                        dpr.setNvrLicense(String.valueOf(NVR_license).trim());//录像及密码
//                      dpr.getDeviceChannelNo();//通道编号
                        dpr.setDeviceSerial(deviceId+":"+String.valueOf(NVR_serialNumber));//设置萤石录像机序列号
                        dpr.setBusinessId(String.valueOf(newAssetId));//设备id
                        dpr.setType("2");//
                        dpr.setStatus("1");//是否在线，默认在线
                        dpr.setIsEnable("1");//是否启用，默认启用
                        dpr.setHookCheckBox("0");//是否吊钩，默认否
                        dpr.setPtzCheckBox("0");//是否云台控制，默认否
                        dpr.setIsAiEquipment("0");//是否AI设备，默认否
//                      dpr.setAiMonitorType("");
                        dpr.setSort(rowIndex-1);//排序
                        dpr.setCreateTime(new Date());
                        dpr.setTenantId(oldAssetMaxSort.getTenantId());//租户
                        dpr.setProjectId(Integer.valueOf(String.valueOf(projectId).trim()));
                        ysyDockingService.getBaseMapper().insert(dpr);//保存设备与萤石设备关系
                        num.set(num.get()+1);
                    }else{
                        throw new CrmException(1002,"设备id信息获取失败");
                    }
                }else{
                    throw new CrmException(1001,"保存设备信息失败");
                }

            }
        });
        FileUtil.del(filePath);
        JSONObject result = new JSONObject().fluentPut("totalSize", num.get()).fluentPut("errSize", 0);
        return result;
    }



    private String getFilePath(MultipartFile file1) {
        String dirPath = FileUtil.getTmpDirPath();
        try {
            InputStream inputStream1 = file1.getInputStream();
            File fromStream1 = FileUtil.writeFromStream(inputStream1, dirPath + "/" + IdUtil.simpleUUID() + file1.getOriginalFilename());
            return fromStream1.getAbsolutePath();
        } catch (IOException e) {
            throw new CrmException(SystemCodeEnum.SYSTEM_UPLOAD_FILE_ERROR);
        }
    }

}
