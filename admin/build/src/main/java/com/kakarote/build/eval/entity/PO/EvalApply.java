package com.kakarote.build.eval.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 智慧工地评价-评估打分申请表
 * </p>
 *
 * @author lzy
 * @since 2022-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_eval_apply")
@ApiModel(value="EvalApply对象", description="智慧工地评价-评估打分申请表")
public class EvalApply implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "申请评级/等级ID")
    private String levelId;

    @ApiModelProperty(value = "申请描述")
    private String applyDes;

    @ApiModelProperty(value = "申请人")
    private String applyBy;

    @ApiModelProperty(value = "申请时间")
    private Date applyTime;

    @ApiModelProperty(value = "状态 （0:待评估、1:评估中、2：申请通过、3：申请拒绝）默认为0")
    private String status;

    @ApiModelProperty(value = "审判部门")
    private Integer appDept;

    @ApiModelProperty(value = "审批人")
    private Long appBy;

    @ApiModelProperty(value = "审批时间")
    private Date appTime;

    @ApiModelProperty(value = "审批描述")
    private String appDes;

    @ApiModelProperty(value = "总分值")
    private String score;

    @ApiModelProperty(value = "评估等级")
    private String appLevel;

    @ApiModelProperty(value = "批次id")
    private String batchId;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "等级名称")
    @TableField(exist = false)
    private String levelName;

    @ApiModelProperty(value = "项目名称")
    @TableField(exist = false)
    private String projectName;

    @ApiModelProperty(value = "施工单位")
    @TableField(exist = false)
    private String company;


}
