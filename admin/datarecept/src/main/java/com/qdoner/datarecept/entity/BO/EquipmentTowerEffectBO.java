package com.qdoner.datarecept.entity.BO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
* @Description:塔机功效查询类
* @author shz
* @date 2022-7-19 14:12
* @param 
* @return 
*/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("塔机功效查询类")
public class EquipmentTowerEffectBO  extends PageEntity {

    @ApiModelProperty(value = "塔机id")
    private Integer assetId;

    @ApiModelProperty(value = "塔机名称")
    private Integer assetName;

    @ApiModelProperty(value = "塔机编号")
    private Integer assetCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "平均吊重")
    private Double weight;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Integer tenantId;

    private Integer projectId;

    private String projectName;

    @ApiModelProperty(value = "最大幅度")
    private Double maxRadius;

    @ApiModelProperty(value = "起始回转角度")
    private Double startAngle;

    @ApiModelProperty(value = "最大力矩百分比")
    private Double maxPercent;

    @ApiModelProperty(value = "最大高度")
    private Double maxHeight;

    @ApiModelProperty(value = "卸吊点的回转角度")
    private Double endAngle;

    @ApiModelProperty(value = "本次吊物过程中的最小幅度")
    private Double minRadius;

    @ApiModelProperty(value = "本次吊物过程中的最大起重本次吊物最大风速")
    private Double maxLoad;

    @ApiModelProperty(value = "本次吊物最大风速")
    private Double maxWindSpeed;

    @ApiModelProperty(value = "本次吊物过程中的最小高度")
    private Double minHeight;

    @ApiModelProperty(value = "力矩最大点系统预警；无则填0")
    private Integer maxMomentEarlyAlarm;

    @ApiModelProperty(value = "卸吊点的系统预警状态编码；无则填0")
    private Integer endEarlyAlarm;

    @ApiModelProperty(value = "起吊点的系统预警状态编码；无则填0")
    private Integer startEarlyAlarm;

    @ApiModelProperty(value = "力矩最大点系统报警状态编码；无则填0")
    private Integer maxMomentAlarm;

    @ApiModelProperty(value = "卸吊点的系统报警状态编码；无则填0")
    private Integer endAlarm;

    @ApiModelProperty(value = "起吊点的系统报警状态编码；无则填0")
    private Integer startAlarm;

    @ApiModelProperty(value = "起吊点违章码；无则填0")
    private Integer startPeccancy;

    @ApiModelProperty(value = "卸吊点违章码；无则填0")
    private Integer endPeccancy;

    @ApiModelProperty(value = "力矩最大点违章码；无则填0")
    private Integer maxMomentPeccancy;



}
