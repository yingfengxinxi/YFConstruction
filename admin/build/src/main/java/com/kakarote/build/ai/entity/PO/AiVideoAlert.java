package com.kakarote.build.ai.entity.PO;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shao
 * @since 2021-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_ai_video_alert")
@ApiModel(value="AiVideoAlert对象", description="")
public class AiVideoAlert implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "警报时间")
    private Date alertTime;

    @ApiModelProperty(value = "警报类型 旧版AI事件类型(已弃用)")
    private String alertType;

    @ApiModelProperty(value = "警报位置")
    private String alertPosition;

    @ApiModelProperty(value = "警报内容")
    private String alertContent;

    @ApiModelProperty(value = "警报图片")
    private String alertImg;

    @ApiModelProperty(value = "警报数据")
    private String alertData;

    @ApiModelProperty(value = "处置人")
    private String handleBy;

    @ApiModelProperty(value = "处置内容")
    private String handleContent;

    @ApiModelProperty(value = "处置时间")
    private Date handleTime;

    @ApiModelProperty(value = " 创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    private Long updateUserId;

    @ApiModelProperty(value = "设备id")
    private Integer equipmentId;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "AI事件类型 b_ai_event_type")
    private String aiEventType;

    @ApiModelProperty(value = "设备编号")
    @TableField(exist = false)
    private String code;

    @ApiModelProperty(value = "设备名称")
    @TableField(exist = false)
    private String assetName;


}
