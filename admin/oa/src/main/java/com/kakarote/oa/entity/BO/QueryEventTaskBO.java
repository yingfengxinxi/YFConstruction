package com.kakarote.oa.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryEventTaskBO {

    private Long startTime;

    private Long endTime;

    private Long userId;

    @ApiModelProperty(value = "完成状态 1正在进行2延期3归档 5结束 查询不等于该参数的任务")
    private Integer status;

    @ApiModelProperty(value = "1归档")
    private Integer isArchive;
}
