package com.kakarote.build.safety.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 施工安全--安全巡检信息
 * </p>
 *
 * @author lzy
 * @since 2021-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_inspection_info")
@ApiModel(value="SafetyInspectionInfo对象", description="施工安全--安全巡检信息")
public class SafetyInspectionInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "安全检查计划id")
    private Integer planId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "执行日期")
    private Date executeDate;

    @ApiModelProperty(value = "检查编号")
    private String checkCode;

    @ApiModelProperty(value = "检查计划名称")
    private String planName;

    @ApiModelProperty(value = "检查计划等级")
    private String planLevel;

    @ApiModelProperty(value = "检查部门")
    private Integer deptId;

    @ApiModelProperty(value = "检查人")
    private Long inspectorId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "检查时间")
    private Date checkDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "整改期限")
    private Date repairTimeLimit;

    @ApiModelProperty(value = "执行状态 0:未执行 1:已执行 默认为0")
    private String isExecute;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.UPDATE)
    private Integer updateUserId;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "部门名称")
    @TableField(exist = false)
    private String deptName;

    @ApiModelProperty(value = "检查员名称")
    @TableField(exist = false)
    private String realname;

    @ApiModelProperty(value = "执行之后的状态：1：整改；2：复检")
    @TableField(exist = false)
    private String rectifyStatus;

}
