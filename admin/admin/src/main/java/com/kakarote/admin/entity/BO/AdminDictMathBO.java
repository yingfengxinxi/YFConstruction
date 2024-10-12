package com.kakarote.admin.entity.BO;

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
public class AdminDictMathBO extends PageEntity {

    @ApiModelProperty(value = "本系统字典类型")
    private String selfDict;

    @ApiModelProperty(value = "本系统字典值")
    private String selfDictValue;
}
