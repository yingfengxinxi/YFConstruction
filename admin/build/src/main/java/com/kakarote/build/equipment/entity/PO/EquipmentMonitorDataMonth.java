package com.kakarote.build.equipment.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 设备监测-监测项月数据表
 * </p>
 *
 * @author lzy
 * @since 2021-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_monitor_data_month")
@ApiModel(value="EquipmentMonitorDataMonth对象", description="设备监测-监测项月数据表")
public class EquipmentMonitorDataMonth implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "监测点id")
    private Integer pointId;

    @ApiModelProperty(value = "监测值")
    private String value;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "统计类型")
    private String statisticalType;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "最大值")
    private String maxValue;

    @ApiModelProperty(value = "最小值")
    private String minValue;

    @ApiModelProperty(value = "是否最新数据")
    private String isNear;

    @ApiModelProperty(value = "聚合开始时间")
    private Date startTime;

    @ApiModelProperty(value = "聚合结束时间（不包含）")
    private Date endTime;


}
