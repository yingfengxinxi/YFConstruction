package com.kakarote.build.equipment.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自动喷淋系统
 * </p>
 *
 * @author lzy
 * @since 2021-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_spray_system")
@ApiModel(value="EquipmentSpraySystem对象", description="自动喷淋系统")
public class EquipmentSpraySystem implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "监测点")
    private Integer monitorPointId;

    @ApiModelProperty(value = "设备")
    private Integer equipmentId;

    @ApiModelProperty(value = "操作类型 1：启动 0：关闭 非字典项")
    private String operationType;

    @ApiModelProperty(value = "操作原因")
    private String operationReason;

    @ApiModelProperty(value = "操作时间")
    private Date operationTime;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "监测点名称")
    @TableField(exist = false)
    private String pointName;

    @ApiModelProperty(value = "设备名称")
    @TableField(exist = false)
    private String assetName;

    @ApiModelProperty(value = "项目名称")
    @TableField(exist = false)
    private String projectName;

    @ApiModelProperty(value = "设备位置")
    @TableField(exist = false)
    private String assetDom;

    @ApiModelProperty(value = "PM2.5")
    @TableField(exist = false)
    private String PM1;

    @ApiModelProperty(value = "PM10值")
    @TableField(exist = false)
    private String PM2;
}
