package com.kakarote.build.eval.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EvalApplyBO extends PageEntity {

    @ApiModelProperty(value = "申请评级")
    private String levelId;

    @ApiModelProperty(value = "申请人")
    private String applyBy;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "评价等级")
    private String appLevel;
}
