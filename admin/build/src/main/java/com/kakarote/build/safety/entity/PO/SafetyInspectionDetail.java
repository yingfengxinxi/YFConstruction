package com.kakarote.build.safety.entity.PO;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.servlet.upload.FileEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 施工安全--安全巡检信息--子表巡检明细
 * </p>
 *
 * @author lzy
 * @since 2021-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_inspection_detail")
@ApiModel(value="SafetyInspectionDetail对象", description="施工安全--安全巡检信息--子表巡检明细")
public class SafetyInspectionDetail implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer inspectionInfoId;

    @ApiModelProperty(value = "巡检部位")
    private String place;

    @ApiModelProperty(value = "问题描述")
    private String problemDescription;

    @ApiModelProperty(value = "巡检结论")
    private String inspectionConclusion;

    @ApiModelProperty(value = "隐患等级 字典项 ")
    private String level;

    @ApiModelProperty(value = "风险点id")
    private String riskId;

    @ApiModelProperty(value = "风险点数据类型 1：静态风险 表 b_safety_static_risk  2：动态风险 表 b_safety_dynamic_risk")
    private String riskDataType;

    @ApiModelProperty(value = "危险源")
    private Integer dangerSourceId;

    @ApiModelProperty(value = "危险源名称")
    private String dangerName;

    @ApiModelProperty(value = "实际整改人")
    private String rectificationPeople;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "整改时间")
    private Date rectifyDate;

    @ApiModelProperty(value = "整改效果说明")
    private String rectifyEffect;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "复检时间")
    private Date recheckDate;

    @ApiModelProperty(value = "复检人")
    private Long recheckPeopleId;

    @ApiModelProperty(value = "复检结论")
    private String recheckConclusion;

    @ApiModelProperty(value = "图片批次id")
    private String batchId;

    @TableField(select = false)
    private Integer tenantId;

    private Integer projectId;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片")
    private List<FileEntity> imgs;

    @ApiModelProperty(value = "隐患类别 basic:基础管理类隐患  production:生产现场类隐患")
    private String category;

    @ApiModelProperty(value = "检查类型")
    private String routineCheck;

    @ApiModelProperty(value = "责任单位")
    private String dutyUnit;

    @ApiModelProperty(value = "责任单位统一社会信用代码")
    private String dutyUnitCode;

    @ApiModelProperty(value = "整改人电话")
    private String rectifyPersonPhone;

    @ApiModelProperty(value = "补充信息/备注")
    private String supplementInfo;

    @ApiModelProperty(value = "整改结果")
    private String checkResult;

    @ApiModelProperty(value = "劳务队伍")
    private String serviceTeam;

    @ApiModelProperty(value = "劳务班组")
    private Integer serviceClass;

    @ApiModelProperty(value = "处罚信息")
    private String fineInfo;

    @ApiModelProperty(value = "所属公司")
    private String belongCompany;

    @ApiModelProperty(value = "上报时间")
    private Date publishDate;

    @ApiModelProperty(value = "通知人")
    private Integer informPerson;

    @ApiModelProperty(value = "隐患模型id")
    private Integer modelId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建用户")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "更新用户")
    @TableField(fill = FieldFill.INSERT)
    private Long updateUserId;

    @TableField(exist = false)
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @TableField(exist = false)
    @ApiModelProperty(value = "检查人")
    private Long inspectorId;

    @TableField(exist = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "检查时间")
    private Date checkDate;

    @TableField(exist = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "整改期限")
    private Date repairTimeLimit;

    @TableField(exist = false)
    @ApiModelProperty(value = "隐患状态 1：整改中 2：复检中 3：已完成")
    private String hiddenState;
}
