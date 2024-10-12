package com.kakarote.build.project.entity.PO;

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
 * 计划管理--月计划--月劳务明细
 * </p>
 *
 * @author zy
 * @since 2021-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_progress_plan_month_labor")
@ApiModel(value="ProgressPlanMonthLabor对象", description="计划管理--月计划--月劳务明细")
public class ProgressPlanMonthLabor implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "年月")
    private String planMonth;

    @ApiModelProperty(value = "劳务工种")
    private String workTypeId;

    @ApiModelProperty(value = "本月计划人数")
    private Integer curMonPlanPeopleNum;

    @ApiModelProperty(value = "本月实际人数")
    private Integer curMonActualPeopleNum;

    @ApiModelProperty(value = "本月偏差")
    private String curMonDeviation;

    @ApiModelProperty(value = "工程量")
    private String engineeringMount;

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
