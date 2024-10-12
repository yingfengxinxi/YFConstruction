package com.kakarote.crm.entity.BO;

import com.kakarote.crm.entity.PO.CrmExpenseExamines;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * 费用保存对象
 * @author wudw
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@ApiModel("crm费用保存对象")
public class CrmExpenseSaveBO extends CrmModelSaveBO {
    //这里需要扩展，多子表，一主表对象保存
    @ApiModelProperty("审批表数据")
    private CrmExpenseExamines examinesData;

    @ApiModelProperty("审批表数据")
    private List<CrmExpenseSaveBO> expenseList;

    @ApiModelProperty("审核人ID")
    private Long checkUserId;

    @ApiModelProperty("是否直接新增")
    private String isCreate;

    private List<Integer> delItemIds;
}
