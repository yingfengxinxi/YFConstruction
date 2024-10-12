package com.qdoner.datarecept.entity.PO;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 塔机设备实时数据表
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BEquipmentTowerDataAt对象", description="塔机设备实时数据表")
public class BEquipmentTowerDataAt implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "设备ID")
    private Integer equipmentId;

    @ApiModelProperty(value = "监测时间")
    private Date monitorTime;

    @ApiModelProperty(value = "荷载比(单位是%)")
    private String loadRatio;

    @ApiModelProperty(value = "幅度（单位是M）0.00~99.99")
    private String amplitude;

    @ApiModelProperty(value = "高度（单位是M）-100~655.35")
    private String height;

    @ApiModelProperty(value = "转角（单位是度）-540~540")
    private String rotation;

    @ApiModelProperty(value = "吊重（单位是t）0.00~99.99")
    private String weight;

    @ApiModelProperty(value = "风速（单位是m/s）0.00~36.90")
    private String windspeed;

    @ApiModelProperty(value = "协议版本号")
    private String protocolVer;

    @ApiModelProperty(value = "设备序列号")
    @TableField("device_SN")
    private String deviceSn;

    @ApiModelProperty(value = "黑匣子TCP实际IP")
    @TableField("device_TCPIP")
    private String deviceTcpip;

    @ApiModelProperty(value = "黑匣子TCP实际端口")
    @TableField("device_TCP_Poot")
    private String deviceTcpPoot;

    @ApiModelProperty(value = "塔机工作状态0:运行监控1:顶升监控")
    private String workStatus;

    @ApiModelProperty(value = "系统运行状态 0:无电机处于工作状态 1:有电机处于工作状态")
    private String motorStatus;

    @ApiModelProperty(value = "倍率1~99")
    private String multiple;

    @ApiModelProperty(value = "力矩百分比 0.00~655.35%")
    private Float moment;

    @ApiModelProperty(value = "倾角-9.99~9.99°")
    private Float obliguity;

    @ApiModelProperty(value = "额定载重 0.00~99.99t")
    private Float ratedWeight;

    @ApiModelProperty(value = "风级 0~12级")
    private String windLevel;

    @ApiModelProperty(value = "无任何外设故障 0:有外设故障 1:无任何外设故障")
    private String noError;

    @ApiModelProperty(value = "载重传感器故障 0:无故障 1:有故障")
    private String weightError;

    @ApiModelProperty(value = "风速传感器故障 0:无故障 1:有故障")
    private String windSpeedError;

    @ApiModelProperty(value = "幅度传感器故障 0:无故障 1:有故障")
    private String rangeError;

    @ApiModelProperty(value = "高度传感器故障 0:无故障 1:有故障")
    private String heightError;

    @ApiModelProperty(value = "角度传感器故障 0:无故障 1:有故障")
    private String angleError;

    @ApiModelProperty(value = "倾角传感器故障 0:无故障 1:有故障")
    private String obliguityError;

    @ApiModelProperty(value = "gps故障 0:无故障 1:有故障")
    private String gpsError;

    @ApiModelProperty(value = "身份识别模块故障 0:无故障 1:有故障")
    private String idError;

    @ApiModelProperty(value = "无任何报警 0:有报警 1:无任何报警")
    private String noAlarm;

    @ApiModelProperty(value = "力矩报警 0:正常 1:报警 2:预警")
    private String momentAlarm;

    @ApiModelProperty(value = "风速报警 0:正常 1:报警 2:预警")
    @TableField("wind_speed_Alarm")
    private String windSpeedAlarm;

    @ApiModelProperty(value = "高度上限位报警 0:正常 1:报警 2:预警")
    private String heightAlarm;

    @ApiModelProperty(value = "幅度内限位报警 0:正常 1:报警 2:预警")
    private String minRangeAlarm;

    @ApiModelProperty(value = "幅度外限位报警 0:正常 1:报警 2:预警")
    private String maxRangeAlarm;

    @ApiModelProperty(value = "顺时针回转限位报警 0:正常 1:报警 2:预警")
    private String posAngleAlarm;

    @ApiModelProperty(value = "逆时针回转限位报警 0:正常 1:报警 2:预警")
    private String negAngleAlarm;

    @ApiModelProperty(value = "倾角报警 0:正常 1:报警 2:预警")
    private String obliguityAlarm;

    @ApiModelProperty(value = "禁入区报警 0: 正常 1:报警")
    private String forbidEntryAlarm;

    @ApiModelProperty(value = "A类禁吊区域报警0: 正常 1~5:1~5区域报警")
    private String forbidSuspend2Alarm;

    @ApiModelProperty(value = "B类禁吊区域报警 0: 正常 1~5:1~5区域报警")
    private String forbidSuspend4Alarm;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "防碰撞左转预警 0:正常 1:报警 2:预警")
    private String antiCollisionLeftTurnAlarm;

    @ApiModelProperty(value = "防碰撞右转预警 0:正常 1:报警 2:预警")
    private String antiCollisionRightTurnAlarm;

    @ApiModelProperty(value = "防碰撞小车预警 0:正常 1:报警 2:预警")
    private String collisionTrolleyAlarm;

    @ApiModelProperty(value = "防碰撞小车预警 0:正常 1:报警 2:预警")
    private String collisionTrolleyEntryAlarm;

    @ApiModelProperty(value = "左转环境预警 0:正常 1:报警 2:预警")
    private String leftTurnEnvironmentalAlarm;

    @ApiModelProperty(value = "右转环境预警 0:正常 1:报警 2:预警")
    private String rightTurnEnvironmentalAlarm;

    @ApiModelProperty(value = "小车出环境预警 0:正常 1:报警 2:预警")
    private String trolleyExitEnvironmentAlarm;

    @ApiModelProperty(value = "小车进环境预警 0:正常 1:报警 2:预警")
    private String trolleyEntryEnvironmentAlarm;

    @ApiModelProperty(value = "最大幅度限位预警 0:正常 1:报警 2:预警")
    private String maximumAmplitudeLimitAlarm;

    @ApiModelProperty(value = "最小幅度限位预警 0:正常 1:报警 2:预警")
    private String minimumAmplitudeLimitAlarm;

    @ApiModelProperty(value = "高度上限位预警 0:正常 1:报警 2:预警")
    private String altitudeUpperLimitAlarm;

    @ApiModelProperty(value = "高度下限位预警 0:正常 1:报警 2:预警")
    private String altitudeLowerLimitAlarm;

    @ApiModelProperty(value = "回转左限位预警 0:正常 1:报警 2:预警")
    private String slewingLeftLimitAlarm;

    @ApiModelProperty(value = "回转右限位预警 0:正常 1:报警 2:预警")
    private String slewingRightLimitAlarm;

    @ApiModelProperty(value = "碰撞预警 0:正常 1:报警 2:预警")
    private String collisionAlarm;

    @ApiModelProperty(value = "防碰撞停车角 0:正常 1:报警 2:预警")
    private String antiCollisionParkingAngleAlarm;

    @ApiModelProperty(value = "防碰撞刹车角 0:正常 1:报警 2:预警")
    private String antiCollisionBrakeAngleAlarm;

    @ApiModelProperty(value = "限行区 0:正常 1:报警 2:预警")
    private String restrictedAreaAlarm;
    @ApiModelProperty(value = "吊重报警")
    private String weightAlarm;

    @ApiModelProperty(value = "吊重百分比")
    private Float weightPercentage;

    @ApiModelProperty(value = "风速百分比")
    private Float windPercentage;

    @ApiModelProperty(value = "倾角百分比")
    private Float obliguityPercentage;

    @ApiModelProperty(value = "碰撞报警")
    private String towerCollisionAlarm;

    @ApiModelProperty(value = "制动状态")
    private String brakingStatus;

    @Override
    public String toString() {
        return "BEquipmentTowerDataAt{" +
                "equipmentId=" + equipmentId +
                //", monitorTime=" + monitorTime +
                ", loadRatio='" + loadRatio + '\'' +
                ", amplitude='" + amplitude + '\'' +
                ", height='" + height + '\'' +
                ", rotation='" + rotation + '\'' +
                ", weight='" + weight + '\'' +
                ", windspeed='" + windspeed + '\'' +
                ", protocolVer='" + protocolVer + '\'' +
                ", deviceSn='" + deviceSn + '\'' +
                ", deviceTcpip='" + deviceTcpip + '\'' +
                ", deviceTcpPoot='" + deviceTcpPoot + '\'' +
                ", workStatus='" + workStatus + '\'' +
                ", motorStatus='" + motorStatus + '\'' +
                ", multiple='" + multiple + '\'' +
                ", moment=" + moment +
                ", obliguity=" + obliguity +
                ", ratedWeight=" + ratedWeight +
                ", windLevel='" + windLevel + '\'' +
                ", noError='" + noError + '\'' +
                ", weightError='" + weightError + '\'' +
                ", windSpeedError='" + windSpeedError + '\'' +
                ", rangeError='" + rangeError + '\'' +
                ", heightError='" + heightError + '\'' +
                ", angleError='" + angleError + '\'' +
                ", obliguityError='" + obliguityError + '\'' +
                ", gpsError='" + gpsError + '\'' +
                ", idError='" + idError + '\'' +
                ", noAlarm='" + noAlarm + '\'' +
                ", momentAlarm='" + momentAlarm + '\'' +
                ", windSpeedAlarm='" + windSpeedAlarm + '\'' +
                ", heightAlarm='" + heightAlarm + '\'' +
                ", minRangeAlarm='" + minRangeAlarm + '\'' +
                ", maxRangeAlarm='" + maxRangeAlarm + '\'' +
                ", posAngleAlarm='" + posAngleAlarm + '\'' +
                ", negAngleAlarm='" + negAngleAlarm + '\'' +
                ", obliguityAlarm='" + obliguityAlarm + '\'' +
                ", forbidEntryAlarm='" + forbidEntryAlarm + '\'' +
                ", forbidSuspend2Alarm='" + forbidSuspend2Alarm + '\'' +
                ", forbidSuspend4Alarm='" + forbidSuspend4Alarm + '\'' +
                ", tenantId=" + tenantId +
                ", projectId=" + projectId +
                ", antiCollisionLeftTurnAlarm='" + antiCollisionLeftTurnAlarm + '\'' +
                ", antiCollisionRightTurnAlarm='" + antiCollisionRightTurnAlarm + '\'' +
                ", collisionTrolleyAlarm='" + collisionTrolleyAlarm + '\'' +
                ", collisionTrolleyEntryAlarm='" + collisionTrolleyEntryAlarm + '\'' +
                ", leftTurnEnvironmentalAlarm='" + leftTurnEnvironmentalAlarm + '\'' +
                ", rightTurnEnvironmentalAlarm='" + rightTurnEnvironmentalAlarm + '\'' +
                ", trolleyExitEnvironmentAlarm='" + trolleyExitEnvironmentAlarm + '\'' +
                ", trolleyEntryEnvironmentAlarm='" + trolleyEntryEnvironmentAlarm + '\'' +
                ", maximumAmplitudeLimitAlarm='" + maximumAmplitudeLimitAlarm + '\'' +
                ", minimumAmplitudeLimitAlarm='" + minimumAmplitudeLimitAlarm + '\'' +
                ", altitudeUpperLimitAlarm='" + altitudeUpperLimitAlarm + '\'' +
                ", altitudeLowerLimitAlarm='" + altitudeLowerLimitAlarm + '\'' +
                ", slewingLeftLimitAlarm='" + slewingLeftLimitAlarm + '\'' +
                ", slewingRightLimitAlarm='" + slewingRightLimitAlarm + '\'' +
                ", collisionAlarm='" + collisionAlarm + '\'' +
                ", antiCollisionParkingAngleAlarm='" + antiCollisionParkingAngleAlarm + '\'' +
                ", antiCollisionBrakeAngleAlarm='" + antiCollisionBrakeAngleAlarm + '\'' +
                ", restrictedAreaAlarm='" + restrictedAreaAlarm + '\'' +
                '}';
    }
}
