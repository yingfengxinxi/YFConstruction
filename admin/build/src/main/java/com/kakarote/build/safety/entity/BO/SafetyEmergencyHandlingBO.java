package com.kakarote.build.safety.entity.BO;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 应急事件处置
 * </p>
 *
 * @author zy
 * @since 2021-04-06
 */
@Data
@ApiModel("应急事件处置")
public class SafetyEmergencyHandlingBO extends PageEntity {

    @ApiModelProperty(value = "事故编号")
    private String code;

    @ApiModelProperty(value = "事故发生时间")
    private Date happenedTime;

    @ApiModelProperty(value = "事故发生地点")
    private String happenedAddress;

    @ApiModelProperty(value = "事故类型")
    private String accidentType;


    @ApiModelProperty(value = "批次id")
    private String batchId;


    //事故发生时间 查询范围 开始
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String happenedTimeStart;

    //事故发生时间 查询范围 结束
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String happenedTimeEnd;



}
