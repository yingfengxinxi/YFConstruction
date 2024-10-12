package com.kakarote.build.labour.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Title: LabourWorktypeBO
 * @Package com.kakarote.build.labour.entity.BO
 * @Description: 描述
 * @author: ZangYu
 * @date: 2021-03-15 11:38
 */
@Data
@ApiModel("劳务管理--分包商查询对象")
public class LabourSubContractorBO extends PageEntity {

    @ApiModelProperty(value = "分包商名称")
    private String realName;

    @ApiModelProperty(value = "分包商类型")
    private String subcontractorType;

}