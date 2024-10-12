package com.qdoner.dataagg.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 设备监测-监测项日数据表
 * </p>
 *
 * @author wudw
 * @since 2021-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_monitor_data_day")
@ApiModel(value="EquipmentMonitorDataDay对象", description="设备监测-监测项日数据表")
public class MonitorDataDay implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "监测点id")
    private Integer pointId;

    @ApiModelProperty(value = "监测值")
    private String value;

    @ApiModelProperty(value = "集合开始时间")
    @TableField(fill = FieldFill.INSERT)
    private Date startTime;

    @ApiModelProperty(value = "集合结束时间（不包含）")
    @TableField(fill = FieldFill.INSERT)
    private Date endTime;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "统计类型")
    private String statisticalType;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "最小值")
    private String minValue;

    @ApiModelProperty(value = "最大值")
    private String maxValue;

    @ApiModelProperty(value = "是否最近数据")
    private String isNear;
}
