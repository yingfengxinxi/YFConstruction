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
 * 视频监控执法记录
 * </p>
 *
 * @author wnj
 * @since 2021-11-25
 */
@Data
@ApiModel("视频监控执法记录")
public class JgEnforceRecordBO extends PageEntity {

    @ApiModelProperty(value = "发现时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date findDate;

    @ApiModelProperty(value = "发现时间区间开始")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date findStartDate;

    @ApiModelProperty(value = "发现时间区间结束")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date findEndDate;

    @ApiModelProperty(value = "发现人")
    private String findPeople;

    @ApiModelProperty(value = "发现问题")
    private String findProblem;

    @ApiModelProperty(value = "处罚措施")
    private String punishMeasures;

    @ApiModelProperty(value = "处罚条例")
    private String punishReg;

    @ApiModelProperty(value = "整改期限")
    private String rectTerm;

    @ApiModelProperty(value = "整改要求")
    private String rectReq;

    @ApiModelProperty(value = "执法依据附件（视频截图或录像）")
    private String batchId;

    @ApiModelProperty(value = "整改单位")
    private String rectUnit;

    @ApiModelProperty(value = "整改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date rectTime;

    @ApiModelProperty(value = "整改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date rectStartTime;

    @ApiModelProperty(value = "整改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date rectEndTime;

    @ApiModelProperty(value = "整改结果")
    private String rectResult;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;
}
