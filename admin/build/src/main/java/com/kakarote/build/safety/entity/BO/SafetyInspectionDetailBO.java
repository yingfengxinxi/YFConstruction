package com.kakarote.build.safety.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("施工安全--安全巡检信息--子表巡检明细查询对象")
public class SafetyInspectionDetailBO extends PageEntity {


    @ApiModelProperty(value = "危险源")
    private Integer dangerSourceId;

    private List<Integer> dangerSourceIds;


    // 监管端-安全隐患查询

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "隐患内容")
    private String problemDescription;

    @ApiModelProperty(value = "隐患状态")
    private String hiddenState;

    @ApiModelProperty(value = "检查日期-开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date inspectStartDate;

    @ApiModelProperty(value = "检查日期-结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date inspectEndDate;

    @ApiModelProperty(value = "时间段查询，1今天，2本周，3本月，4本年，为空查询全部（仅大屏数据分析）")
    private String timeSlot;

}
