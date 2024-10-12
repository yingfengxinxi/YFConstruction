package com.qdoner.datarecept.equipment.entity.PO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 设备监测-基坑设备实时数据表
 * </p>
 *
 * @author shz
 * @since 2022-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_pit_data_at_bak")
@ApiModel(value="EquipmentPitDataAtBak对象", description="设备监测-基坑设备实时数据表")
public class EquipmentPitDataAtBak implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "监测点id")
    private Integer pointId;

    @ApiModelProperty(value = "设备id")
    private Integer equipmentId;

    @ApiModelProperty(value = "监测值")
    private String value;

    @ApiModelProperty(value = "监测时间")
    private Date monitorTime;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "最新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "单次变化值")
    private Double variation;

    @ApiModelProperty(value = "累计变化值")
    private Double aggregateValue;

    @ApiModelProperty(value = "变化速率")
    private Double rateChange;

    @ApiModelProperty(value = "单次变化值限定值")
    private Double variationLimit;

    @ApiModelProperty(value = "累计变化值限定值")
    private Double aggregateValueLimit;

    @ApiModelProperty(value = "变化速率限定值")
    private Double rateChangeLimit;

    @ApiModelProperty(value = "更新间隔（秒）")
    private Double timeInterval;

    @ApiModelProperty(value = "初始值")
    private Double initValue;

    @ApiModelProperty(value = "偏差值")
    private Double offsetValue;

    @ApiModelProperty(value = "报警状态 1-正常；2-预警；3-报警")
    private Integer alarmStatus;

    @ApiModelProperty(value = "报警描述")
    private String alarmDesc;

    @ApiModelProperty(value = "累计位移发生位置")
    private Double maxCumulativeDisplacementPosition;

    @ApiModelProperty(value = "变化速率发生位置")
    private Double currentMaxDisplacementPosition;

    @ApiModelProperty(value = "X轴方向传感角度")
    private Double xangle;

    @ApiModelProperty(value = "Y轴方向传感角度")
    private Double yangle;

    @ApiModelProperty(value = "传感节点电池电压")
    private Double batteryVoltage;

    @ApiModelProperty(value = "裂缝长度")
    private Double crack;

    @ApiModelProperty(value = "环境温度")
    private Double ambientTemperature;

    @ApiModelProperty(value = "传感器类型：1-位移；2-深层水平位移；3-倾斜；4-力;")
    private String deviceType;
}
