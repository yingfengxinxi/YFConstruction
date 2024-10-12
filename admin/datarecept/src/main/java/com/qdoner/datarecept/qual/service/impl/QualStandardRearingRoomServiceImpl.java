package com.qdoner.datarecept.qual.service.impl;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminDictData;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.qdoner.datarecept.entity.PO.BEquipmentAssets;
import com.qdoner.datarecept.entity.PO.BMonitorPointAlert;
import com.qdoner.datarecept.qual.entity.PO.QualStandardRearingRoom;
import com.qdoner.datarecept.qual.mapper.QualStandardRearingRoomMapper;
import com.qdoner.datarecept.qual.service.IQualStandardRearingRoomService;
import com.qdoner.datarecept.service.EqStatusCommonService;
import com.qdoner.datarecept.service.IBEquipmentAssetsService;
import com.qdoner.datarecept.service.IBMonitorPointAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 标养室监测信息 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2023-05-07
 */
@Service
public class QualStandardRearingRoomServiceImpl extends BaseServiceImpl<QualStandardRearingRoomMapper, QualStandardRearingRoom> implements IQualStandardRearingRoomService {

    @Autowired
    private IBEquipmentAssetsService equipmentAssetsService;

    @Autowired
    private IBMonitorPointAlertService monitorPointAlertService;

    @Autowired
    private EqStatusCommonService statusCommonService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result   standardRearinaRoom(List<Map<String, Object>> list) {

        try {

            List<QualStandardRearingRoom> standardRearingRoomList = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                List<BMonitorPointAlert> alertList = new ArrayList<>();
                Map<String, Object> map = list.get(i);
                if (StringUtils.isEmpty(map.get("deviceSN"))) {
                    throw new CrmException(500, "第" + (i+1) + "条数据设备序列号不能为空！");
                }
                if (StringUtils.isEmpty(map.get("monitorDateTime"))) {
                    throw new CrmException(500, "第" + (i+1) + "条数据发送时间不能为空！");
                }
                if (StringUtils.isEmpty(map.get("temperature"))) {
                    throw new CrmException(500, "第" + (i+1) + "条数据温度不能为空！");
                }
                if (StringUtils.isEmpty(map.get("humidity"))) {
                    throw new CrmException(500, "第" + (i+1) +"条数据湿度不能为空！");
                }
                if (StringUtils.isEmpty(map.get("warntype"))) {
                    throw new CrmException(500, "第" + (i+1) + "条数据报警类型不能为空！");
                }

                // 根据设备编号查询设备id、租户id和项目id
                BEquipmentAssets equipmentAssets = equipmentAssetsService.selectByAssetId(Integer.valueOf(map.get("deviceSN") + ""));
                if (equipmentAssets == null) {
                    throw new CrmException(500, "第" + (i+1) + "条数据设备不存在，请确定设备序列号是否正确！");
                }

                QualStandardRearingRoom qualStandardRearingRoom = new QualStandardRearingRoom();
                qualStandardRearingRoom.setEquipmentId(equipmentAssets.getAssetId());
                qualStandardRearingRoom.setTenantId(equipmentAssets.getTenantId());
                qualStandardRearingRoom.setProjectId(equipmentAssets.getProjectId());
                // 没有报警数据时的更新条件
                BMonitorPointAlert alertUpdateObj = new BMonitorPointAlert();
                alertUpdateObj.setEquipmentId(equipmentAssets.getAssetId());
                alertUpdateObj.setTenantId(equipmentAssets.getTenantId());
                alertUpdateObj.setProjectId(equipmentAssets.getProjectId());

                try {
                    DateTime monitorDateTime = DateUtil.parse(String.valueOf(map.get("monitorDateTime")));
                    qualStandardRearingRoom.setMonitorDataTime(monitorDateTime);
                    qualStandardRearingRoom.setCreateTime(monitorDateTime);
                }catch (DateException e) {
                    throw new CrmException(500, "第" + (i+1) + "条数据发送时间格式不正确！");
                }

                if (!(map.get("temperature") instanceof Double)) {
                    throw new CrmException(500, "第" + (i+1) + "条数据温度类型不正确！" );
                }
                qualStandardRearingRoom.setTemperature((Double) map.get("temperature"));
                if (!(map.get("humidity") instanceof Double)) {
                    throw new CrmException(500, "第" + (i+1) + "条数据湿度类型不正确！" );
                }
                qualStandardRearingRoom.setHumidity((Double) map.get("humidity"));
                qualStandardRearingRoom.setWarnType(String.valueOf(map.get("warntype")));
                standardRearingRoomList.add(qualStandardRearingRoom);

                // 不等于99的为报警数据
                if (!"99".equals(String.valueOf(map.get("warntype")))) {

                    BMonitorPointAlert monitorPointAlert = new BMonitorPointAlert();
                    BMonitorPointAlert monitorPointAlert2 = null;

                    monitorPointAlert.setEquipmentId(equipmentAssets.getAssetId());
                    monitorPointAlert.setProjectId(equipmentAssets.getProjectId());
                    monitorPointAlert.setTenantId(equipmentAssets.getTenantId());
                    monitorPointAlert.setAlertType("1");
                    monitorPointAlert.setHandleState("0");
                    DateTime monitorDateTime = DateUtil.parse(String.valueOf(map.get("monitorDateTime")));
                    monitorPointAlert.setCreateTime(monitorDateTime);

                    // 判断报警类型
                    if ("01".equals(String.valueOf(map.get("warntype")))) {
                        monitorPointAlert.setMonitorData(String.valueOf(qualStandardRearingRoom.getTemperature()));
                        monitorPointAlert.setContent("标养室温度报警");
                    } else if ("02".equals(String.valueOf(map.get("warntype")))) {
                        monitorPointAlert.setMonitorData(String.valueOf(qualStandardRearingRoom.getHumidity()));
                        monitorPointAlert.setContent("标养室湿度报警");
                    } else if ("03".equals(String.valueOf(map.get("warntype")))) {

                        monitorPointAlert.setMonitorData(String.valueOf(qualStandardRearingRoom.getTemperature()));
                        monitorPointAlert.setContent("标养室温度报警");

                        // 拷贝数据
                        monitorPointAlert2 = new BMonitorPointAlert();
                        monitorPointAlert2 = this.copyPastry(monitorPointAlert);
                        monitorPointAlert2.setMonitorData(String.valueOf(qualStandardRearingRoom.getHumidity()));
                        monitorPointAlert2.setContent("标养室湿度报警");
                    } else {
                        // 跳过循环
                        continue;
                    }
                    alertList.add(monitorPointAlert);
                    if (monitorPointAlert2 != null) {
                        alertList.add(monitorPointAlert2);
                    }
                }

                // 设备上限
                statusCommonService.handleOfflineSatus(equipmentAssets.getAssetId(),"1");

                // 保存报警数据
                if(alertList.size()>0){
                    monitorPointAlertService.saveRoomAlertByList(alertList);
                } else {
                    if(alertUpdateObj != null){
                        monitorPointAlertService.updataAlert(alertUpdateObj);
                    }
                }
            }
            // 批量保存
            this.saveBatch(standardRearingRoomList);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("打印错误：" + e);
            throw e;
        }

        return R.ok();
    }

    private BMonitorPointAlert copyPastry(BMonitorPointAlert copyObj){
        BMonitorPointAlert mMonitorPointAlert = new BMonitorPointAlert();
        mMonitorPointAlert.setProjectId(copyObj.getProjectId());
        mMonitorPointAlert.setTenantId(copyObj.getTenantId());
        mMonitorPointAlert.setEquipmentId(copyObj.getEquipmentId());
        mMonitorPointAlert.setAlertType(copyObj.getAlertType());
        mMonitorPointAlert.setCreateTime(copyObj.getCreateTime());
        mMonitorPointAlert.setHandleState("0");
        return mMonitorPointAlert;
    }
}
