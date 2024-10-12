package com.kakarote.build.safety.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("安全资料查询对象")
public class SafetyMaterialBO extends PageEntity {

    @ApiModelProperty(value = "目录ID")
    private Integer catalogId;
    @ApiModelProperty(value = "目录ID集合")
    private List<Integer> catalogIds;
}
