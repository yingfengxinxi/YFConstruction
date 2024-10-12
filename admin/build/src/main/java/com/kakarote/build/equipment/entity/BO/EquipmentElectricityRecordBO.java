package com.kakarote.build.equipment.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 *
 * @author lzy
 * @since 2021-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("设备用电量查询接口")
public class EquipmentElectricityRecordBO extends PageEntity {

    @ApiModelProperty(value = "报表类型 1：选择年；2：选择月；3：时间范围选择")
    private String dateType;

    @ApiModelProperty(value = "部门id")
    private Integer deptId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "查询时间")
    private Date date;

    @ApiModelProperty(value = "查询列")
    private List<String> cols;

    @ApiModelProperty(value = "时间格式化字符串")
    private String dateFormat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @ApiModelProperty(value = "项目Id")
    private Integer projectId;
}
