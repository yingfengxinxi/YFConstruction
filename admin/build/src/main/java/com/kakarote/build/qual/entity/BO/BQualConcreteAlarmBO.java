package com.kakarote.build.qual.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zjc
 * @since 2022-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="大体积混凝土告警数据", description="")
public class BQualConcreteAlarmBO extends PageEntity {

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

    @ApiModelProperty(value = "测温孔ID   b_qual_concrete_point 表主键")
    private String nickId;

    @ApiModelProperty(value = "电池电量")
    private Double batteryPower;

    @ApiModelProperty(value = "测点温度")
    private Double pointTemp;

    @ApiModelProperty(value = "告警项  b_qual_concrete_alarm    表里温差告警 降温速率告警")
    private String alarmRuleType;

    @ApiModelProperty(value = "告警描述")
    private String alarmRuleTypeDesc;

    @ApiModelProperty(value = "告警类型 b_qual_concrete_alarm_type  1：预警，2：报警")
    private String warningType;

    @ApiModelProperty(value = "告警阈值")
    private Double alertValue;

    @ApiModelProperty(value = "开始时间")
    private Date eventTime;

    @ApiModelProperty(value = "结束时间")
    private Date recoverTime;

    @ApiModelProperty(value = "开始告警温度")
    private Double tempStartValue;

    @ApiModelProperty(value = "结束告警温度")
    private Double tempEndValue;

    @ApiModelProperty(value = "表层温度")
    private Double fromPositionTempValue;

    @ApiModelProperty(value = "底层温度")
    private Double toPositionTempValue;

}
