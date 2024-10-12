package com.kakarote.build.labour.entity.BO;

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
 * 零工签证
 * </p>
 *
 * @author zy
 * @since 2021-06-07
 */
@Data
@ApiModel("零工签证")
public class LabourOddVisaBO extends PageEntity {


    @ApiModelProperty(value = "记录人姓名")
    private String recordPeopleName;

    @ApiModelProperty(value = "记录人ID")
    private Long recordPeopleId;

    @ApiModelProperty(value = "派工时间")
    private Date dispatchTime;

    @ApiModelProperty(value = "派工原因")
    private String dispatchReason;

    @ApiModelProperty(value = "派工单位id")
    private Integer dispatchUnitId;

    @ApiModelProperty(value = "派工单位名称")
    private String dispatchUnitName;

    @ApiModelProperty(value = "派工班组id")
    private Integer dispatchTeamId;

    @ApiModelProperty(value = "派工班组名称")
    private String dispatchTeamName;

    @ApiModelProperty(value = "派工人员id")
    private Integer dispatchPeopleId;

    @ApiModelProperty(value = "派工人员姓名")
    private String dispatchPeopleName;

    @ApiModelProperty(value = "派工内容")
    private String dispatchText;

    @ApiModelProperty(value = "签证部门id")
    private Long visaDepartmentId;

    @ApiModelProperty(value = "签证部门名称")
    private String visaDepartmentName;

    @ApiModelProperty(value = "责任人id")
    private Long responsibleId;

    @ApiModelProperty(value = "责任人姓名")
    private String responsibleName;

    @ApiModelProperty(value = "作业开始时间")
    private Date workTimeStart;

    @ApiModelProperty(value = "作业结束时间")
    private Date workTimeEnd;


    //用于 查询使用，表没有此字段
    @ApiModelProperty(value = "派工开始时间")
    private String dispatchTimeStart;

    @ApiModelProperty(value = "派工结束时间")
    private String dispatchTimeEnd;






}
