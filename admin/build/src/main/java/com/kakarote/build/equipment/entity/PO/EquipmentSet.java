package com.kakarote.build.equipment.entity.PO;

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
 * 
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_set")
@ApiModel(value="EquipmentSet对象", description="")
public class EquipmentSet implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "set_id", type = IdType.AUTO)
    private Integer setId;

    private Integer planId;

    @ApiModelProperty(value = "执行编码")
    private String setCode;

    @ApiModelProperty(value = "计划名称")
    private String planName;

    @ApiModelProperty(value = "计划类型")
    private String planType;

    @ApiModelProperty(value = "执行周期")
    private String cycle;

    @ApiModelProperty(value = "执行单位")
    private String unit;

    @ApiModelProperty(value = "执行登记")
    private String level;

    @ApiModelProperty(value = "执行部门")
    private Integer deptId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "执行时间")
    private Date setDate;

    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @ApiModelProperty(value = "执行人")
    private String setBy;

    @ApiModelProperty(value = "执行情况说明")
    private String setContent;

    @ApiModelProperty(value = "是否执行")
    private String isSet;

    private String remarks;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    private Long updateUserId;

    private Integer tenantId;

    private Integer projectId;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门名称")
    private String deptName;


}
