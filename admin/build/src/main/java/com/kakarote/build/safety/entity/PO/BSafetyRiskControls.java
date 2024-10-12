package com.kakarote.build.safety.entity.PO;

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
 * 安全风险分级管控
 * </p>
 *
 * @author author
 * @since 2021-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_risk_controls")
@ApiModel(value="BSafetyRiskControls对象", description="安全风险分级管控")
public class BSafetyRiskControls implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "风险名称")
    private String riskName;

    @ApiModelProperty(value = "风险类型：0 动态风险  1静态风险")
    private String riskCategory;

    @ApiModelProperty(value = "工作内容（静态风险：检查内容）（动态风险：工作内容—活动作业内容）")
    private String content;

    @ApiModelProperty(value = "标准要求（静态独有：标准要求）（动态独有：风险点描述）")
    private String standardRequire;

    @ApiModelProperty(value = "事故类型 b_safety_accidentType")
    private String accidentType;

    @ApiModelProperty(value = "风险风级管控模板id")
    private Integer templateId;

    @ApiModelProperty(value = "管控等级，字典项：风险评定等级b_safety_riskRating")
    private String riskLevel;

    @ApiModelProperty(value = "责任人")
    private String dutyPerson;

    @ApiModelProperty(value = "发布时间")
    private Date publishDate;

    @ApiModelProperty(value = "风险解除状态：0代表未解除，1代表已解除")
    private String relieveStatus;

    @ApiModelProperty(value = "删除状态：0正常、1删除")
    private String isDelete;

    @ApiModelProperty(value = "工程措施")
    private String engineeringMeasure;

    @ApiModelProperty(value = "个体防护措施")
    private String individualProtection;

    @ApiModelProperty(value = "设备设施名称(静态风险)")
    private String deviceType;


}
