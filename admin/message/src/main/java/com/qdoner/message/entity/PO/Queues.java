package com.qdoner.message.entity.PO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wudw
 * @since 2021-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wm_queues")
@ApiModel(value="Queues对象", description="")
public class Queues implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "描述说明")
    private String descr;

    @ApiModelProperty(value = "code码")
    private String code;

    @ApiModelProperty(value = "启用MQ通道")
    private String ifSendMq;

    @ApiModelProperty(value = "持久化")
    private String durability;

    @ApiModelProperty(value = "自动删除")
    private String autoDelete;

    @ApiModelProperty(value = "扩展参数")
    private String arguments;

    @ApiModelProperty(value = "发送内容")
    private String sendContent;

    @ApiModelProperty(value = "交换机")
    private String exchangeName;

    @ApiModelProperty(value = "通道key")
    private String channelName;

    @ApiModelProperty(value = "启用web通道")
    private String ifSendWeb;

    @ApiModelProperty(value = "web端推送引擎")
    private String webSendEngine;

    @ApiModelProperty(value = "推送目标配置 0：系统指定  1：租户配置")
    private String targetConfigType;

    @ApiModelProperty(value = "推送端")
    private String targets;

    @ApiModelProperty(value = "同步推送移动设备")
    private String ifSendMobile;

    @ApiModelProperty(value = "移动设备消息引擎")
    private String mobileSendEngine;

    @ApiModelProperty(value = "系统内置")
    private String isSys;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "通知部门")
    private String deptIds;

    @ApiModelProperty(value = "通知人")
    private String ownerUserIds;

    @ApiModelProperty(value = "最近一次部署时间")
    private Date deployTime;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "隔离级别")
    private String keepLevel;

    @ApiModelProperty(value = "显示图标")
    private String icon;

    @ApiModelProperty(value = "点击是否打开url")
    private String clickOpen;

    @ApiModelProperty(value = "点击跳转页面")
    private String url;

    @ApiModelProperty(value = "文本内容 缺省url，跳转默认页面时展示的文本")
    private String msg_content;

    @ApiModelProperty(value = "是否插入消息记录")
    private String insertLog;
}
