package com.kakarote.build.ai.entity.BO;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("渣土车辆智能分析")
public class AiBareSoilDataBO extends PageEntity {

    @ApiModelProperty(value = "报警类型")
    private String alarmType;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "报警位置")
    private String alarmLocation;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value="")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date analysisTime;
}
