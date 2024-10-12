package com.kakarote.build.qual.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 质量方案
 * </p>
 *
 * @author wudw
 * @since 2021-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("质量方案查询对象")
public class QualPlanBO extends PageEntity {
    @ApiModelProperty(value = "search")
    private String search;

    @ApiModelProperty(value = "编号")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;
}
