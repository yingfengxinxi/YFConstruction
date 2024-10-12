package com.kakarote.build.project.entity.BO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 计划管理--周计划
 * </p>
 *
 * @author zy
 * @since 2021-07-05
 */
@Data
@ApiModel(value="计划管理--周计划")
public class ProgresssPlanWeekBO extends PageEntity {

    @ApiModelProperty(value = "模板id")
    private Integer mid;

    @ApiModelProperty(value = "周计划开始日期")
    private String weekStartDate;

    @ApiModelProperty(value = "周计划结束日期")
    private String weekEndDate;
}
