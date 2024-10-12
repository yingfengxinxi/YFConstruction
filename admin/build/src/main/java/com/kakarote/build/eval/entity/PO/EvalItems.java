package com.kakarote.build.eval.entity.PO;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
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
 * 
 * </p>
 *
 * @author lzy
 * @since 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_eval_items")
@ApiModel(value="EvalItems对象", description="")
public class EvalItems implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "应用类别")
    private String classType;

    @ApiModelProperty(value = "打分项类型（10:基础项、20:提升项、30：推广项）")
    private String itemType;

    @ApiModelProperty(value = "打分项名称")
    private String itemName;

    @ApiModelProperty(value = "打分项描述")
    @TableField("Item_des")
    private String itemDes;

    @ApiModelProperty(value = "最大分值")
    private BigDecimal maxData;

    @ApiModelProperty(value = "最小分值（默认为0）")
    private BigDecimal minData;

    @ApiModelProperty(value = "必选等级（多选，使用逗号隔开）")
    private String levels;

    @ApiModelProperty(value = "状态 （0:停用、1:启用、-1已删除）默认为0")
    private String status;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "排序")
    private String sort;

    private Integer tenantId;

    private Integer projectId;


}
