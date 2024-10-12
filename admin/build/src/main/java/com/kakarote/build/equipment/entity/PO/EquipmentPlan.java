package com.kakarote.build.equipment.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备监测-预防性维护计划-维护计划设置
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_plan")
@ApiModel(value="EquipmentPlan对象", description="设备监测-预防性维护计划-维护计划设置")
public class EquipmentPlan implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "plan_id", type = IdType.AUTO)
    private Integer planId;

    @ApiModelProperty(value = "计划名称")
    private String planName;

    @ApiModelProperty(value = "计划类型 字典项")
    private String planType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "开始日期")
    private Date startDate;

    @ApiModelProperty(value = "执行周期")
    private String cycle;

    @ApiModelProperty(value = "执行单位 字典项")
    private String unit;

    @ApiModelProperty(value = "执行等级 字典项")
    private String level;

    @ApiModelProperty(value = "执行部门")
    private Integer deptId;

    @ApiModelProperty(value = "是否启用 字典项")
    private String enable;

    @ApiModelProperty(value = "作业名称")
    private String workName;

    @ApiModelProperty(value = "作业指导")
    private String workDirect;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "下次执行日期")
    private Date nextDate;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    private Long updateUserId;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "部门名称")
    @TableField(exist = false)
    private String deptName;

    @ApiModelProperty(value = "计划维护设备信息")
    @TableField(exist = false)
    private List<EquipmentPlanAsset> assetList;


}
