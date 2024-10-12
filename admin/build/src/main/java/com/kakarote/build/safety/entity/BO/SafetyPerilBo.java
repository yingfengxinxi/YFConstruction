package com.kakarote.build.safety.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;

public class SafetyPerilBo extends PageEntity {

    @ApiModelProperty(value = "工程类型")
    private String projectType;

    @ApiModelProperty(value = "辨识标准")
    private String criteria;

    @ApiModelProperty(value = "管理方案")
    private String managePlan;
}
