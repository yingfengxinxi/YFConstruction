package com.kakarote.admin.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author zhangzhiwei
 * BO业务逻辑代码
 */
@Data
@ApiModel(value="SysArea查询对象")
public class SysAreaBO {
    @ApiModelProperty(value = "级别：1、省级 2、市级 3、区县")
    private Integer level;

    @ApiModelProperty(value = "父级id")
    private Integer parentId;
}
