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
 * 环保事件督办——处理表
 * </p>
 *
 * @author lzy
 * @since 2022-03-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_event_item")
@ApiModel(value="EventItem对象", description="环境事件督办——处理表")
public class EventItem implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "事件id")
    private String eventId;

    @ApiModelProperty(value = "处理人，登录用户wx openId")
    private String handleBy;

    @ApiModelProperty(value = "处理时间")
    private Date handleTime;

    @ApiModelProperty(value = "整改描述")
    private String rectifyDes;

    @ApiModelProperty(value = "批次id 整改图片/视频")
    private String batchId;

    @ApiModelProperty(value = "审核人，默认当前登录用户")
    private Long checkBy;

    @ApiModelProperty(value = "审核时间，默认当前登录时间")
    private Date checkTime;

    @ApiModelProperty(value = "审核结果 1：通过、2：退回")
    private String checkResult;

    @ApiModelProperty(value = "审核说明")
    private String checkDes;

    @ApiModelProperty(value = "创建人")
    private Integer createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    private Integer projectId;

    private Integer tenantId;

    @ApiModelProperty(value = "处理人")
    @TableField(exist = false)
    private String handleName;

    @ApiModelProperty(value = "审批人")
    @TableField(exist = false)
    private String checkName;


}
