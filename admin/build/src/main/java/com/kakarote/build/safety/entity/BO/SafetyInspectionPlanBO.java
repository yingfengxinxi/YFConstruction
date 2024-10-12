package com.kakarote.build.safety.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 安全检查计划
 * </p>
 *
 * @author lzy
 * @since 2021-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("质量检查查询对象")
public class SafetyInspectionPlanBO extends PageEntity {

    @ApiModelProperty(value = "执行计划名称")
    private String planName;
}
