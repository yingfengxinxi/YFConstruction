package com.kakarote.build.equipment.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 *
 * @author lzy
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("计划执行列表查询对象")
public class EquipmentSetBO extends PageEntity {

    @ApiModelProperty(value = "计划名称")
    private String planName;

    @ApiModelProperty(value = "计划类型")
    private String planType;

    @ApiModelProperty(value = "执行登记")
    private String level;

    @ApiModelProperty(value = "执行部门")
    private Integer deptId;

    @ApiModelProperty(value = "执行人")
    private String setBy;

    @ApiModelProperty(value = "是否执行")
    private String isSet;

    private Integer assetId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date searchStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date searchEndDate;

    private String startDate;

    private String endDate;
}
