package com.kakarote.build.qual.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 施工质量--创优评优申报--子表
 * </p>
 *
 * @author wnj
 * @since 2021-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_appraisingdeclare_son_grp")
@ApiModel(value="QualAppraisingdeclareSonGrp对象", description="施工质量--创优评优申报--子表")
public class QualAppraisingdeclareSonGrp implements Serializable {

    private static final long serialVersionUID=1L;

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

    @ApiModelProperty(value = "主表id")
    private Integer pid;

    @ApiModelProperty(value = "申报项目")
    private Integer declareProject;

    @ApiModelProperty(value = "项目经理")
    private String projectManager;

    @ApiModelProperty(value = "建筑规模")
    @TableField("Architecture_scale")
    private String architectureScale;

    @ApiModelProperty(value = "评审材料（附件批次）")
    private String batchId;

    @ApiModelProperty(value = "总计评分")
    private Integer totalScore;

    @ApiModelProperty(value = "检查结论")
    private String inspectionConclusion;

    @ApiModelProperty(value = "是否通过")
    private String whetherAdopt;


}
