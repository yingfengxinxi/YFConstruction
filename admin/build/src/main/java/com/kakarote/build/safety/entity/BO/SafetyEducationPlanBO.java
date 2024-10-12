package com.kakarote.build.safety.entity.BO;

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
 * 安全教育计划
 * </p>
 *
 * @author wnj
 * @since 2021-08-09
 */
@Data
@ApiModel("安全教育计划")
public class SafetyEducationPlanBO extends PageEntity {
    @ApiModelProperty(value = "培训计划名称")
    private String trainPlanName;

    @ApiModelProperty(value = "培训类型")
    private String trainType;

    @ApiModelProperty(value = "培训周期")
    private String trainCycle;

    @ApiModelProperty(value = "培训内容")
    private String trainContent;

    @ApiModelProperty(value = "总课时")
    private String totalClassHours;

    @ApiModelProperty(value = "负责人")
    private String personCharge;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

}
