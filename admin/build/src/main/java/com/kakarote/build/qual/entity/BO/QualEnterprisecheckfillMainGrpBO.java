package com.kakarote.build.qual.entity.BO;

import com.baomidou.mybatisplus.annotation.*;
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
 * 施工质量--企业质量检查填报--主表
 * </p>
 *
 * @author wnj
 * @since 2021-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="QualEnterprisecheckfillMainGrp对象", description="施工质量--企业质量检查填报--主表")
public class QualEnterprisecheckfillMainGrpBO extends PageEntity {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "审批状态  0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废")
    private Integer checkStatus;

    @ApiModelProperty(value = "审核记录ID")
    private Integer examineRecordId;

    @ApiModelProperty(value = "负责人ID  若没有业务不需要负责人，则存入创建人即可")
    private Long ownerUserId;

    @ApiModelProperty(value = "检查模板id")
    private Integer checkTemplateId;

    @ApiModelProperty(value = "检查项目")
    private Integer checkProject;

    @ApiModelProperty(value = "检查人id")
    private Integer checkPersonId;

    @ApiModelProperty(value = "检查人姓名")
    private String checkPersonName;

    @ApiModelProperty(value = "检查日期")
    private Date checkDate;

    @ApiModelProperty(value = "检查得分")
    private Integer checkScore;

    @ApiModelProperty(value = "检查得分开始")
    private Integer checkScoreStart;

    @ApiModelProperty(value = "检查得分结束")
    private Integer checkScoreEnd;

    @ApiModelProperty(value = "评定结果")
    private String evaluateResult;


}
