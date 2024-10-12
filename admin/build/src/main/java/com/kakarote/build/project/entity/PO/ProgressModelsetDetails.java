package com.kakarote.build.project.entity.PO;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 进度管理--模板设置--明细表
 * </p>
 *
 * @author zy
 * @since 2021-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_progress_modelset_details")
@ApiModel(value="ProgressModelsetDetails对象", description="进度管理--模板设置--明细表")
public class ProgressModelsetDetails implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "所属模板ID")
    private Integer mid;

    @ApiModelProperty(value = "所属模板名称")
    private String mname;

    @ApiModelProperty(value = "序号")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer serialNumber;

    @ApiModelProperty(value = "等级")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer level;

    @ApiModelProperty(value = "任务名称")
    private String name;

    @ApiModelProperty(value = "父任务")
    private String pid;

    @ApiModelProperty(value = "计划工期天数")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer planGqiNumber;

    @ApiModelProperty(value = "是否里程碑")
    private String ifMilestone;

    @ApiModelProperty(value = "是否关键路线")
    private String ifCriticalRoute;

    @ApiModelProperty(value = "前置节点")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String preNode;

    @ApiModelProperty(value = "备注")
    private String note;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.UPDATE)
    private Long updateUserId;

    private Integer tenantId;

    private Integer projectId;

    @TableField(exist = false)
    private Boolean hasChildren;

    @TableField(exist = false)
    private String pname;
}
