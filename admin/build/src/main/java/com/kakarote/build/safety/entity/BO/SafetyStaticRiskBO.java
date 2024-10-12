package com.kakarote.build.safety.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description="静态风险数据")
public class SafetyStaticRiskBO extends PageEntity {

    private String riskName;


    private String riskLevel;


    private String accidentType;


    private String controlLevel;


    private String dutyPerson;


    private String engineeringMeasure;


    private String controlMeasures;


    private String individualProtection;


    private String emergencyMeasures;


    private String isDelete;


    private String relieveStatus;


    private String content;


    private String deviceType;


    private String standardRequire;


    private String riskDesc;


    private Date publishDate;


    private Date updateDate;


    private String createBy;


    private Integer projectId;


    private Integer tenantId;

    private String updateBy;
}
