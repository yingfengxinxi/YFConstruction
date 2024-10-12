package com.kakarote.build.equipment.entity.PO;

import java.math.BigDecimal;

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
 * 设备监测-监测类型通用设置
 * </p>
 *
 * @author wudw
 * @since 2021-10-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_monitor_warn")
@ApiModel(value="EquipmentMonitorWarn对象", description="设备监测-监测类型通用设置")
public class EquipmentMonitorWarn implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "类型ID")
    private String typeId;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "预警上限")
    private BigDecimal warnUpper;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "预警下限")
    private BigDecimal warnLower;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "报警上限阀值")
    private BigDecimal alarmUpper;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "报警下限阀值")
    private BigDecimal alarmLower;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "控制上限")
    private BigDecimal contrUpper;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "控制下限")
    private BigDecimal contrLower;

    @ApiModelProperty(value = " 创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(exist = false)
    private String createUserName;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    private Long updateUserId;

    @TableField(exist = false)
    private String updateUserName;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "警报范围时间 单位秒")
    private Integer rangeTime;

    @ApiModelProperty(value = "状态（0：无效 1：启用 ）")
    private String status;


}
