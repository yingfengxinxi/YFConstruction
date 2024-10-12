package com.kakarote.build.labour.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("人员进场退场管理")
public class LabourAdvanceRetreatBO  extends PageEntity {

    @ApiModelProperty(value = "工人所属企业统一社会信用编码")
    private String corpCode;

    @ApiModelProperty(value = "工人所属企业名称")
    private String corpName;

    @ApiModelProperty(value = "班组编号")
    private Integer teamId;

    @ApiModelProperty(value = "证件类型")
    private String idCardType;

    @ApiModelProperty(value = "人员名称")
    private String name;

    @ApiModelProperty(value = "证件号码")
    private String idCardNumber;

    private String type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}
