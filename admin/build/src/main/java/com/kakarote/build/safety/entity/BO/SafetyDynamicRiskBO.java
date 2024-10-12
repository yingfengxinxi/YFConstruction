package com.kakarote.build.safety.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description="动态风险数据")
public class SafetyDynamicRiskBO  extends PageEntity {



    private String riskName;


    private String riskLevel;


    private String accidentType;


    private String controlLevel;


    private String dutyPerson;


    private String relieveStatus;


    private String engineeringMeasure;


    private String controlMeasures;

    private String individualProtection;


    private String emergencyMeasures;


    private String content;


    private String riskDesc;


    private Date publishDate;


    private Date updateDate;


    private String createBy;

    private String updateBy;
}
