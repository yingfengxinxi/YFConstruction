package com.kakarote.build.safety.entity.BO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 边界区块基本信息
 * </p>
 *
 * @author zy
 * @since 2021-06-10
 */
@Data
@ApiModel(description="边界区块基本信息")
public class SafetyBoundaryBlockBO extends PageEntity {


    @ApiModelProperty(value = "防护网编号")
    private String protectiveNetCode;


    @ApiModelProperty(value = "边界名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;


}
