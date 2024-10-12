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
 * 费用审查表
 * </p>
 *
 * @author wudw
 * @since 2021-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_expense_examines")
@ApiModel(value="CrmExpenseExamines对象", description="费用审查表")
public class CrmExpenseExamines implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "examine_id", type = IdType.AUTO)
    private Integer examineId;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "关联合同")
    private String contractIds;

    @ApiModelProperty(value = "关联商机")
    private String businessIds;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "费用总金额")
    private BigDecimal money;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "附件批次id")
    private String batchId;

    private Integer tenantId;

    @ApiModelProperty(value = "审核记录ID")
    private Integer examineRecordId;

    @ApiModelProperty(value = "审核状态")
    private Integer checkStatus;

    @ApiModelProperty(value = "关联客户")
    private String customerIds;
}
