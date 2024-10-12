package com.kakarote.build.labour.entity.PO;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 特殊工种--证件管理（分包单位--特殊工种--证件台账）
 * </p>
 *
 * @author zy
 * @since 2021-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_speworktype")
@ApiModel(value="LabourSpeworktype对象", description="特殊工种--证件管理（分包单位--特殊工种--证件台账）")
public class LabourSpeworktype implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "采集信息主键")
    private Integer infocolId;

    @ApiModelProperty(value = "工种信息主键")
    private String workTypeId;

    @ApiModelProperty(value = "分包单位Id")
    private Integer subcontractor;

    @ApiModelProperty(value = "工龄")
    private Integer workAge;

//    @ApiModelProperty(value = "隶属人员名称")
//    private String name;
//
//    @ApiModelProperty(value = "身份证号")
//    private String idCard;

    @ApiModelProperty(value = "特工证信息--证书名称")
    private String cerName;

    @ApiModelProperty(value = "特工证信息--证件种类")
    private String cerCategory;

    @ApiModelProperty(value = "特工证信息--证书类型")
    private String cerCategoryType;

    @ApiModelProperty(value = "特工证信息--证书编号")
    private String cerNumber;

    @ApiModelProperty(value = "特工证信息--证书等级")
    private String cerLevel;

    @ApiModelProperty(value = "特工证信息--认证时间")
    private String authTime;

    @ApiModelProperty(value = "特工证信息--认证状态")
    private String authStatus;

    @ApiModelProperty(value = "岗位类型")
    private String postType;

    @ApiModelProperty(value = "岗位名称")
    private String positionTitle;

    @ApiModelProperty(value = "特工证信息--初始领证日期")
    private String cerStartDate;

    @ApiModelProperty(value = "特工证信息--归档日期")
    private String cerArchiveDate;

    @ApiModelProperty(value = "特工证使用日期--开始日期")
    private String cerUsestartDate;

    @ApiModelProperty(value = "特工证使用日期--结束日期")
    private String cerUseendDate;

    @ApiModelProperty(value = "特工证复核日期")
    private String cerReviewDate;

    @ApiModelProperty(value = "特工证网络检验日期")
    private String cerNetspeDate;

    @ApiModelProperty(value = "发证机关")
    private String cerIssuingAuthority;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "关键岗位证照批次id")
    private String batchId;

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
}
