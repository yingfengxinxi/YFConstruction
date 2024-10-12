package com.kakarote.build.equipment.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author lzy
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("维护计划设备列表查询对象")
public class EquipmentPlanAssetBO extends PageEntity {

    @ApiModelProperty(value = "计划id")
    private Integer planId;
}
