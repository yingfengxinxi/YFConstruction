package com.kakarote.build.equipment.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shz
 * @since 2022-08-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_platform_data_at")
@ApiModel(value="EquipmentPlatformDataAt对象", description="")
public class EquipmentPlatformDataAt implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备ID")
    private Integer equipmentId;

    @ApiModelProperty(value = "监测时间")
    private Date monitorTime;

    @ApiModelProperty(value = "卸料平台实际载重")
    private Double deviceLoad;

    @ApiModelProperty(value = "载重百分比")
    private Double loadPercent;

    @ApiModelProperty(value = "主绳实际载重")
    private Double mainLoad;

    @ApiModelProperty(value = "副绳实际载重")
    private Double subLoad;

    @ApiModelProperty(value = "主绳报警级别，0：正常，1：预警，2：报警")
    private Integer alarmMainLoad;

    @ApiModelProperty(value = "副绳报警级别，0：正常，1：预警，2：报警")
    private Integer alarmSubLoad;

    @ApiModelProperty(value = "X倾角")
    private Double angleX;

    @ApiModelProperty(value = "Y倾角")
    private Double angleY;

    @ApiModelProperty(value = "X倾角报警级别，0：正常，1：预警，2：报警")
    private Integer alarmAngleX;

    @ApiModelProperty(value = "Y倾角报警级别，0：正常，1：预警，2：报警")
    private Integer alarmAngleY;

    private Integer tenantId;

    private Integer projectId;


}
