package com.kakarote.build.eval.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;
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
 * @since 2022-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_eval_apply_items")
@ApiModel(value="EvalApplyItems对象", description="")
public class EvalApplyItems implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "申请ID")
    private String applyId;

    @ApiModelProperty(value = "源打分项ID")
    private String itemId;

    @ApiModelProperty(value = "应用类别")
    private String classType;

    @ApiModelProperty(value = "打分项类型（10:基础项、20:提升项、30：推广项）")
    private String itemType;

    @ApiModelProperty(value = "打分项名称")
    private String itemName;

    @ApiModelProperty(value = "打分项描述")
    private String itemDes;

    @ApiModelProperty(value = "最大分值")
    private BigDecimal maxData;

    @ApiModelProperty(value = "最小分值（默认为0）")
    private BigDecimal minData;

    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "状态 （0:未打分、1:已打分）默认为0")
    private String status;

    @ApiModelProperty(value = "单项分值")
    private String itemScore;

    @ApiModelProperty(value = "打分部门")
    private Integer scoreDept;

    @ApiModelProperty(value = "打分人")
    private Long scoreBy;

    @ApiModelProperty(value = "打分时间（只记录最后一次的打分时间）")
    private Date scoreTime;

    @ApiModelProperty(value = "备注")
    private String scoreDes;

    @ApiModelProperty(value = "批次id")
    private String batchId;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "文件数量")
    @TableField(exist = false)
    private Integer fileNumber;

    @ApiModelProperty(value = "列表下标")
    @TableField(exist = false)
    private Integer index;


}
