package com.kakarote.build.equipment.entity.BO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class EquipmentElevatorEffectBO   extends PageEntity {

    @ApiModelProperty(value = "升降机id")
    private Integer assetId;

    private String assetName;

    private String assetCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "载重")
    private Double weight;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Integer tenantId;

    private Integer projectId;

    private String projectName;

    @ApiModelProperty(value = "载重百分比")
    private Double percentRate;

    @ApiModelProperty(value = "人数")
    private Integer numberOfPeople;

    @ApiModelProperty(value = "开始楼层")
    private Integer startFloor;

    @ApiModelProperty(value = "呼叫楼层")
    private Integer endFloor;

    @ApiModelProperty(value = "风速")
    private Double wind;

    @ApiModelProperty(value = "升降机运行速度")
    private Double speed;

    @ApiModelProperty(value = "倾斜角度")
    private Double angle;

    @ApiModelProperty(value = "Bypass开关状态 0关闭，1开启")
    private String bypass;

    @ApiModelProperty(value = "预警码；无则填0")
    private Integer earlyAlarm;

    @ApiModelProperty(value = "报警码；无则填0")
    private Integer alarm;

    @ApiModelProperty(value = "y倾斜角度")
    private Double angleY;

    @ApiModelProperty(value = "本次起升起点高度")
    private Double startHeight;

    @ApiModelProperty(value = "本次起升终点高度")
    private Double endHeight;

    @ApiModelProperty(value = "本次起升的行程高度")
    private Double height;

    @ApiModelProperty(value = "本次起升方向")
    private String direction;
}
