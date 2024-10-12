package com.kakarote.build.equipment.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("基坑监测报告")
public class EquipmentPitReportBO extends PageEntity {

    @ApiModelProperty(value = "编号")
    private String code;

    @ApiModelProperty(value = "观测人")
    private String observeBy;

    @ApiModelProperty(value = "计算人")
    private String countBy;

    @ApiModelProperty(value = "校队人")
    private String calibrationBy;

    @ApiModelProperty(value = "监测单位")
    private String observeUnit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    private Date endDate;

}
