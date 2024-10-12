package com.kakarote.build.equipment.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 *
 * @author lzy
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("工程查询对象")
public class BEquipmentProBO extends PageEntity {

    @ApiModelProperty(value = "工程类型")
    private String type;

    @ApiModelProperty(value = "工程名称")
    private String name;

}
