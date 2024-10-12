package com.kakarote.build.qual.entity.PO;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 施工质量--质量检查管理--安全隐患
 * </p>
 *
 * @author lzy
 * @since 2021-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_hidden_trouble")
@ApiModel(value="QualHiddenTrouble对象", description="施工质量--质量检查管理--安全隐患")
public class QualHiddenTrouble implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "hidden_trouble_id", type = IdType.AUTO)
    private Integer hiddenTroubleId;

    @ApiModelProperty(value = "质量检查Id")
    private Integer checkId;

    @ApiModelProperty(value = "隐患位置")
    private String place;

    @ApiModelProperty(value = "隐患描述")
    private String qualTrobuleDescription;

    @ApiModelProperty(value = "紧急程度 字典项 1:高  2:中 3:低")
    private String priority;

    @ApiModelProperty(value = "隐患等级 字典项")
    private String level;

    @ApiModelProperty(value = "实际整改人id")
    private Integer actualCorrectorId;

    @ApiModelProperty(value = "实际整改人姓名")
    private String actualCorrectorName;

    @ApiModelProperty(value = "质量问题id")
    private Integer questionId;

    @ApiModelProperty(value = "质量问题名称")
    private String questionName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "整改时间")
    private Date rectifyDate;

    @ApiModelProperty(value = "整改效果说明")
    private String receiptInfo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "复检时间")
    private Date recheckDate;

    @ApiModelProperty(value = "复检人")
    private Long recheckPeopleId;

    @ApiModelProperty(value = "复检结论")
    private String recheckConclusion;

    @ApiModelProperty(value = "隐患图片")
    private String hiddenTroubleBatchId;

    @ApiModelProperty(value = "回执图片")
    private String receiptImgBatchId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(select = false)
    private Integer tenantId;

    private Integer projectId;


}
