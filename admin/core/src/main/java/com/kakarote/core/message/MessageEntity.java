package com.kakarote.core.message;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 消息推送实体
 */
@Data
public class MessageEntity {
    private static final long serialVersionUID=1L;

    private String code;
    /**
     * 模板参数
     * 举例：
     *  消息模板：{0}给你发送了消息，标题为《{1}》
     *  模板参数：["张三","2021年度年中总结会议通知"]
     *  实际展示效果：张三给你发送了消息，标题为《2021年度年中总结会议通知》
     */
    private String[] titleParams;

    private String title;

    private String msgContent;

    private String data;

    /**
     * 业务类型：若是业务审批，此属性必传
     */
    private Integer businessType;
    /**
     * 发送人 为空时，默认为admin
     */
    private Long byUser;
    /**
     * 接收人
     */
    private List<Long> toUsers;
    /**
     * 项目ID（仅项目类消息有效）
     */
    private Long projectId;
    /**
     * 租户ID（仅项目类消息有效）
     */
    private Integer tenantId;
    /**
     * 源数据ID：主要考虑源数据需要关联消息记录时使用  2022-12-06  改为必填项
     */
    private String batchId;
    @Override
    public String toString(){
        JSONObject json = new JSONObject();
        json.put("type",code);
        json.put("title",title);
        json.put("data",data);
        json.put("businessType",businessType);
        json.put("batchId",batchId);
        json.put("byUser",byUser);
        json.put("toUsers",toUsers);
        json.put("projectId",projectId);
        json.put("tenantId",tenantId);
        json.put("msgContent",msgContent);
        return json.toJSONString();
    }

    public void setToUsers(List<Long> toUsers) {
        if(toUsers != null) {
            HashSet<Long> hashSet = new HashSet<>(toUsers);
            this.toUsers = new ArrayList<>(hashSet);
        }
    }

    public void setToUser(Long...toUsers) {
        this.toUsers = Arrays.asList(toUsers);
    }

    public String getMesTitle(){
        switch (this.code){
            case MessageType.NOTICE_NOTICE:
                return  "系统公告";
            case MessageType.MESSAGE_EXAMINE_SUBMIT:
                return  "审批提交";
            case MessageType.MESSAGE_EXAMINE_PASS:
                return  "审批通过";
            case MessageType.MESSAGE_EXAMINE_REFUSE:
                return  "审批拒绝";
            case MessageType.MESSAGE_SAFETY_EMERGENCY:
                return  "应急事件";
            case MessageType.ALARM_EQU_ENVRMT:
                return  "环境报警";
            case MessageType.ALARM_EQU_TOWER:
                return  "塔机报警";
            case MessageType.MESSAGE_SECURITY_RISKS:
                return  "安全隐患超期";
            case MessageType.EQUI_OFFLINE_TIMEOUT:
                return  "设备离线超时";
            case MessageType.MESSAGE_SECURITY_RISKS_DETECTION:
                return  "安全隐患排查";
        }
        return "";
    }
}
