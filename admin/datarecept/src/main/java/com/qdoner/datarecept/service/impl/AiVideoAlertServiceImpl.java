package com.qdoner.datarecept.service.impl;
import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminDictData;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.qdoner.datarecept.entity.PO.AiVideoAlert;
import com.qdoner.datarecept.entity.PO.BEquipmentAssets;
import com.qdoner.datarecept.entity.PO.BMonitorPointAlert;
import com.qdoner.datarecept.entity.PO.MonitorPointAlert;
import com.qdoner.datarecept.mapper.AiVideoAlertMapper;
import com.qdoner.datarecept.service.EqStatusCommonService;
import com.qdoner.datarecept.service.IAiVideoAlertService;
import com.qdoner.datarecept.service.IBEquipmentAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shao
 * @since 2021-05-24
 */
@Service
public class AiVideoAlertServiceImpl extends BaseServiceImpl<AiVideoAlertMapper, AiVideoAlert> implements IAiVideoAlertService {

    @Autowired
    private IBEquipmentAssetsService equipmentAssetsService;

    @Autowired
    private EqStatusCommonService eqStatusCommonService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result aiRecognition(List<Map<String, Object>> list) {

        try {
            List<AiVideoAlert> aiVideoAlertList = new ArrayList<>();
            // 封装报警数据 用来进行设备报警推送
            List<BMonitorPointAlert> alertList = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                if (StringUtils.isEmpty(map.get("deviceSN"))) {
                    throw new CrmException(500, "第" + (i+1) + "条数据设备序列号不能为空！");
                }
                if (StringUtils.isEmpty(map.get("monitorDateTime"))) {
                    throw new CrmException(500, "第" + (i+1) + "条数据发送时间不能为空！");
                }
                if (StringUtils.isEmpty(map.get("eventHappenTime"))) {
                    throw new CrmException(500, "第" + (i+1) + "条数据发生时间不能为空！");
                }
                if (StringUtils.isEmpty(map.get("eventType"))) {
                    throw new CrmException(500, "第" + (i+1) + "条数据事件类型不能为空！");
                }
                if (StringUtils.isEmpty(map.get("eventPicUrl"))) {
                    throw new CrmException(500, "第" + (i+1) + "条数据时间图片URL不能为空！");
                }

                // 根据设备id查询设备数据
                BEquipmentAssets equipmentAssets = equipmentAssetsService.selectByAssetId(Integer.valueOf(map.get("deviceSN") + ""));
                if (equipmentAssets == null) {
                    throw new CrmException(500, "第" + (i+1) + "条数据设备不存在，请确定设备序列号是否正确！");
                }

                // 查询指定时间内是否存在报警数据，如果存在不保存新数据.目前1小时
                Integer dataNum = this.baseMapper.getDataByEventTypeAndTime(equipmentAssets.getAssetId(),
                        String.valueOf(map.get("eventType")), 1);
                if (dataNum > 0) {
                    continue;
                }


                AiVideoAlert aiVideoAlert = new AiVideoAlert();
                aiVideoAlert.setEquipmentId(equipmentAssets.getAssetId());
                aiVideoAlert.setProjectId(equipmentAssets.getProjectId());
                aiVideoAlert.setTenantId(equipmentAssets.getTenantId());
                try {
                    DateTime monitorDateTime = DateUtil.parse(String.valueOf(map.get("monitorDateTime")));
                    aiVideoAlert.setCreateTime(monitorDateTime);
                }catch (DateException e) {
                    throw new CrmException(500, "第" + (i+1) + "条数据发送时间格式不正确！");
                }

                try {
                    DateTime eventHappenTime = DateUtil.parse(String.valueOf(map.get("eventHappenTime")));
                    aiVideoAlert.setAlertTime(eventHappenTime);
                }catch (DateException e) {
                    throw new CrmException(500, "第" + (i+1) + "条数据发生时间格式不正确！");
                }
                aiVideoAlert.setAiEventType(String.valueOf(map.get("eventType")));
                aiVideoAlert.setAlertImg(String.valueOf(map.get("eventPicUrl")));
                aiVideoAlert.setAlertType("1");
                aiVideoAlert.setAlertContent((String) map.get("alertContent"));
                aiVideoAlert.setAlertPosition((String) map.get("alertPosition"));

                aiVideoAlertList.add(aiVideoAlert);

                BMonitorPointAlert monitorPointAlert = new BMonitorPointAlert();
                monitorPointAlert.setEquipmentId(equipmentAssets.getAssetId());
                monitorPointAlert.setContent("AI识别"+aiVideoAlert.getAlertContent());
                alertList.add(monitorPointAlert);
            }
            // 批量保存
            this.saveBatch(aiVideoAlertList);

            for (BMonitorPointAlert bMonitorPointAlert : alertList) {
                eqStatusCommonService.sendMsgEquipment(bMonitorPointAlert);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("打印错误：" + e);
            throw e;
        }


        return Result.ok();
    }
}
