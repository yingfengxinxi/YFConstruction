package com.kakarote.build.env.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 环境标准
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("环境标准查询对象")
public class EnvLevelBO extends PageEntity {

    @ApiModelProperty(value = "标准等级")
    private String levelName;

}
