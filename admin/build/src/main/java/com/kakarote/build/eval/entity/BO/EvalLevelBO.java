package com.kakarote.build.eval.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class EvalLevelBO extends PageEntity {

    @ApiModelProperty(value = "等级名称")
    private String levelName;
}
