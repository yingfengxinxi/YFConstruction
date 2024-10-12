package com.kakarote.build.safety.entity.PO;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2021-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_inspection_plan")
@ApiModel(value="SafetyInspectionPlan对象", description="")
public class SafetyInspectionPlan implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "计划名称")
    private String planName;

    @ApiModelProperty(value = "计划类型 字典项")
    private String planType;

    @ApiModelProperty(value = "是否启用 1:启用 0:禁用")
    private String isEnable;

    @ApiModelProperty(value = "执行周期")
    private String cycle;

    @ApiModelProperty(value = "执行周期单位 字典项")
    private String cycleUnit;

    @ApiModelProperty(value = "计划等级")
    private String planLevel;

    @ApiModelProperty(value = "执行部门")
    private Integer deptId;

    @ApiModelProperty(value = "计划描述")
    private String planDescribe;


    @ApiModelProperty(value = "启动日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @ApiModelProperty(value = "下次执行日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nextDate;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "修改人")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateUserId;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "部门名称")
    @TableField(exist = false)
    private String deptName;

}
