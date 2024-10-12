package com.kakarote.build.safety.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 静态风险数据
 * </p>
 *
 * @author shz
 * @since 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_static_risk")
@ApiModel(value="SafetyStaticRisk对象", description="静态风险数据")
public class SafetyStaticRisk implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "风险点编号")
    private String riskNum;

    @ApiModelProperty(value = "风险点名称")
    private String riskName;

    @ApiModelProperty(value = "风险等级")
    private String riskLevel;

    @ApiModelProperty(value = "潜在事故类型")
    private String accidentType;

    @ApiModelProperty(value = "管控层级")
    private String controlLevel;

    @ApiModelProperty(value = "责任人")
    private String dutyPerson;

    @ApiModelProperty(value = "工程措施")
    private String engineeringMeasure;

    @ApiModelProperty(value = "管理措施")
    private String controlMeasures;

    @ApiModelProperty(value = "培训教育")
    private String trainContent;

    @ApiModelProperty(value = "个体防护措施")
    private String individualProtection;

    @ApiModelProperty(value = "应急措施")
    private String emergencyMeasures;

    @ApiModelProperty(value = "风险解除状态：0代表未解除，1代表已解除")
    private String relieveStatus;

    @ApiModelProperty(value = "检查内容")
    private String content;

    @ApiModelProperty(value = "风险点描述")
    private String riskDesc;

    @ApiModelProperty(value = "删除状态：0正常、1删除")
    private String isDelete;

    @ApiModelProperty(value = "设备设施类型")
    private String deviceType;

    @ApiModelProperty(value = "标准要求")
    private String standardRequire;

    @ApiModelProperty(value = "分部分项及专项工程 字典项")
    private String partEngineering;

    @ApiModelProperty(value = "发布时间（创建时间）")
    private Date publishDate;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "租户ID")
    private Integer tenantId;


}
