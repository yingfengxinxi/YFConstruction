package com.qdoner.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.api.UserApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.CidAliasListDTO;
import com.getui.push.v2.sdk.dto.req.Settings;
import com.getui.push.v2.sdk.dto.req.Strategy;
import com.getui.push.v2.sdk.dto.req.message.PushBatchDTO;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.message.MessageEntity;
import com.kakarote.core.utils.UserUtil;
import com.qdoner.message.common.MqManageService;
import com.qdoner.message.common.SocketIOService;
import com.qdoner.message.common.WebSocketService;
import com.qdoner.message.entity.PO.AssignUser;
import com.qdoner.message.entity.PO.Message;
import com.qdoner.message.entity.PO.Queues;
import com.qdoner.message.service.IAssignUserService;
import com.qdoner.message.service.IMService;
import com.qdoner.message.service.IMessageService;
import com.qdoner.message.service.IQueuesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.*;

@Slf4j
@Service
public class MServiceImpl implements IMService {
    @Autowired
    private MqManageService sendMq;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IQueuesService queuesService;

    @Autowired
    private SocketIOService socketIOService;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private IAssignUserService assignUserService;

    /**
     * 将消息转发至交换机，并保存记录
     *
     * @param messageEntity
     * @return
     */
    @Override
    public Result send(MessageEntity messageEntity) {
        System.out.println("哈哈哈");
        //System.out.println("===messageEntity===="+ new JSONObject(messageEntity).toString());
        List<Queues> queuesList = queuesService.lambdaQuery()
                .eq(Queues::getCode, messageEntity.getCode()).list();
        if (queuesList.size() < 1) {
            log.error("MessageCenter:非法的标识 => " + messageEntity.getCode());
            return Result.error(-1, "非法的标识，未查询到消息配置");
        }
        //发送人校验
        if (StringUtils.isEmpty(messageEntity.getByUser())) {//为空 => user => 14773 admin
            UserInfo userInfo = UserUtil.getUser();
            if (userInfo != null) {
                messageEntity.setByUser(userInfo.getUserId());
            } else {
                messageEntity.setByUser(Long.valueOf(14773));
            }
        }
        Queues queues = queuesList.get(0);
        if ("0".equals(queues.getStatus())) {
            log.info("MessageCenter:停用的消息类型 => " + queues.getCode());
            return Result.error(-1, "停用的消息类型 => " + queues.getCode());
        }
        //隔离级别 tenantId projectId校验
        String keepLevelStr = queues.getKeepLevel();
        if (!StringUtils.isEmpty(keepLevelStr)) {
            String[] kls = keepLevelStr.split(",");
            for (String kl : kls) {
                if ("10".equals(kl)) {//租户
                    if (StringUtils.isEmpty(messageEntity.getTenantId())) {
                        log.error("MessageCenter:参数错误 => tenantId 为空");
                        return Result.error(-1, "参数错误，tenantId 为空");
                    }
                }
                if ("20".equals(kl)) {//项目
                    if (StringUtils.isEmpty(messageEntity.getProjectId())) {
                        log.error("MessageCenter:参数错误 => projectId 为空");
                        return Result.error(-1, "参数错误，projectId 为空");
                    }
                }
            }
        }
        //异步提醒
        new Thread(new Runnable() {
            @Override
            public void run() {
                //判断隔离级别
                boolean tenantKeep = false;
                boolean projectKeep = false;
                String keepLevelStr = queues.getKeepLevel();
                if (!StringUtils.isEmpty(keepLevelStr)) {
                    String[] kls = keepLevelStr.split(",");
                    for (String kl : kls) {
                        if ("10".equals(kl)) {//租户
                            tenantKeep = true;
                        }
                        if ("20".equals(kl)) {//项目
                            projectKeep = true;
                        }
                    }
                }
                //1、处理接收人员
                String code = messageEntity.getCode();
                LambdaQueryChainWrapper<AssignUser> wrapper = assignUserService.lambdaQuery().eq(AssignUser::getCode, code);
                if (tenantKeep) {
                    wrapper.eq(AssignUser::getTenantId, messageEntity.getTenantId());
                }
                List<Long> toUsers = messageEntity.getToUsers();
                //推送目标类型
                if (queues.getTargetConfigType().equals("1")) {//租户自配
                    List<AssignUser> assignUsers = wrapper.list();
                    if (assignUsers.size() < 1) {
                        log.error("MessageCenter:", "配置错误 => TargetConfigType[1]时，AssignUser不得为空");
                    }
                    Map<String, Object> params = new HashMap<>();
                    if (tenantKeep) {
                        params.put("tenantId", messageEntity.getTenantId());
                    }
                    if (projectKeep) {//被授权了项目
                        params.put("projectId", messageEntity.getProjectId());
                    }
                    List<Long> users = new ArrayList<>();
                    List<Long> depts = new ArrayList<>();
                    String targetType = "";
                    for (AssignUser user : assignUsers) {
                        if (user.getType().equals("10")) {//部门
                            depts.add(Long.parseLong(user.getDataId()));
                        }
                        if (user.getType().equals("20")) {//人员
                            users.add(Long.parseLong(user.getDataId()));
                        }
                        targetType = user.getTargetType();
                    }
                    params.put("targetType", targetType);
                    String usersStr = "-1";
                    String deptsStr = "-1";
                    if("1".equals(targetType)){//指定
                        for (Long id : users) {
                            usersStr = id + "," + usersStr;
                        }
                        for (Long id : depts) {
                            deptsStr = id + "," + deptsStr;
                        }
                    }
                    params.put("users", usersStr);
                    params.put("depts", deptsStr);
                    //获取  List<Long> toUsers
                    List<Long> userIds = assignUserService.getAssignUser(params);
                    messageEntity.setToUsers(userIds);
                } else {
                    if (toUsers == null || toUsers.size() < 1) {
                        log.error("MessageCenter:", "参数错误 => TargetConfigType[0]时，toUsers不得为空");
                    }
                }
                //2、处理发送内容 保留 @userName@ @title@  扩展 [0],[1]
                String username = adminService.queryUserName(messageEntity.getByUser()).getData();
                String title = queues.getSendContent();
                //@XX@
                title = title.replace("@userName@", username)
                        .replace("@title@", messageEntity.getTitle() == null ? "" : messageEntity.getTitle());
                //[0],[1]
                String[] titleParams = messageEntity.getTitleParams();
                if (titleParams != null) {
                    title = MessageFormat.format(queues.getSendContent(), titleParams);
                }
                //3、保存消息记录
                String insertLog = queues.getInsertLog();
                if("1".equals(insertLog)){
                    List<Message> list = new ArrayList();
                    for (Long toUser : messageEntity.getToUsers()) {
                        Message message = BeanUtil.copyProperties(messageEntity, Message.class);
                        message.setType(messageEntity.getCode());
                        message.setId(UUID.randomUUID().toString());
                        message.setToUser(toUser);
                        message.setTitle(title);
                        messageEntity.setMsgContent(message.getMsgContent());
                        list.add(message);
                    }
                    if (list.size() > 0) {
                        messageService.saveBatch(list);
                    }
                }
                //4、推送引擎
                //MQ
                if ("1".equals(queues.getIfSendMq())) {
                    sendMq.send(queues.getExchangeName(), queues.getChannelName(), BeanUtil.beanToMap(messageEntity));
                }
                //及时会话 这里是多选 //支持socketio websocket
                if ("1".equals(queues.getIfSendWeb())) {
                    String[] engines = queues.getWebSendEngine().split(",");
                    for (String e : engines) {
                        if ("10".equals(e)) {//IO
                            socketIOService.send(messageEntity, queues);
                        }
                        if ("20".equals(e)) {//WEB
                            webSocketService.send(messageEntity, queues);
                        }
                    }
                }
                //如配置手机端推送则需通过UniPush进行消息推送
                if ("1".equals(queues.getIfSendMobile())) {
                    String[] engines = queues.getMobileSendEngine().split(",");
                    for (String e : engines) {
                        //TODO 已实现Unipush
                        if (e.equals("UNIPUSH")) {
                            uniPushMsg(messageEntity);
                        }
                    }
                }

            }
        }).start();
        return Result.ok();
    }

    /**
     * 通过UPush 推送TRtc通话提醒
     * v1.0 未研究 upush消息的持续震动
     *
     * @param data 目标信息   id、name、roomId
     * @return
     */
    @Override
    public Result sendTRtc(JSONObject data) {
        PushApi pushApi = getApiHelper().creatApi(PushApi.class);
        PushBatchDTO PushBatchDTO = new PushBatchDTO();
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        Strategy strategy = new Strategy();
        strategy.setDef(3);//个推
        Settings settings = new Settings();
        settings.setStrategy(strategy);
        pushDTO.setSettings(settings);
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        // PushMessage在线走个推通道才会起作用的消息体
        PushMessage pushMessage = new PushMessage();
        pushDTO.setPushMessage(pushMessage);
        String title = data.getString("name") + "邀请通话";
        pushMessage.setTransmission(" {title:\"" + title + "\",content:\"邀请通话……\",payload:\"{dataType:'TRTC',roomId:"
                + data.getInteger("roomId") + "}\"}");// 设置接收人信息
        Audience audience = new Audience();
        audience.addAlias(data.getString("userId"));//手机端注册对应绑定别名=登录用户名 目标UserId
        pushDTO.setAudience(audience);
        PushBatchDTO.addPushDTO(pushDTO);
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushBatchByAlias(PushBatchDTO);
        if (apiResult.isSuccess()) {
            // success
            System.out.println(apiResult.getData());
        } else {
            // failed
            System.out.println("code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
        }
        return Result.ok();
    }

    @Override
    public Result registerCid(Map<String, String> map) {
//        System.out.println("registerCid==="+map.get("cid"));
        UserApi userApi = getApiHelper().creatApi(UserApi.class);
        CidAliasListDTO cidDTO = new CidAliasListDTO();
        CidAliasListDTO.CidAlias cid = new CidAliasListDTO.CidAlias(map.get("cid"), map.get("userid"));
        cidDTO.add(cid);

        ApiResult<Void> apiResult = userApi.bindAlias(cidDTO);
        if (apiResult.isSuccess()) {
            // success
            System.out.println("registerCid success ：" + apiResult.getData());
        } else {
            // failed
            System.out.println("registerCid failed code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
        }
        return Result.ok();
    }

    private ApiHelper getApiHelper() {
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        //填写应用配置
//        AppID：
//        wMNt76hzKZ8XneeucgNIO9
//        AppKey：
//        rT79EFzYFc7dDJqD2R8HDA
//        AppSecret：
//        AruCqULd7L7QCl5MK8IRrA
//        MasterSecret：
//        AVG9PezDjs698i36M6kWi2
        apiConfiguration.setAppId("wMNt76hzKZ8XneeucgNIO9");
        apiConfiguration.setAppKey("rT79EFzYFc7dDJqD2R8HDA");
        apiConfiguration.setMasterSecret("AVG9PezDjs698i36M6kWi2");
        //apiConfiguration.setDomain("https://restapi.getui.com/v2/");
        // 实例化ApiHelper对象，用于创建接口对象
        return ApiHelper.build(apiConfiguration);
    }

    private void uniPushMsg(MessageEntity messageEntity) {
        PushApi pushApi = getApiHelper().creatApi(PushApi.class);
        PushBatchDTO PushBatchDTO = new PushBatchDTO();
        for (Long userId : messageEntity.getToUsers()) {
            PushDTO<Audience> pushDTO = new PushDTO<Audience>();
            Strategy strategy = new Strategy();
            strategy.setDef(3);
            Settings settings = new Settings();
            settings.setStrategy(strategy);
            pushDTO.setSettings(settings);
            pushDTO.setRequestId(System.currentTimeMillis() + "");
            // PushMessage在线走个推通道才会起作用的消息体
            PushMessage pushMessage = new PushMessage();
            pushDTO.setPushMessage(pushMessage);
            pushMessage.setTransmission(" {title:\"" + messageEntity.getMesTitle() + "\",content:\"" + messageEntity.getMsgContent() +
                    "\",payload:\"{dataType:" + messageEntity.getCode() + ",dataId:" + messageEntity.getData() + "}\"}");
            // 设置接收人信息
            Audience audience = new Audience();
            audience.addAlias(userId.toString());//手机端注册对应绑定别名=登录用户名
            pushDTO.setAudience(audience);
            PushBatchDTO.addPushDTO(pushDTO);
        }
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushBatchByAlias(PushBatchDTO);
        if (apiResult.isSuccess()) {
            // success
            System.out.println(apiResult.getData());
        } else {
            // failed
            System.out.println("code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
        }
    }

    //UniApp、个推消息推送测试方法
    public static void main(String[] aa) {
        String[] str = "测试1,提醒".split(",");
        String content = MessageFormat.format("{0}下发企业报验任务：《{1}》，请及时处理",
                str);
        System.out.println(content);
    }
}
