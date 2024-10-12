package com.kakarote.build.project.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("项目管理查询对象")
public class ProjectInfoBO extends PageEntity {

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "项目状态")
    private String projectState;

    @ApiModelProperty(value = "工程状态")
    private String engineeringStatus;

    @ApiModelProperty(value = "开工令起日")
    private String commencementOrderDateStart;

    @ApiModelProperty(value = "开工令至日")
    private String commencementOrderDateEnd;

    @ApiModelProperty(value = "开工令起日")
    private String actualCompletionDateStart;

    @ApiModelProperty(value = "开工令至日")
    private String actualCompletionDateEnd;



}
