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
 * 设备监测-监测点设置
 * </p>
 *
 * @author lzy
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_monitor_point")
@ApiModel(value="EquipmentMonitorPoint对象", description="设备监测-监测点设置")
public class EquipmentMonitorPoint implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "监测点编号")
    private String pointCode;

    @ApiModelProperty(value = "监测点名称")
    private String pointName;

    @ApiModelProperty(value = "监测项类型 字典项 1：基坑监测、2：塔机监测、3；升降机监测、9：环境监测")
    private String monitorType;

    @ApiModelProperty(value = "检测项子类型 字典项：1：PM2.5、2：PM10、3：噪音、")
    private String monitorSonType;

    @ApiModelProperty(value = "设备名称")
    private Integer equipmentId;

    @ApiModelProperty(value = "统计类型")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String statisticalType;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "经度")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal longitude;

    @ApiModelProperty(value = "纬度")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal latitude;

    @ApiModelProperty(value = "控制上限阀值")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal contrUpper;

    @ApiModelProperty(value = "控制下限阀值")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal contrLower;

    @ApiModelProperty(value = "报警上限阀值")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal alarmUpper;

    @ApiModelProperty(value = "报警下限阀值")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal alarmLower;

    @ApiModelProperty(value = "预警上限阀值")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal warnUpper;

    @ApiModelProperty(value = "预警下限阀值")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal warnLower;

    @ApiModelProperty(value = "是否关联喷洒系统 1：是 0：否  非字典项")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String isSpraySystem;

    @ApiModelProperty(value = "启动喷淋值")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal startSprayValue;

    @ApiModelProperty(value = "关闭喷淋值")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal closeSprayValue;

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

    @ApiModelProperty(value = "设备名称")
    @TableField(exist = false)
    private String equipmentName;

    @ApiModelProperty(value = "控件id")
    private String controlsId;

    @ApiModelProperty(value = "控件坐标  以x,y,z格式存放")
    private String controlsCoordinates;

    @ApiModelProperty(value = "控件值，用来在控件模型展示时使用平常查询控件时无用")
    private String controlsValue;

    @ApiModelProperty(value = "是否展示  0正常展示    1红字展示     2不展示")
    private String controlsState;

    @ApiModelProperty(value = "警报范围时间 单位秒")
    private Integer rangeTime;

    @ApiModelProperty(value = "是否启用  0未启用    1启用")
    private String status;

    @ApiModelProperty(value = "c4d模型设备位置")
    private String c4dPosition;




    //相对坐标
    @ApiModelProperty(value = "相对位置X轴")
    private String planeCoordinateX;
    @ApiModelProperty(value = "相对位置Y轴")
    private String planeCoordinateY;


    @ApiModelProperty(value = "项目名称")
    @TableField(exist = false)
    private String projectName;

    @ApiModelProperty(value = "监测子项名称")
    @TableField(exist = false)
    private String monitorSonTypeName;

    @ApiModelProperty(value = "实时值")
    @TableField(exist = false)
    private String value;

    @ApiModelProperty(value = "监测时间")
    @TableField(exist = false)
    private String monitorTime;

    @ApiModelProperty(value = "监测更新时间")
    @TableField(exist = false)
    private String dataUpdateTime;
}
