package com.qdoner.message.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.message.MessageEntity;
import com.kakarote.core.utils.UserUtil;
import com.qdoner.message.entity.BO.MessageBO;
import com.qdoner.message.entity.PO.Message;
import com.qdoner.message.entity.PO.MessageHis;
import com.qdoner.message.service.IMService;
import com.qdoner.message.service.IMessageHisService;
import com.qdoner.message.service.IMessageService;
import com.qdoner.message.service.IQueuesService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 消息接口
 */
@RestController
@RequestMapping("/msg")
@Api(tags = "消息中心接口")
public class MessageController {

    @Autowired
    private IMService imService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IMessageHisService messageHisService;

    @Autowired
    private AdminService adminService;
    @Autowired
    private IQueuesService queuesService;

    /**
     * 发送
     *
     * @return
     */
    @PostMapping("")
//    @OperateLog(detail = "消息发送", types = LogTypes.OTHER, behavior = LogBehavior.INSERT)
    public Result send(@RequestBody MessageEntity messageEntity) throws Exception {
        return imService.send(messageEntity);
    }

    @PostMapping("/trtc")
    public Result sendTRtc(@RequestBody JSONObject data){
        return imService.sendTRtc(data);
    }

    @PostMapping("/registerCid")
    @OperateLog(detail = "手机端uniPush客户端标识别名注册", types = LogTypes.OTHER, behavior = LogBehavior.INSERT)
    public Result registerCid(@RequestBody Map<String,String> map) {
        return imService.registerCid(map);
    }

    /**
     * 全部消息列表
     * {type:}
     *
     * @return
     */
    @PostMapping("/list")
    @OperateLog(detail = "查询消息列表", types = LogTypes.OTHER, behavior = LogBehavior.SELECT)
    public Result list(@RequestBody MessageBO messageBO) {
        return Result.ok(messageService.queryPageList(messageBO));
    }

    /**
     * 未读消息数量
     * {type:}
     *
     * @return
     */
    @PostMapping("/countUnread")
    @OperateLog(detail = "未读消息数量", types = LogTypes.OTHER, behavior = LogBehavior.SELECT)
    public Result countUnread() {
        Integer count = messageService.lambdaQuery()
                .eq(Message::getIfRead, "0")
                .eq(Message::getToUser, UserUtil.getUserId())
                .orderByDesc(Message::getCreateTime).count();
        return Result.ok(count);
    }

    /**
     * 读取消息
     * {type:,ids:}
     *
     * @return
     */
    @PostMapping("/read")
    @OperateLog(detail = "读取消息", types = LogTypes.OTHER, behavior = LogBehavior.UPDATE)
    public Result read(@RequestBody MessageBO messageBO) {
        List<Message> list;
        if (messageBO.getId() == null) {//全部已读
            list = messageService.lambdaQuery().eq(Message::getToUser, UserUtil.getUserId())
                    .list();
        } else {
            list = messageService.lambdaQuery().eq(Message::getToUser, UserUtil.getUserId())
                    .eq(Message::getId, messageBO.getId()).list();
        }
        for (Message message : list) {
            message.setIfRead("1");
            message.setReadTime(new Date());
        }
        if (list.size() > 0) {
            messageService.updateBatchById(list);
        }
        return Result.ok();
    }

    /**
     * 删除消息(单条)
     * {type:,}
     *
     * @return
     */
    @Transactional
    @PostMapping("/delOne")
    @OperateLog(detail = "删除已读消息", types = LogTypes.OTHER, behavior = LogBehavior.DELETE)
    public Result delOne(@RequestBody MessageBO messageBO) {
        Message message = messageService.getById(messageBO.getId());
        if (message == null) {
            return Result.error(-1, "数据不存在");
        }
        MessageHis messageHis = BeanUtil.copyProperties(message, MessageHis.class);
        messageHis.setDelTime(new Date());
        messageHisService.save(messageHis);
        messageService.removeById(messageBO.getId());
        return Result.ok();
    }

    /**
     * 删除已读消息
     * {type:}
     *
     * @return
     */
    @Transactional
    @PostMapping("/delRead")
    @OperateLog(detail = "删除已读消息", types = LogTypes.OTHER, behavior = LogBehavior.DELETE)
    public Result delRead(@RequestBody MessageBO messageBO) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<Message>();
        queryWrapper.eq("to_User", UserUtil.getUserId());
        queryWrapper.eq("if_Read", "1");
        List<Message> messageList = messageService.getBaseMapper().selectList(queryWrapper);
        List<MessageHis> messageHisList = new ArrayList<>();
        for (Message message : messageList) {
            MessageHis messageHis = BeanUtil.copyProperties(message, MessageHis.class);
            messageHis.setDelTime(new Date());
            messageHisList.add(messageHis);
        }
        if (messageHisList.size() > 0) {
            messageHisService.saveBatch(messageHisList);
        }
        messageService.remove(queryWrapper);
        return Result.ok();
    }

    /**
     * 根据id查询消息
     * @return
     */
    @PostMapping("/getById/{id}")
    @OperateLog(detail = "根据id查询消息", types = LogTypes.OTHER, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable String id) {
        Message message = messageService.getById(id);
        String username = adminService.queryUserName(message.getByUser()).getData();
        message.setByUserName(username);
        return Result.ok(message);
    }
}
