package com.kakarote.build.labour.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("建设单位管理")
public class LabourConstructionUnitBO extends PageEntity {

    @ApiModelProperty(value = "企业统一社会信用代码，如果无统一社会信用代码，则用组织机构代码")
    private String corpCode;

    @ApiModelProperty(value = "企业名称")
    private String corpName;

    @ApiModelProperty(value = "法定代表人姓名")
    private String legalMan;


}
