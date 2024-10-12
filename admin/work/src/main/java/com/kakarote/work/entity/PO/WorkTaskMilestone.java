package com.kakarote.work.entity.PO;

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
 * 任务里程碑表
 * </p>
 *
 * @author author
 * @since 2020-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_work_task_milestone")
@ApiModel(value="WorkTaskMilestone对象", description="任务里程碑表")
public class WorkTaskMilestone implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "任务里程碑表")
    @TableId(value = "milestone_id", type = IdType.AUTO)
    private Integer milestoneId;

    @ApiModelProperty(value = "任务ID")
    private Integer taskId;

    @ApiModelProperty(value = "标记ID")
    private Long userId;

    @ApiModelProperty(value = "标记时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "是否里程碑")
    private Integer status;

    private Integer tenantId;


}
