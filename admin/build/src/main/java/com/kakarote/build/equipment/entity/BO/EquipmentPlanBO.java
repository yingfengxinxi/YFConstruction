package com.kakarote.build.equipment.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 监测点设置
 * </p>
 *
 * @author lzy
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("维护计划列表查询对象")
public class EquipmentPlanBO extends PageEntity {

    @ApiModelProperty(value = "计划名称")
    private String planName;

    @ApiModelProperty(value = "是否启用")
    private String enable;
}
