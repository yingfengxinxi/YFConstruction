package com.kakarote.build.labour.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel("劳务管理--考勤管理")
public class BLabourWageBO extends PageEntity {

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

    @ApiModelProperty(value = "班组id")
    private String wageTeamName;

    @ApiModelProperty(value = "年月")
    private String wageMonth;

    @ApiModelProperty(value = "分包单位")
    private String wageSubcontractor;

    @ApiModelProperty(value = "劳务工种")
    private String wageWorkId;

    @ApiModelProperty(value = "人数")
    private String infocollectionNumber;

    @ApiModelProperty(value = "总工日")
    private Integer wageManDays;

    @ApiModelProperty(value = "总工程量")
    private Integer wageQuantities;

    @ApiModelProperty(value = "应发工资")
    private Double wageShould;

    @ApiModelProperty(value = "预支工资")
    private Double wageDvance;

    @ApiModelProperty(value = "累计剩余")
    private Double wageaCumulativeBalance;

    @ApiModelProperty(value = "发放日期")
    private String wageDate;

}