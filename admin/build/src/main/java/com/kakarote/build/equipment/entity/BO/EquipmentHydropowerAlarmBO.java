package com.kakarote.build.equipment.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 *
 * @author lzy
 * @since 2021-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("水电报警查询")
public class EquipmentHydropowerAlarmBO extends PageEntity {

    @ApiModelProperty(value = "能源名称 1：电量；2：水量；")
    private String energyName;

    @ApiModelProperty(value = "超限类型 1：当期；2：累计；")
    private String transfiniteType;

    @ApiModelProperty(value = "项目Id")
    private Integer projectId;
}
