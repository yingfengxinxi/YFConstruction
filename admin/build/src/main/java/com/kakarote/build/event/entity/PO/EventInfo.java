package com.kakarote.build.event.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 环保事件督办——事件表
 * </p>
 *
 * @author lzy
 * @since 2022-03-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_event_info")
@ApiModel(value="EventInfo对象", description="环境事件督办——事件表")
public class EventInfo implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "事件编号 日期自增 例：202203040001")
    private String eventCode;

    @ApiModelProperty(value = "事件状态 0：待分派、1：办理中、2：待审核、3：退回、4：完成")
    private String eventStatus;

    @ApiModelProperty(value = "填报人 默认当前登录用户")
    private Long informant;

    @ApiModelProperty(value = "填报时间")
    private Date fillInTime;

    @ApiModelProperty(value = "事件类型 1：大气污染、2：视频监控、3：其他")
    private String eventType;

    @ApiModelProperty(value = "整改期限")
    private Date rectifyTimeLimit;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "事件描述")
    private String eventDes;

    @ApiModelProperty(value = "批次id 图片或视频附件")
    private String batchId;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "发生工地 项目id 筛选项目状态 2：已接入的项目")
    private Integer projectId;

    private Integer tenantId;

    @ApiModelProperty(value = "项目名称")
    @TableField(exist = false)
    private String projectName;

    @ApiModelProperty(value = "填报人")
    @TableField(exist = false)
    private String informantName;


}
