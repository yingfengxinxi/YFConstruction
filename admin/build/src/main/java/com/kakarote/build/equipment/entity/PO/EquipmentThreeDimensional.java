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
 * 设备三维坐标表
 * </p>
 *
 * @author lzy
 * @since 2022-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_three_dimensional")
@ApiModel(value="EquipmentThreeDimensional对象", description="设备三维坐标表")
public class EquipmentThreeDimensional implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer modelId;

    @ApiModelProperty(value = "设备id")
    private Integer equipmentId;

    @ApiModelProperty(value = "模型类型 字典项 b_model_type")
    private String modelType;

    @ApiModelProperty(value = "模型id")
    private String modelName;

    @ApiModelProperty(value = "模型url")
    private String modelUrl;

    @ApiModelProperty(value = "x轴坐标 图标使用")
    @TableField("xAxis")
    private Integer xAxis;

    @ApiModelProperty(value = "y轴坐标 图标使用")
    @TableField("yAxis")
    private Integer yAxis;

    @ApiModelProperty(value = "z轴坐标 图标使用")
    @TableField("zAxis")
    private Integer zAxis;

    @ApiModelProperty(value = "模型x坐标 模型使用")
    private Integer modelX;

    @ApiModelProperty(value = "模型y坐标 模型使用")
    private Integer modelY;

    @ApiModelProperty(value = "模型z坐标 模型使用")
    private Integer modelZ;

    @ApiModelProperty(value = "设备监测项类型")
    private String monitorType;

    @ApiModelProperty(value = "旋转角度")
    private String rotationAngle;

    @ApiModelProperty(value = "场景x")
    private Integer sceneX;

    @ApiModelProperty(value = "场景y")
    private Integer sceneY;

    @ApiModelProperty(value = "场景z")
    private Integer sceneZ;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.UPDATE)
    private Long updateUserId;

    private Integer tenantId;

    private Integer projectId;


}
