package com.kakarote.build.qual.entity.PO;

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
 * 质量方案变更
 * </p>
 *
 * @author wudw
 * @since 2021-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_plan_bg")
@ApiModel(value="QualPlanBg对象", description="质量方案变更")
public class QualPlanBg implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "质量方案ID")
    private String planId;

    @TableField(exist = false)
    private String planName;

    @ApiModelProperty(value = "变更原因")
    private String reason;

    @ApiModelProperty(value = "变更内容简述")
    private String content;

    @ApiModelProperty(value = "编制日期")
    private String bgDate;

    @ApiModelProperty(value = "编制人")
    private String bgBy;

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


}
