package com.kakarote.build.safety.entity.BO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 库房报警
 * </p>
 *
 * @author wnj
 * @since 2021-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("库房报警")
public class SafetyRoomInfoAlertBO extends PageEntity {

    private Integer id;

    @ApiModelProperty(value = "库房编号")
    private String roomNum;

    @ApiModelProperty(value = "库房名称 ")
    private String roomName;

    @ApiModelProperty(value = "监测项目")
    private String monitorPro;

    @ApiModelProperty(value = "监测器编号")
    private String monitorNum;

    @ApiModelProperty(value = "报警内容")
    private String alertCount;

    @ApiModelProperty(value = "报警类型")
    private String alertType;

    @ApiModelProperty(value = "批次id")
    private String batchId;

    @ApiModelProperty(value = "报警时间范围-开始时间")
    private String alarmTimeStart;

    @ApiModelProperty(value = "报警时间范围-结束时间")
    private String alarmTimeEnd;
}
