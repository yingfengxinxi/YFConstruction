package com.kakarote.crm.entity.PO;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 费用明细表
 * </p>
 *
 * @author author
 * @since 2021-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_expense")
@ApiModel(value="CrmExpenseItems对象", description="费用明细表")
public class CrmExpenseItems implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "expense_id", type = IdType.AUTO)
    private Integer expenseId;

    @ApiModelProperty(value = "关联客户")
    private Integer customerId;

    @ApiModelProperty(value = "关联合同")
    private Integer contractId;

    @ApiModelProperty(value = "关联商机")
    private Integer businessId;

    @ApiModelProperty(value = "费用说明")
    private String description;

    @ApiModelProperty(value = "费用类型")
    private String expenseType;

    @ApiModelProperty(value = "费用金额")
    private BigDecimal money;

    @ApiModelProperty(value = "费用日期")
    private Date expenseTime;

    @ApiModelProperty(value = "有发票 0:否;1:是")
    private String hasInvoice;

    @ApiModelProperty(value = "发票号码")
    private String invoiceNums;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "报销 0:否;1:是")
    private String hasExamine;

    @ApiModelProperty(value = "审批ID")
    private Integer examineId;

    @ApiModelProperty(value = "批次id")
    private String batchId;

    private Integer tenantId;

    @ApiModelProperty(value = "费用编号")
    private String expenseNum;

    @ApiModelProperty(value = "0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交")
    private Integer checkStatus;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;


}
