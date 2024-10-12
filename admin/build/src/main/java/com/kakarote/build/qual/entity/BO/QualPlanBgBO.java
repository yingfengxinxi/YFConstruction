package com.kakarote.build.qual.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 质量方案变更
 * </p>
 *
 * @author wudw
 * @since 2021-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("质量方案变更查询对象")
public class QualPlanBgBO extends PageEntity {

    @ApiModelProperty(value = "质量方案")
    private String planId;

    @ApiModelProperty(value = "变更原因")
    private String reason;

    @ApiModelProperty(value = "变更日期")
    private String bgDate;

    @ApiModelProperty(value = "变更人")
    private String bgBy;
}
