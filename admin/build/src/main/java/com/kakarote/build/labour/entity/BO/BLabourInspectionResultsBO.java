package com.kakarote.build.labour.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel("企业报验结果表")
public class BLabourInspectionResultsBO extends PageEntity {

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

    @ApiModelProperty(value = "企业报验id")
    private String inspectionId;

    @ApiModelProperty(value = "验收结束日期/整改日期")
    private Date resultsTime;

    @ApiModelProperty(value = "验收说明/整改说明")
    private String resultsContent;

    @ApiModelProperty(value = "验收附件/整改附件")
    private String batchId;

    @ApiModelProperty(value = "备注")
    private String remarks;


}