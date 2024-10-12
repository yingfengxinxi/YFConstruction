package com.kakarote.build.qual.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 质量巡检
 * </p>
 *
 * @author lzy
 * @since 2021-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("质量检查查询对象")
public class QualCheckBO extends PageEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "检查起日")
    private Date startCheckDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "检查至日")
    private Date endCheckDate;

    @ApiModelProperty(value = "检查人姓名")
    private String qualCheckPersonName;

    @ApiModelProperty(value = "检查单位")
    private String qualCheckCom;

    @ApiModelProperty(value = "检查类型")
    private String qualCheckType;

    @ApiModelProperty(value = "单据状态 0未回执 1已回执")
    private String qualReceiptStatus;

    @ApiModelProperty(value = "单据编号")
    private String qualDocumentNumber;

    @ApiModelProperty(value = "项目人员")
    private String qualPrincipalName;
}
