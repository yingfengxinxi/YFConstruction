package com.kakarote.build.safety.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import com.kakarote.core.feign.admin.entity.SimpleUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应急事件处置
 * </p>
 *
 * @author zy
 * @since 2021-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_emergency_handling")
@ApiModel(value="SafetyEmergencyHandling对象", description="应急事件处置")
public class SafetyEmergencyHandling implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "应急事件处置id")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "事故编号")
    private String code;

    @ApiModelProperty(value = "事故发生时间")
    private Date happenedTime;

    @ApiModelProperty(value = "事故发生地点")
    private String happenedAddress;

    @ApiModelProperty(value = "事故类型")
    private String accidentType;

    @ApiModelProperty(value = "死亡人数")
    private Integer deathNumber;

    @ApiModelProperty(value = "重伤人数")
    private Integer seriousInjuryNumber;

    @ApiModelProperty(value = "事故描述")
    private String accidentNote;

    @ApiModelProperty(value = "事故原因")
    private String accidentReason;

    @ApiModelProperty(value = "批次id")
    private String batchId;

    @ApiModelProperty(value = "关联应急预案id")
    private String eId;

    @ApiModelProperty(value = "消息推送人员ids")
    private String toUserIds;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.UPDATE)
    private Integer updateUserId;

    private Integer tenantId;

    private Integer projectId;


    @TableField(exist = false)
    private List<SimpleUser> ownerUserList;
}
