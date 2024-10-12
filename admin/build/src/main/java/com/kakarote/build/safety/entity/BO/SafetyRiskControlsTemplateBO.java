package com.kakarote.build.safety.entity.BO;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 安全风险分级管控模板
 * </p>
 *
 * @author wnj
 * @since 2021-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("安全风险分级管控模板")
public class SafetyRiskControlsTemplateBO extends PageEntity {

    @ApiModelProperty(value = "分部分项(字典：sub_item_works_parts)")
    private String subItemWorksParts;

    @ApiModelProperty(value = "风险类型")
    private String riskType;

    @ApiModelProperty(value = "风险辨识点")
    private String riskIdentPoint;

    @ApiModelProperty(value = "事故类型 ")
    private String accidentType;

    @ApiModelProperty(value = "风险分级/标识")
    private String riskLevelIdent;

    @ApiModelProperty(value = "防范措施")
    private String preventiveMeasures;

    @ApiModelProperty(value = "工作依据")
    private String workingBasis;

    @ApiModelProperty(value = "区间开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "区间结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "分组字段名")
    private String isGroupName;
}
