package com.kakarote.build.project.entity.BO;

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
 * 计划管理--月计划
 * </p>
 *
 * @author zy
 * @since 2021-06-29
 */
@Data
@ApiModel(description="计划管理--月计划")
public class ProgressPlanMonthBO extends PageEntity {


    @ApiModelProperty(value = "年月")
    private String planMonth;

    @ApiModelProperty(value = "模板id")
    private Integer mid;

    @ApiModelProperty(value = "计划开始日期 开始范围")
    private String planStartDateLeft;

    @ApiModelProperty(value = "计划开始日期 结束范围")
    private String planStartDateRight;

    @ApiModelProperty(value = "计划结束日期 开始范围")
    private String planEndDateLeft;

    @ApiModelProperty(value = "计划结束日期 结束范围")
    private String planEndDateRight;

}
