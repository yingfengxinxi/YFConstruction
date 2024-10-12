package com.kakarote.build.jg.entity.BO;

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
 * 现场安全监督
 * </p>
 *
 * @author wnj
 * @since 2021-11-25
 */
@Data
@ApiModel("现场安全监督")
public class JgSupervisionBO extends PageEntity {


    @ApiModelProperty(value = "项目名称")
    private Integer projectId;

    @ApiModelProperty(value = "项目地址")
    private String projectAddress;

    @ApiModelProperty(value = "巡查时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date patrolStartTime;

    @ApiModelProperty(value = "巡查时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date patrolEndTime;

    @ApiModelProperty(value = "巡查内容")
    private String patrolContent;

    @ApiModelProperty(value = "巡查综述")
    private String patrolOverview;

    @ApiModelProperty(value = "处理意见")
    private String handleOpinion;

    @ApiModelProperty(value = "巡查人")
    private String patrolPeople;

    @ApiModelProperty(value = "陪同巡查人")
    private String patrolPeopleAccom;

    @ApiModelProperty(value = "备注")
    private String remarks;

}
