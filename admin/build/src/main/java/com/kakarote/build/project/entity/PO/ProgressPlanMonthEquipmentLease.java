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
 * 计划管理--月计划--设备租赁明细
 * </p>
 *
 * @author zy
 * @since 2021-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_progress_plan_month_equipment_lease")
@ApiModel(value="ProgressPlanMonthEquipmentLease对象", description="计划管理--月计划--设备租赁明细")
public class ProgressPlanMonthEquipmentLease implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "年月")
    private String planMonth;

    @ApiModelProperty(value = "租赁商名称")
    private String leaserName;

    @ApiModelProperty(value = "明细内容")
    private String detailContent;

    @ApiModelProperty(value = "计费方式")
    private String billingMethod;

    @ApiModelProperty(value = "费用类型")
    private String feeType;

    @ApiModelProperty(value = "租赁总价")
    private BigDecimal totalRentalPrice;

    @ApiModelProperty(value = "租赁总价不含税")
    private BigDecimal totalRentalPriceNotTax;

    @ApiModelProperty(value = "租赁总价税金")
    private BigDecimal totalRentalPriceTax;

    @ApiModelProperty(value = "归集金额含税")
    private BigDecimal collectionPrice;

    @ApiModelProperty(value = "归集金额不含税")
    private BigDecimal collectionPriceNotTax;

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
