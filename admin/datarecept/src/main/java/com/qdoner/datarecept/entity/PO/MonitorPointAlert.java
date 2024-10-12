package com.qdoner.datarecept.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 监测点警报
 * </p>
 *
 * @author shao
 * @since 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_monitor_point_alert")
@ApiModel(value="MonitorPointAlert对象", description="监测点警报")
public class MonitorPointAlert implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "监测点id")
    private Integer monitorPointId;

    @ApiModelProperty(value = "设备id")
    private Integer equipmentId;

    @ApiModelProperty(value = "监测数据")
    private String monitorData;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "处理时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String handleTime;

    @ApiModelProperty(value = "处理人")
    private String handleBy;

    @ApiModelProperty(value = "处理措施")
    private String handleMeasures;

    @ApiModelProperty(value = "处理后结论")
    private String handleConclu;

    @ApiModelProperty(value = "处理状态")
    private String handleState;

    @ApiModelProperty(value = "处理过程图片批次id")
    private String batchId;

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

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "提醒类型  0预警提醒，1警报提醒 2控制")
    private String alertType;

    @ApiModelProperty(value = "设备报警类型 设备报警类型 该字段用来判断当前数据是否是设备离线报警数据 字典b_monitor_alert_type")
    private String monitorAlertType;


    @TableField(exist = false)
    @ApiModelProperty(value = "设备名")
    private String assetName;

    @TableField(exist = false)
    @ApiModelProperty(value = "监控点名称")
    private String pointName;

    @TableField(exist = false)
    @ApiModelProperty(value = "监控点类型")
    private String monitorType;

    @TableField(exist = false)
    @ApiModelProperty(value = "监控点上阈值")
    private String alarmUpper;

    @TableField(exist = false)
    @ApiModelProperty(value = "监控点下阈值")
    private String alarmLower;

    @TableField(exist = false)
    @ApiModelProperty(value = "报警设备位置名称")
    private String locationName;

    @TableField(exist = false)
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @TableField(exist = false)
    @ApiModelProperty(value = "监测项子项")
    private String monitorSonType;

    @TableField(exist = false)
    @ApiModelProperty(value = "上限值 根据提醒类型取监测点的预警上限或报警上限")
    private String upperValue;

    @TableField(exist = false)
    @ApiModelProperty(value = "下限值 根据提醒类型取监测点的预警下限或报警下限")
    private String lowerValue;

    @TableField(exist = false)
    @ApiModelProperty(value = "报警值拼接单位")
    private String valueUnit;

    @TableField(exist = false)
    @ApiModelProperty(value = "设备编号")
    private String assetCode;

    @ApiModelProperty(value = "监测数据（恢复值）")
    private String recoveryData;

}
