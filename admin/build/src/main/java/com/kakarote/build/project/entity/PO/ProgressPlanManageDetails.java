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
 * 进度管理--计划管理--子表
 * </p>
 *
 * @author zy
 * @since 2021-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_progress_plan_manage_details")
@ApiModel(value="ProgressPlanManageDetails对象", description="进度管理--计划管理--子表")
public class ProgressPlanManageDetails implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "主表id")
    private Integer zhid;

    @ApiModelProperty(value = "模板明细id")
    @TableId(value = "mdid")
    private String mdid;

    @ApiModelProperty(value = "所属模板id")
    private Integer mid;

    @ApiModelProperty(value = "序号")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer serialNumber;

    @ApiModelProperty(value = "等级")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer level;

    @ApiModelProperty(value = "任务名称")
    private String name;

    @ApiModelProperty(value = "父任务")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String pmdid;

    @ApiModelProperty(value = "计划工期天数")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer planGqiNumber;

    @ApiModelProperty(value = "关联楼座ID")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer relatedBuildingId;

    @ApiModelProperty(value = "是否里程碑")
    private String ifMilestone;

    @ApiModelProperty(value = "是否关键路线")
    private String ifCriticalRoute;

    @ApiModelProperty(value = "前置节点")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String preNode;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "计划开始日期")
    private String planStartDate;

    @ApiModelProperty(value = "计划结束日期")
    private String planEndDate;

    @ApiModelProperty(value = "实际开始日期")
    private String actualStartDate;

    @ApiModelProperty(value = "实际结束日期")
    private String actualEndDate;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.UPDATE)
    private Long updateUserId;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    @TableField(exist = false)
    @ApiModelProperty(value = "前置节点名称")
    private String preNodeName;


}
