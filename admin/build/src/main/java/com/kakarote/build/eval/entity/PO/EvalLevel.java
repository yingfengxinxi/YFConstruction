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
@TableName("b_eval_level")
@ApiModel(value="EvalLevel对象", description="")
public class EvalLevel implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "等级名称")
    private String levelName;

    @ApiModelProperty(value = "最小分值关系（小于、小于等于、大于、大于等于、等于）")
    private String minRel;

    @ApiModelProperty(value = "最小分值")
    private String minData;

    @ApiModelProperty(value = "最大分值关系")
    private String maxRel;

    @ApiModelProperty(value = "最大分值")
    private String maxData;

    @ApiModelProperty(value = "状态 （0:停用、1:启用、-1已删除）默认为0")
    private String status;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "最大分值关系+最大分值")
    @TableField(exist = false)
    private String maxRelData;

    @ApiModelProperty(value = "最小分值关系+最小分值")
    @TableField(exist = false)
    private String minRelData;


}
