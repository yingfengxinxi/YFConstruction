package com.qdoner.build.equipment.entity.PO;

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
 * @author shz
 * @since 2023-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_pit_report")
@ApiModel(value="EquipmentPitReport对象", description="基坑监测报告")
public class EquipmentPitReport implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "编号")
    private String code;

    @ApiModelProperty(value = "观测时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date observeTime;

    @ApiModelProperty(value = "观测人")
    private String observeBy;

    @ApiModelProperty(value = "计算人")
    private String countBy;

    @ApiModelProperty(value = "校队人")
    private String calibrationBy;

    @ApiModelProperty(value = "监测单位")
    private String observeUnit;

    @ApiModelProperty(value = "附件id")
    private String batchId;

    @ApiModelProperty(value = "备注说明")
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


}
