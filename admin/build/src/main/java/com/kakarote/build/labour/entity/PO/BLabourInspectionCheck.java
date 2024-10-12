package com.kakarote.build.labour.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业报验检查表
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_inspection_check")
@ApiModel(value="BLabourInspectionCheck对象", description="企业报验检查表")
public class BLabourInspectionCheck implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateBy;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "项目")
    private String inspectionId;

    @ApiModelProperty(value = "检查人")
    private String checkPeople;

    @ApiModelProperty(value = "检查时间")
    private Date checkTime;

    @ApiModelProperty(value = "单位工程名称")
    private String projectName;

    @ApiModelProperty(value = "在施部位 在施部位 字典值 b_inspection_phase")
    private String constructionParts;

    @ApiModelProperty(value = "气象状况")
    private String meteorological;

    @ApiModelProperty(value = "工程评价 字典值 b_inspection_project_appraise")
    private String projectEvaluation;

    @ApiModelProperty(value = "安全主管到岗情况 字典值  b_inspection_duty_status")
    private String securityWorkInfo;

    @ApiModelProperty(value = "项目经理到岗情况 字典值  b_inspection_duty_status")
    private String managerWorkInfo;

    @ApiModelProperty(value = "总监到岗情况 字典值  b_inspection_duty_status")
    private String directorWorkInfo;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "批次Id")
    private String batchId;


}
