package com.kakarote.crm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@ApiModel("费用查询")
public class CrmExpensePageBO extends PageEntity {

    @ApiModelProperty("客户ID")
    private Integer customerId;

    @ApiModelProperty("商机ID")
    private Integer businessId;

    @ApiModelProperty("合同ID")
    private Integer contractId;

    @ApiModelProperty("报销ID")
    private Integer examineId;
}
