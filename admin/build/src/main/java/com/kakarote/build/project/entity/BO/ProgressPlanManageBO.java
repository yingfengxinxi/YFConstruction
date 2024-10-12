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
 * 进度管理--计划管理--主表
 * </p>
 *
 * @author zy
 * @since 2021-05-06
 */
@Data
@ApiModel("进度管理--计划管理--主表")
public class ProgressPlanManageBO extends PageEntity {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "模板id")
    private Integer mid;

    @ApiModelProperty(value = "开始日期")
    private String startDate;

    @ApiModelProperty(value = "最高楼层")
    private Integer maxLoft;

    @ApiModelProperty(value = "计划开始日期 开始范围")
    private String planStartDateLeft;

    @ApiModelProperty(value = "计划开始日期 结束范围")
    private String planStartDateRight;

    @ApiModelProperty(value = "计划结束日期 开始范围")
    private String planEndDateLeft;

    @ApiModelProperty(value = "计划结束日期 结束范围")
    private String planEndDateRight;

    //是否里程碑
    private String ifMilestone;

}
