package com.kakarote.build.safety.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 安全巡检信息
 * </p>
 *
 * @author wudw
 * @since 2021-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("安全巡检信息查询对象")
public class SafetyInspectionInfoBO extends PageEntity {

    @ApiModelProperty(value = "检查编号")
    private String checkCode;

    @ApiModelProperty(value = "执行计划名称")
    private String planName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "执行时间范围 开始时间")
    private Date beginDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "执行时间范围 结束时间")
    private Date endDate;

    @ApiModelProperty(value = "检查人")
    private Long inspectorId;

    @ApiModelProperty(value = "执行状态 0:未执行 1:已执行 默认为0")
    private String isExecute;
}
