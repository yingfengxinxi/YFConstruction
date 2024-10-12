package com.kakarote.build.equipment.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 计划维护设备信息
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_plan_asset")
@ApiModel(value="EquipmentPlanAsset对象", description="计划维护设备信息")
public class EquipmentPlanAsset implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "计划")
    private Integer planId;

    @ApiModelProperty(value = "设备")
    private Integer assetId;

    @TableField(exist = false)
    private Integer tenantId;

    @TableField(exist = false)
    private Integer projectId;

    @TableField(exist = false)
    @ApiModelProperty(value = "资产编号")
    private String assetCode;

    @TableField(exist = false)
    @ApiModelProperty(value = "资产名称")
    private String assetName;

    @TableField(exist = false)
    @ApiModelProperty(value = "资产位置")
    private Integer locationId;

    @TableField(exist = false)
    private String locationName;

    @TableField(exist = false)
    @ApiModelProperty(value = "重要程度")
    private String level;
}
