package com.kakarote.build.equipment.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 喷淋定期任务
 * </p>
 *
 * @author lzy
 * @since 2021-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("喷淋定期任务查询对象")
public class EquipmentSprayTaskBO extends PageEntity {

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "启停状态 0：禁用；1：启动；非字典项")
    private String isEnable;
}
