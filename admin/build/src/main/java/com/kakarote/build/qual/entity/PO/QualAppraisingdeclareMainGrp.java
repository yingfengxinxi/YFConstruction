package com.kakarote.build.qual.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.wf.entity.WfVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 施工质量--创优评优申报--主表
 * </p>
 *
 * @author wnj
 * @since 2021-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_appraisingdeclare_main_grp")
@ApiModel(value = "QualAppraisingdeclareMainGrp对象", description = "施工质量--创优评优申报--主表")
public class QualAppraisingdeclareMainGrp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty(value = "创优评优批次")
    @TableField("Appraising_batch")
    private String appraisingBatch;

    @ApiModelProperty(value = "评审开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reviewTime;

    @ApiModelProperty(value = "评审结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reviewEndTime;

    @ApiModelProperty(value = "评审标准")
    private String reviewStandard;

    @ApiModelProperty(value = "评审组长")
    private Integer reviewLeader;

    @ApiModelProperty(value = "评审成员（多个）")
    private String reviewMember;

    @ApiModelProperty(value = "附件批次")
    private String mainBatchId;

    @ApiModelProperty(value = "人员对象")
    @TableField(exist = false)
    private List<SimpleUser> ownerUserList;

    //Copy 审批
    @TableField(exist = false)
    WfVO wfVO;
}
