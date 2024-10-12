package com.kakarote.build.ai.entity.BO;


import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("渣土车辆智能分析")
public class AiDregCarAlertBO extends PageEntity {

    @ApiModelProperty(value = "编号")
    private String code;

    @ApiModelProperty(value = "警报类型")
    private String alertType;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "是否查询处理数据 1：查询未处理数据 0：不进行该条件过滤")
    private String isDispose;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;
}
