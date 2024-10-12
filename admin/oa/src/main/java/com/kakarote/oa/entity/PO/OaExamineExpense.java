package com.kakarote.oa.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kakarote.core.servlet.upload.FileEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 费用报销明细表
 * </p>
 *
 * @author author
 * @since 2021-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_oa_examine_expense")
@ApiModel(value="OaExamineExpense对象", description="费用报销明细表")
public class OaExamineExpense implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "expense_id", type = IdType.AUTO)
    private Integer expenseId;

    @ApiModelProperty(value = "审批ID")
    private Integer examineId;

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
    private String invoiceNum;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "批次id")
    private String batchId;

//    private Integer tenantId;

    @TableField(exist = false)
    private List<FileEntity> img = new ArrayList<>();
    @TableField(exist = false)
    private List<FileEntity> file = new ArrayList<>();
}
