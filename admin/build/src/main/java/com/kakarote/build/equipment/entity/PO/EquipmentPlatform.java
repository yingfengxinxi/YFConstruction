package com.kakarote.build.equipment.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 卸料平台配置表
 * </p>
 *
 * @author shz
 * @since 2022-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_platform")
@ApiModel(value="EquipmentPlatform对象", description="卸料平台配置表")
public class EquipmentPlatform implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备id")
    private Integer equipmentId;

    @ApiModelProperty(value = "卸料平台额定载重，单位：t")
    private Double deviceRatedLoad;

    @ApiModelProperty(value = "主绳额定载重，单位：t")
    private Double mainRatedLoad;

    @ApiModelProperty(value = "副绳额定载重，单位：t")
    private Double subRatedLoad;

    @ApiModelProperty(value = "X倾角预警阈值")
    private Double angleWarmX;

    @ApiModelProperty(value = "Y倾角预警阈值")
    private Double angleWarnY;

    @ApiModelProperty(value = "X倾角报警阈值")
    private Double angleAlarmX;

    @ApiModelProperty(value = "Y倾角报警阈值")
    private Double angleAlarmY;

    @ApiModelProperty(value = "单体工程名称")
    private String monomerProjectName;

    @ApiModelProperty(value = "楼层")
    private Integer floor;

    @ApiModelProperty(value = "本卸料平台的安装次数，第Ｎ次安装:1、2、3等")
    private Integer installNumber;

    private Integer tenantId;

    private Integer projectId;

}
