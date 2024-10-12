package com.kakarote.build.safety.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.kakarote.build.safety.entity.BO.SafetyInspectionInfoBO;
import com.kakarote.build.safety.entity.PO.SafetyInspectionDetail;
import com.kakarote.build.safety.entity.PO.SafetyInspectionInfo;
import com.kakarote.build.safety.mapper.SafetyInspectionInfoMapper;
import com.kakarote.build.safety.service.ISafetyInspectionDetailService;
import com.kakarote.build.safety.service.ISafetyInspectionInfoService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.message.MessageEntity;
import com.kakarote.core.message.MessageFeignService;
import com.kakarote.core.message.MessageType;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工安全--安全巡检信息 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-04-08
 */
@Service
public class SafetyInspectionInfoServiceImpl extends BaseServiceImpl<SafetyInspectionInfoMapper, SafetyInspectionInfo> implements ISafetyInspectionInfoService {

    @Autowired
    private ISafetyInspectionDetailService inspectionDetailService;

    @Autowired
    private MessageFeignService messageFeignService;

    @Override
    public BasePage<SafetyInspectionInfo> selectByPage(SafetyInspectionInfoBO inspectionInfoBO) {
        inspectionInfoBO.setOrdersBd("bsii.execute_date", false);
        BasePage<SafetyInspectionInfo> list = this.getBaseMapper().selectByPage(inspectionInfoBO.parse(), inspectionInfoBO);

        // 判断巡检是否已经执行，isExecute 执行状态 0:未执行 1:已执行 默认为0
        for (SafetyInspectionInfo inspectionInfo:list.getList()) {
            // 如果巡检已执行，判断他的整改状态
            if ("1".equals(inspectionInfo.getIsExecute())) {
                // 根据整改时间是否为空，判断是否进行过整改
                LambdaQueryWrapper<SafetyInspectionDetail> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SafetyInspectionDetail::getInspectionInfoId, inspectionInfo.getId());
                queryWrapper.isNull(true, SafetyInspectionDetail::getRectifyDate);
                List<SafetyInspectionDetail> detailList = inspectionDetailService.list(queryWrapper);

                if (detailList.size() > 0) {
                    inspectionInfo.setRectifyStatus("1");
                } else {
                    inspectionInfo.setRectifyStatus("2");
                }
            }
        }
        return list;
    }

    @Override
    public Map<String, Object> selectDataById(Integer id) {
        Map<String, Object> data = new HashMap<>();

        // 查询安全巡检基本信息
        SafetyInspectionInfo inspectionInfo = getBaseMapper().getInspectionInfo(id);
        // 查询安全巡检明细
        List<SafetyInspectionDetail> inspectionDetailList = inspectionDetailService.lambdaQuery()
                .eq(SafetyInspectionDetail::getInspectionInfoId, id).list();

        data.put("inspectionInfo", inspectionInfo);
        data.put("inspectionDetailList", inspectionDetailList);

        return data;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateInspectionInfo(Map<String, Object> params) {
        // 获取数据
        Map<String, Object> inspectionInfo = (Map<String, Object>) params.get("inspectionInfo");
        List<Map<String, Object>> inspectionDetailList = (List<Map<String, Object>>) params.get("inspectionDetailList");

        if (inspectionInfo == null) {
            throw new CrmException(1001, "inspectionInfo参数不存在");
        }

        if (inspectionDetailList == null || inspectionDetailList.size() <= 0) {
            throw new CrmException(1001, "inspectionDetailList参数不存在");
        }

        // 保存巡检基本信息
        SafetyInspectionInfo info = JSON.parseObject(JSON.toJSONString(inspectionInfo), SafetyInspectionInfo.class);
        // 设置执行状态
        info.setIsExecute("1");

        Integer infoId = info.getId();
        if (infoId == null) {
            throw new CrmException(1001, "基本信息Id不存在");
        }
        getBaseMapper().updateById(info);

        // 先删除数据
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("inspection_info_id", infoId);
        inspectionDetailService.remove(queryWrapper);
        // 保存巡检明细
        for (Map<String, Object> map: inspectionDetailList) {
            SafetyInspectionDetail detail = JSON.parseObject(JSON.toJSONString(map), SafetyInspectionDetail.class);
            detail.setInspectionInfoId(infoId);
            detail.setPublishDate(new Date());
            inspectionDetailService.save(detail);
        }

        return infoId;
    }

    @Override
    public Map<String, Object> getPrintData(Integer id) {

        // 查询安全巡检基本信息
        Map<String, Object> inspectionInfo = getBaseMapper().getPrintData(id);
        // 查询安全巡检明细
        List<SafetyInspectionDetail> inspectionDetailList = inspectionDetailService.lambdaQuery()
                .eq(SafetyInspectionDetail::getInspectionInfoId, id).list();

        // 将子表数据添加进map
        inspectionInfo.put("detailList", inspectionDetailList);

        return inspectionInfo;
    }

    /**
     * 集团数据总览-隐患-四个块数据
     * @return
     */
    @Override
    public Map<String, Object> getCount(Integer projectId) {
        Map<String, Object> count = getBaseMapper().getCountMapper(projectId);
        return count;
    }

    @Override
    public BasePage<Map<String, Object>> selectTimeoutList(SafetyInspectionInfoBO inspectionInfoBO) {
        BasePage<Map<String, Object>> list = getBaseMapper().selectTimeoutList(inspectionInfoBO.parse(), inspectionInfoBO);
        return list;
    }

    @Override
    public Result dangeTimeoutPush(Integer id, Integer detailId) {
        // 查询
        SafetyInspectionInfo inspectionInfo = getBaseMapper().selectById(id);
        if (inspectionInfo == null) {
            throw new CrmException(500, "安全巡检信息不存在");
        }

        // 查询详情信息
        SafetyInspectionDetail inspectionDetail = inspectionDetailService.getById(detailId);
        if (inspectionDetail == null) {
            throw new CrmException(500, "安全巡检隐患不存在");
        }

        // 推送消息
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setByUser(UserUtil.getUserId());
        messageEntity.setTitle(inspectionDetail.getProblemDescription());
        messageEntity.setData(String.valueOf(inspectionInfo.getId()));
        List<Long> toUsers = messageFeignService.getQueuesUserList(MessageType.MESSAGE_SECURITY_RISKS);
        messageEntity.setToUsers(toUsers);//指定消息推送人
        messageEntity.setCode(MessageType.MESSAGE_SECURITY_RISKS);
        messageFeignService.send(messageEntity);
        return Result.ok();
    }
}
