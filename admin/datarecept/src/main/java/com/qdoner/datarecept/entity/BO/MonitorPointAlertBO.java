package com.qdoner.datarecept.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("监测点警报查询对象")
public class MonitorPointAlertBO extends PageEntity {

    private Integer id;

    @ApiModelProperty(value = "设备名称")
    private String assetName;

    @ApiModelProperty(value = "监控点名称")
    private String pointName;

    @ApiModelProperty(value = "监控点类型")
    private String monitorType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "报警开始时间")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "报警结束时间")
    private Date endDate;

    @ApiModelProperty(value = "处理时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String handleTime;

    @ApiModelProperty(value = "处理人")
    private String handleBy;

    @ApiModelProperty(value = "处理措施")
    private String handleMeasures;

    @ApiModelProperty(value = "处理后结论")
    private String handleConclu;

    @ApiModelProperty(value = "处理状态")
    private String handleState;

    @ApiModelProperty(value = "提醒类型  0预警提醒，1警报提醒 2控制")
    private String alertType;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "检测项子类型 字典项：1：PM2.5、2：PM10、3：噪音、")
    private String monitorSonType;

    @ApiModelProperty(value = "时间段查询，1今天，2本周，3本月，4本年，为空查询全部（仅大屏数据分析）")
    private String timeSlot;

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "设备id")
    private Integer equipmentId;
}
