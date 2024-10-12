package com.kakarote.build.project.entity.PO;

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
 * 计划管理--月计划--分包明细
 * </p>
 *
 * @author zy
 * @since 2021-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_progress_plan_month_subcontract")
@ApiModel(value="ProgressPlanMonthSubcontract对象", description="计划管理--月计划--分包明细")
public class ProgressPlanMonthSubcontract implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "年月")
    private String planMonth;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "分包单位")
    private Integer subcontractUnit;

    @ApiModelProperty(value = "分包类型")
    private String subcontractType;

    @ApiModelProperty(value = "分包报量明细")
    private String subcontractReportVolume;

    @ApiModelProperty(value = "分包报量明细本期确认合价")
    private BigDecimal fbblmxbqrehj;

    @ApiModelProperty(value = "本期确认税金")
    private BigDecimal currentConfirmTax;

    @ApiModelProperty(value = "本期确认含税合价")
    private BigDecimal currentConfirmAllPrice;

    @ApiModelProperty(value = "税率%")
    private BigDecimal taxPercent;

    @ApiModelProperty(value = "实际成本合价")
    private BigDecimal actualCost;

    @ApiModelProperty(value = "是否归集")
    private String ifCollection;

    @ApiModelProperty(value = "月计划节点")
    private String planMonthDetailId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.UPDATE)
    private Long updateUserId;

    private Integer projectId;

    private Integer tenantId;


}
