package com.kakarote.build.complex.entity.BO;

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
 * 施工综合--物料进场
 * </p>
 *
 * @author zy
 * @since 2021-06-16
 */
@Data
@ApiModel(description="施工综合--物料进出场")
public class ComplexMaterialsInBO extends PageEntity {

    private Integer id;

    private Integer inId;

    @ApiModelProperty(value = "物料名称")
    private String name;

    @ApiModelProperty(value = "物料编号")
    private String code;

    @ApiModelProperty(value = "规格")
    private String specification;

    @ApiModelProperty(value = "型号")
    private String model;

}
