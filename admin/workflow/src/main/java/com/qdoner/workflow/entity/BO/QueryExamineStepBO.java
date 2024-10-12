package com.qdoner.workflow.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryExamineStepBO {

    @ApiModelProperty("审核对象id")
    private Integer id;

    @ApiModelProperty("审核类型 1 合同 2 回款 3 发票 4 薪资")
    private Integer categoryType;
}
