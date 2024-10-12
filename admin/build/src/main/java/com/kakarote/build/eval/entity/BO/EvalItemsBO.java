package com.kakarote.build.eval.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EvalItemsBO extends PageEntity {

    @ApiModelProperty(value = "应用类别")
    private String classType;

    @ApiModelProperty(value = "分项类别")
    private String itemType;

    @ApiModelProperty(value = "分项名称")
    private String itemName;

    @ApiModelProperty(value = "工地等级")
    private String evalLevelId;
}
