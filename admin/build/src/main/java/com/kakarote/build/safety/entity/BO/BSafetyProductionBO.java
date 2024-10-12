package com.kakarote.build.safety.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BSafetyProductionBO extends PageEntity {

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

    @ApiModelProperty(value = "评定事项")
    private String productionId;

    @ApiModelProperty(value = "评定项目")
    private String productionName;

    @ApiModelProperty(value = "评分标准")
    private String productionStandard;

    @ApiModelProperty(value = "评分方法")
    private String productionMethods;

    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "备注")
    private String remarks;
}
