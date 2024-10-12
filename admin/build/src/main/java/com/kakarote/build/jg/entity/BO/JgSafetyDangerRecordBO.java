package com.kakarote.build.jg.entity.BO;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 安全隐患记录
 * </p>
 *
 * @author wnj
 * @since 2021-11-26
 */
@Data
@ApiModel("安全隐患记录")
public class JgSafetyDangerRecordBO extends PageEntity {

    @ApiModelProperty(value = "项目名称")
    private Integer projectId;

    @ApiModelProperty(value = "项目编号")
    private String projectCode;

    @ApiModelProperty(value = "隐患编号")
    private String dangerCode;

    @ApiModelProperty(value = "隐患类别")
    private String dangerType;

    @ApiModelProperty(value = "隐患等级")
    private String dangerLevel;

    @ApiModelProperty(value = "隐患内容")
    private String dangerContent;

    @ApiModelProperty(value = "检查负责人")
    private String inspectPeople;

    @ApiModelProperty(value = "检查负责人联系电话")
    private String inspectPeoplePhone;

    @ApiModelProperty(value = "检查日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date inspectStartDate;

    @ApiModelProperty(value = "检查日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date inspectEndDate;

    @ApiModelProperty(value = "检查类型")
    private String inspectType;

    @ApiModelProperty(value = "责任单位")
    private String responUnit;

    @ApiModelProperty(value = "责任单位统一社会信用代码")
    private String responUnitCreditCode;

    @ApiModelProperty(value = "整改人")
    private String rectPerson;

    @ApiModelProperty(value = "整改人联系电话")
    private String rectPersonPhone;

    @ApiModelProperty(value = "整改期限")
    private String rectTerm;

    @ApiModelProperty(value = "上报时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reportTime;

    @ApiModelProperty(value = "检查结果")
    private String inspectResult;

    @ApiModelProperty(value = "检查区域")
    private String inspectRegion;

    @ApiModelProperty(value = "整改完成时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date rectOverTime;

    @ApiModelProperty(value = "复查人")
    private String reviewPeopel;

    @ApiModelProperty(value = "复查结果")
    private String reviewResult;

    @ApiModelProperty(value = "复查完成时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reviewOverTime;

    @ApiModelProperty(value = "说明")
    private String remarks;

    @ApiModelProperty(value = "罚款信息")
    private String fineInfo;

    @ApiModelProperty(value = "补充信息")
    private String supplementInfo;

}
