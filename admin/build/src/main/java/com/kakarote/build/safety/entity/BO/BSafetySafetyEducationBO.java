package com.kakarote.build.safety.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BSafetySafetyEducationBO extends PageEntity {

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

    @ApiModelProperty(value = "题目分类  字典表safety_education_type")
    private String safetyEducationType;

    @ApiModelProperty(value = "题目描述")
    private String safetyEducationDescribe;

    @ApiModelProperty(value = "选项类型  字典表safety_education_options")
    private String safetyEducationOptions;

    @ApiModelProperty(value = "选项集合")
    private String safetyEducationProblem;

    @ApiModelProperty(value = "正确答案")
    private String safetyEducationAnswer;

    @ApiModelProperty(value = "分值")
    private Double safetyEducationScore;

}
