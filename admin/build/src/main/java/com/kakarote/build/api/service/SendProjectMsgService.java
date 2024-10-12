package com.kakarote.build.api.service;

import cn.hutool.json.JSONObject;
import com.kakarote.build.project.entity.PO.ProjectUser;
import com.kakarote.build.project.service.IProjectUserService;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.message.MessageEntity;
import com.kakarote.core.message.MessageFeignService;
import com.kakarote.core.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * buil服务  推送项目消息Service
 * 注意此推送接口仅供项目相关信息使用
 */
@Component
@Slf4j
public class SendProjectMsgService {
    @Autowired
    private MessageFeignService messageFeignService;
    @Autowired
    IProjectUserService projectUserService;

    /**
     * 消息推送
     *
     * @param messageEntity 消息体
     */
    public void send(MessageEntity messageEntity) {
        send(messageEntity, null);
    }

    /**
     * 消息推送
     *
     * @param messageEntity 消息体
     * @param ifSendGrants  是否推送项目授权人
     */
    public void send(MessageEntity messageEntity, Boolean ifSendGrants) {
        List<Long> toUsers = messageEntity.getToUsers();
        if (toUsers == null) {
            toUsers = new ArrayList<>();
        }
        //获取消息配置人员
        Integer tenantId = messageEntity.getTenantId();
        if(tenantId == null){
            UserInfo userInfo = UserUtil.getUser();
            if(userInfo==null){
                log.error("MessageCenter:", "错误参数 => tenantId is null");
                return;
            }
            messageEntity.setTenantId(userInfo.getTenantId().intValue());
        }
        List<Long> configUsers = messageFeignService.getQueuesUserList(messageEntity.getCode(), messageEntity.getTenantId());
        for (Long userId : configUsers) {
            toUsers.add(userId);
        }
        //是否推送项目授权人
        if ((ifSendGrants == null || !ifSendGrants) && toUsers.size()>0) {
            messageEntity.setToUsers(toUsers);
            messageFeignService.send(messageEntity);
            return;
        }
        //获取项目授权人
        Long projectId = messageEntity.getProjectId();
        if(projectId == null){
            UserInfo userInfo = UserUtil.getUser();
            if(userInfo==null){
                log.error("MessageCenter:", "错误参数 => projectId is null");
                return;
            }
            messageEntity.setProjectId(userInfo.getProjectId());
        }
        List<ProjectUser> userList = projectUserService.lambdaQuery()
                .eq(ProjectUser::getProjectId, projectId)
                .eq(ProjectUser::getTenantId, messageEntity.getTenantId()).list();
        for (ProjectUser user : userList) {
            toUsers.add(Long.getLong(String.valueOf(user.getUserId())));
        }
        if (toUsers.size() > 0) {
            messageEntity.setToUsers(toUsers);
            messageFeignService.send(messageEntity);
        }else {
            log.error("MessageCenter:", "错误参数 => toUsers is null："+new JSONObject(messageEntity).toString());
        }
    }
}
