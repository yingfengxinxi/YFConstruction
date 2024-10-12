package com.kakarote.build.safety.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BSafetySafetyTrainingBO extends PageEntity {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "培训计划")
    private String trainingPlanId;

    @ApiModelProperty(value = "培训类型  字典表safety_training_type")
    private String trainingType;

    @ApiModelProperty(value = "培训时间")
    private Date trainingTime;

    @ApiModelProperty(value = "培训地点")
    private String trainingAddress;

    @ApiModelProperty(value = "培训内容")
    private String trainingContent;

    @ApiModelProperty(value = "授课人")
    private String trainingTeaching;

    @ApiModelProperty(value = "课时")
    private String trainingTeachingTime;

    @ApiModelProperty(value = "授课地点")
    private String trainingTeachingAddress;

    @ApiModelProperty(value = "负责人")
    private String trainingPeople;

}
