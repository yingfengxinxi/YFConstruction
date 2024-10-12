package com.kakarote.build.ai.entity.BO;


import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("渣土车记录查询对象")
public class AiDregCarBO extends PageEntity {

    @ApiModelProperty(value = "车牌")
    private String licensePlateNumber;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;
}
