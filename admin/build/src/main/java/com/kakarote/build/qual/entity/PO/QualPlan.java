package com.kakarote.build.qual.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 质量方案
 * </p>
 *
 * @author wudw
 * @since 2021-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_plan")
@ApiModel(value="QualPlan对象", description="质量方案")
public class QualPlan implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "质量方案名称")
    private String name;

    @ApiModelProperty(value = "质量方案编号")
    private String code;

    @ApiModelProperty(value = "方案描述")
    private String content;

    @ApiModelProperty(value = "编制日期")
    private String enactDate;

    @ApiModelProperty(value = "编制人")
    private String enactBy;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.UPDATE)
    private Integer updateUserId;

    private String batchId;

    private Integer projectId;

    private Integer tenantId;

    @ApiModelProperty(value = "0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交")
    private Integer checkStatus;

    @ApiModelProperty(value = "审核记录ID")
    private Integer examineRecordId;

    @ApiModelProperty(value = "负责人ID")
    private Long ownerUserId;


    @TableField(exist = false)
    private List<QualPlanBg> bgList;
}
