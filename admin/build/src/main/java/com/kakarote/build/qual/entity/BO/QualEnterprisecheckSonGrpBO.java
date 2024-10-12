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
 * 施工质量--企业质量检查设置--子表
 * </p>
 *
 * @author wnj
 * @since 2021-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="QualEnterprisecheckSonGrp对象", description="施工质量--企业质量检查设置--子表")
public class QualEnterprisecheckSonGrpBO extends PageEntity {

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

    @ApiModelProperty(value = "检查分类（1安全管理、2质量管理、3人员管理、4其他）")
    private String checkType;

    @ApiModelProperty(value = "主表id")
    private Long pid;

    @ApiModelProperty(value = "检查项")
    private String checkItems;

    @ApiModelProperty(value = "检查内容")
    private String checkContent;

    @ApiModelProperty(value = "评分标准")
    private String scoringCriteria;

    @ApiModelProperty(value = "满分分值")
    private Integer fullScore;


}
