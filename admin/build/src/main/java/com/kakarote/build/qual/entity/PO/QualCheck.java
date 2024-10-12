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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 施工质量--质量检查管理
 * </p>
 *
 * @author lzy
 * @since 2021-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_check")
@ApiModel(value="QualCheck对象", description="施工质量--质量检查管理")
public class QualCheck implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "check_id", type = IdType.AUTO)
    private Integer checkId;

    @ApiModelProperty(value = "检查类型 字典值")
    private String qualCheckType;

    @ApiModelProperty(value = "检查人员id")
    private String qualCheckPersonId;

    @ApiModelProperty(value = "检查人员姓名")
    private String qualCheckPersonName;

    @ApiModelProperty(value = "项目负责人id")
    private Integer qualPrincipalId;

    @ApiModelProperty(value = "项目负责人姓名")
    private String qualPrincipalName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "检查日期")
    private Date qualCheckDate;

    @ApiModelProperty(value = "联系电话")
    private String qualPhone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "整改期限")
    private Date qualRepairTimeLimit;

    @ApiModelProperty(value = "单据编号")
    private String qualDocumentNumber;

    @ApiModelProperty(value = "处理意见 1:停工 2:罚款 3:整改")
    private String qualOpinionOption;

    @ApiModelProperty(value = "检查单位")
    private String qualCheckCom;

    @ApiModelProperty(value = "形象进度 字典值")
    private String qualProgress;

    @ApiModelProperty(value = "检查计分")
    private String qualCheckScore;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "单据日期")
    private Date qualDocumentDate;

    @ApiModelProperty(value = "单据状态 0未回执 1已回执")
    private String qualReceiptStatus;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "检查人员对象")
    @TableField(exist = false)
    private List<SimpleUser> ownerUserList;
}
