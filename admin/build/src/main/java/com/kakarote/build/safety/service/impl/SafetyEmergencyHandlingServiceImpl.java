package com.kakarote.build.safety.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.safety.entity.BO.SafetyEmergencyHandlingBO;
import com.kakarote.build.safety.entity.PO.SafetyEmergencyHandling;
import com.kakarote.build.safety.entity.PO.SafetyEmergencyHandlingDetail;
import com.kakarote.build.safety.mapper.SafetyEmergencyHandlingMapper;
import com.kakarote.build.safety.service.ISafetyEmergencyHandlingDetailService;
import com.kakarote.build.safety.service.ISafetyEmergencyHandlingService;
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

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 应急事件处置 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-04-06
 */
@Service
public class SafetyEmergencyHandlingServiceImpl extends BaseServiceImpl<SafetyEmergencyHandlingMapper, SafetyEmergencyHandling> implements ISafetyEmergencyHandlingService {
    @Autowired
    private ISafetyEmergencyHandlingDetailService iSafetyEmergencyHandlingDetailService;

    @Autowired
    private MessageFeignService messageFeignService;

    @Override
    public BasePage<SafetyEmergencyHandling> selectList(SafetyEmergencyHandlingBO safetyEmergencyHandlingBO) {
        safetyEmergencyHandlingBO.setOrdersBd("a.create_time",false);
        safetyEmergencyHandlingBO.setOrdersBd("a.happened_time",false);
        return getBaseMapper().selectByPage(safetyEmergencyHandlingBO.parse(),safetyEmergencyHandlingBO);
    }

    @Override
    public String saveData(SafetyEmergencyHandling safetyEmergencyHandling) {
        boolean flag = save(safetyEmergencyHandling);
        if(flag){
            //应急事件消息推送
            try{
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setByUser(UserUtil.getUserId());
                messageEntity.setTitle(safetyEmergencyHandling.getCode());
                messageEntity.setData(String.valueOf(safetyEmergencyHandling.getId()));

//                String toUserIds = safetyEmergencyHandling.getToUserIds();
//                String[] ids = toUserIds.split(",");
//
//                Long[] str2 = new Long[ids.length];
//                for (int i = 0; i < ids.length; i++) {
//                    str2[i] = Long.valueOf(ids[i]);
//                }
                List<Long> toUsers = messageFeignService.getQueuesUserList(MessageType.MESSAGE_SAFETY_EMERGENCY);
//                System.out.println("====toUsers:"+ Arrays.toString(toUsers.toArray()));
                messageEntity.setToUsers(toUsers);//指定消息推送人
                messageEntity.setCode(MessageType.MESSAGE_SAFETY_EMERGENCY);
                messageFeignService.send(messageEntity);
            }catch (Exception e){
                e.printStackTrace();
                log.error("socket:应急事件消息推送");
            }
            return safetyEmergencyHandling.getId();
        }
        return "";
    }

    @Override
    public String updateData(SafetyEmergencyHandling safetyEmergencyHandling) {
        boolean flag = updateById(safetyEmergencyHandling);
        if(flag){
            //应急事件消息推送
            try{
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setByUser(UserUtil.getUserId());
                messageEntity.setTitle(safetyEmergencyHandling.getCode());
                messageEntity.setData(String.valueOf(safetyEmergencyHandling.getId()));

                String toUserIds = safetyEmergencyHandling.getToUserIds();
                String[] ids = toUserIds.split(",");

                Long[] str2 = new Long[ids.length];
                for (int i = 0; i < ids.length; i++) {
                    str2[i] = Long.valueOf(ids[i]);
                }

                messageEntity.setToUser(str2);//指定消息推送人
                messageEntity.setCode(MessageType.MESSAGE_SAFETY_EMERGENCY);
                messageFeignService.send(messageEntity);
            }catch (Exception e){
                e.printStackTrace();
                log.error("socket:应急事件消息推送");
            }
            return safetyEmergencyHandling.getId();
        }

        return "";
    }

    @Override
    @Transactional
    public Result updateByZHIdMap(Map<String, Object> map) {
        Map<String, Object> zhub = (Map<String, Object>)map.get("ZHUB");
        List<Map<String, Object>> zib = (List<Map<String, Object>>)map.get("ZIB");

        if(zhub == null){
            throw new CrmException(1001, "主表参数不存在");
        }
//        if(zib == null){
//            throw new CrmException(1001, "子表参数不存在");
//        }

        //修改或者新增主表数据
        SafetyEmergencyHandling info = JSON.parseObject(JSON.toJSONString(zhub), SafetyEmergencyHandling.class);
        if(info.getId() == null || info.getId().equals("")){
            getBaseMapper().insert(info);
        }else {
            getBaseMapper().updateById(info);

            //根据主表id删除子表数据
            QueryWrapper<SafetyEmergencyHandlingDetail> wrapper = new QueryWrapper<>();
            wrapper.eq("pid", info.getId());
            iSafetyEmergencyHandlingDetailService.remove(wrapper);

        }

        //新增子表数据
        for(int i = 0;i < zib.size(); i++){
            SafetyEmergencyHandlingDetail infoDetail = JSON.parseObject(JSON.toJSONString(zib.get(i)), SafetyEmergencyHandlingDetail.class);
            infoDetail.setPid(info.getId());
            infoDetail.setTenantId(null);
            iSafetyEmergencyHandlingDetailService.save(infoDetail);
        }

        return null;
    }
}
