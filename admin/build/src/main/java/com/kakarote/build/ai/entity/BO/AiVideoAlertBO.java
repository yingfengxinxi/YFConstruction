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
@ApiModel("视屏警报查询对象")
public class AiVideoAlertBO  extends PageEntity {

    @ApiModelProperty(value = "编号")
    private String code;

    @ApiModelProperty(value = "警报类型")
    private String aiEventType;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
