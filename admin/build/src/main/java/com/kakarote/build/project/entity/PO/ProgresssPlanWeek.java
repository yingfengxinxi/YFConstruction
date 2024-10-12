package com.kakarote.build.project.entity.PO;

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
import org.apache.commons.configuration.beanutils.BeanFactory;

/**
 * <p>
 * 计划管理--周计划
 * </p>
 *
 * @author zy
 * @since 2021-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_progresss_plan_week")
@ApiModel(value="ProgresssPlanWeek对象", description="计划管理--周计划")
public class ProgresssPlanWeek implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "周计划开始日期")
    private String weekStartDate;

    @ApiModelProperty(value = "周计划结束日期")
    private String weekEndDate;

    @ApiModelProperty(value = "月计划id")
    private String planMonthDetailId;

    @ApiModelProperty(value = "模板id")
    private Integer mid;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "序号")
    private Integer serialNumber;

    @ApiModelProperty(value = "总计划明细表的模板明细id（与父任务相匹配）")
    private String planDetailMdId;

    @ApiModelProperty(value = "父任务id")
    private String parentId;

    @ApiModelProperty(value = "计划工期天数")
    private Integer planGqiNumber;

    @ApiModelProperty(value = "单体楼座ID(可多选)")
    private String relatedBuildingId;

    @ApiModelProperty(value = "计划开始日期")
    private String planStartDate;

    @ApiModelProperty(value = "计划结束日期")
    private String planEndDate;

    @ApiModelProperty(value = "实际开始日期")
    private String actualStartDate;

    @ApiModelProperty(value = "实际结束日期")
    private String actualEndDate;

    @ApiModelProperty(value = "实际工期天数")
    private Integer actualGqiNumber;

    @ApiModelProperty(value = "材料种类")
    private String materialType;

    @ApiModelProperty(value = "计划量")
    private String planAmount;

    @ApiModelProperty(value = "实际量")
    private String actualAmount;

    @ApiModelProperty(value = "单位")
    private String unit;

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
