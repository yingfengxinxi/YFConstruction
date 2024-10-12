package com.kakarote.build.equipment.entity.PO;

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
 * 设备监测-监测项实时数据表
 * </p>
 *
 * @author wudw
 * @since 2021-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_monitor_data_at")
@ApiModel(value="EquipmentMonitorDataAt对象", description="设备监测-监测项实时数据表")
public class EquipmentMonitorDataAt implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "point_id", type = IdType.INPUT)
    private Integer pointId;

    @ApiModelProperty(value = "监测值")
    private String value;

    @ApiModelProperty(value = "监测时间")
    private Date monitorTime;

    @ApiModelProperty(value = "创建时间")
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

    private Integer tenantId;

    private Integer projectId;

    private String alertType;

    private Integer equType;
}
