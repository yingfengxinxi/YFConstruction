package com.kakarote.build.project.entity.BO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value="月度自评")
public class ProgressSelfEvaluateBO extends PageEntity {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "检查目录id")
    private String securityId;

    @ApiModelProperty(value = "检查评定项目")
    private String securityEvaluationId;

    @ApiModelProperty(value = "应得分数")
    private Integer shouldScore;


    @ApiModelProperty(value = "扣分标准")
    private String securityEvaluationStandard;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM", timezone = "GMT+8")
    @ApiModelProperty(value = "评估月份")
    private String evaluateMonth;

    @ApiModelProperty(value = "分数")
    private Integer mark;

    private String securityName;

    private String securityEvaluationName;

    private String securityEvaluationSubitem;
}
