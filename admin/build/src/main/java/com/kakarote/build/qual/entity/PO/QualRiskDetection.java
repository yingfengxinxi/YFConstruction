package com.kakarote.build.qual.entity.PO;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lzy
 * @since 2022-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_risk_detection")
@ApiModel(value="QualRiskDetection对象", description="")
public class QualRiskDetection implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "数据类型：1：日常质量巡检、2：企业质量巡检")
    private String dataType;

    @ApiModelProperty(value = "隐患编号")
    private String hiddenNum;

    @ApiModelProperty(value = "检查类型：月底检查、季度检查、专项检查")
    private String inspectionType;

    @ApiModelProperty(value = "隐患状态描述 0：已检查；1：已下发；:2：已整改；3：已复检")
    private String infoState;

    @ApiModelProperty(value = "检查时间")
    private Date inspectionTime;

    @ApiModelProperty(value = "检查人 当前登录用户")
    private Long inspectorsId;

    @ApiModelProperty(value = "检查人姓名")
    private String inspectorsName;

    @ApiModelProperty(value = "检查人手机号")
    private String inspectorsPhone;

    @ApiModelProperty(value = "巡检部位")
    private String inspectionPosition;

    @ApiModelProperty(value = "问题描述")
    private String problemDescription;

    @ApiModelProperty(value = "问题类型 质量问题库id")
    private Integer questionId;

    @ApiModelProperty(value = "问题级别 字典项")
    private String problemLevel;

    @ApiModelProperty(value = "紧急程度 字典项")
    private String urgency;

    @ApiModelProperty(value = "整改责任单位 数据源：分包商")
    private Integer dutyUnitId;

    @ApiModelProperty(value = "整改负责人 分包商下组长")
    private Integer rectifyPersonId;

    @ApiModelProperty(value = "整改负责人联系电话")
    private String rectifyPersonPhone;

    @ApiModelProperty(value = "实际整改人")
    private Integer actualRectifyPersonId;

    @ApiModelProperty(value = "实际整改人姓名")
    private String actualRectifyPersonName;

    @ApiModelProperty(value = "实际整改人手机号")
    private String actualRectifyPersonPhone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "整改期限")
    private Date rectifyTime;

    @ApiModelProperty(value = "整改时间")
    private Date rectifyCompleteTime;

    @ApiModelProperty(value = "整改结果")
    private String rectificationResult;

    @ApiModelProperty(value = "补充说明")
    private String rectificationReply;

    @ApiModelProperty(value = "复查完成时间")
    private Date reviewTime;

    @ApiModelProperty(value = "复查结论")
    private String reviewResult;

    @ApiModelProperty(value = "复查人id 当前登录用户")
    private Long reviewPersonId;

    @ApiModelProperty(value = "复查人姓名")
    private String reviewPersonName;

    @ApiModelProperty(value = "复查人手机号")
    private String reviewPersonPhone;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    private Long updateUserId;

    @ApiModelProperty(value = "隐患图片id")
    private String batchId;

    @ApiModelProperty(value = "整改图片id")
    private String rectifyBatchId;

    @ApiModelProperty(value = "是否删除 0：未删除 1：删除")
    private String isDelete;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "人员对象")
    @TableField(exist = false)
    private String questionName;


}
