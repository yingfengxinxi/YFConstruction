package com.qdoner.datarecept.entity.PO;

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
 * 升降电梯设备实时数据表
 * </p>
 *
 * @author wudw
 * @since 2022-07-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BEquipmentElevatorDataAt对象", description="升降电梯设备实时数据表")
public class BEquipmentElevatorDataAt implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备ID")
    private Integer equipmentId;

    @ApiModelProperty(value = "监测时间")
    private Date monitorTime;

    @ApiModelProperty(value = "前门锁状态 0 关闭  1 开启")
    private String beforeLock;

    @ApiModelProperty(value = "后门锁状态0 关闭  1 开启")
    private String afterLock;

    @ApiModelProperty(value = "X倾角比(单位是%)")
    private String xAngleRatio;

    @ApiModelProperty(value = "Y倾角比（单位是%）")
    private String yAngleRatio;

    @ApiModelProperty(value = "X倾角（单位是度）")
    private String xAngle;

    @ApiModelProperty(value = "Y倾角（单位是度）")
    private String yAngle;

    @ApiModelProperty(value = "行程高度（单位是M）")
    private Double height;

    @ApiModelProperty(value = "速度（单位是m/s）")
    private String speed;

    @ApiModelProperty(value = "吊重（单位是t）")
    private String weight;

    @ApiModelProperty(value = "上限位")
    private String upLimit;

    @ApiModelProperty(value = "下限位")
    private String downLimit;

    @ApiModelProperty(value = "本次载重百分比")
    private Double weightPercent;

    @ApiModelProperty(value = "起点高度")
    private Double startHeight;

    @ApiModelProperty(value = "终点高度")
    private Double endHeight;

    @ApiModelProperty(value = "平均速度")
    private Double avgSpeed;

    @ApiModelProperty(value = "起升方向(1-静止，3-下 2-上) 字典 b_lift__run_status")
    private String liftingDirection;

    @ApiModelProperty(value = "是否违章（0-正常 1-身份未认证 2-重量违章）")
    private String warningState;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "载重预警 0:正常 1:报警 2:预警")
    private String loadAlarm;

    @ApiModelProperty(value = "人数预警 0:正常 1:报警 2:预警")
    private String numberAlarm;

    @ApiModelProperty(value = "风速预警 0:正常 1:报警 2:预警")
    private String windSpeedAlarm;

    @ApiModelProperty(value = "高度预警 0:正常 1:报警 2:预警")
    private String altitudeAlarm;

    @ApiModelProperty(value = "倾斜预警 0:正常 1:报警 2:预警")
    private String tiltAlarm;

    @ApiModelProperty(value = "冲顶预警 0:正常 1:报警 2:预警")
    private String roofFlushingAlarm;

    @ApiModelProperty(value = "上限位预警 0:正常 1:报警 2:预警")
    private String upperLimitAlarm;

    @ApiModelProperty(value = "下限位预警 0:正常 1:报警 2:预警")
    private String lowerLimitAlarm;

    @ApiModelProperty(value = "速度预警 0:正常 1:报警 2:预警")
    private String speedWarning;

    @ApiModelProperty(value = "人数")
    private Integer numberOfPeople;

    @ApiModelProperty(value = "风速")
    private Double wind;

    @ApiModelProperty(value = "吊重报警")
    private String weightAlarm;

    @ApiModelProperty(value = "前门锁预警 0:正常 1:报警 2:预警")
    private String bedoorWarn;

    @ApiModelProperty(value = "后门锁预警 0:正常 1:报警 2:预警")
    private String afdoorWarn;

    @Override
    public String toString() {
        return "BEquipmentElevatorDataAt{" +
                " equipmentId=" + equipmentId +
                ", beforeLock='" + beforeLock + '\'' +
                ", afterLock='" + afterLock + '\'' +
                ", xAngleRatio='" + xAngleRatio + '\'' +
                ", yAngleRatio='" + yAngleRatio + '\'' +
                ", xAngle='" + xAngle + '\'' +
                ", yAngle='" + yAngle + '\'' +
                ", height=" + height +
                ", speed='" + speed + '\'' +
                ", weight='" + weight + '\'' +
                ", upLimit='" + upLimit + '\'' +
                ", downLimit='" + downLimit + '\'' +
                ", weightPercent=" + weightPercent +
                ", startHeight=" + startHeight +
                ", endHeight=" + endHeight +
                ", avgSpeed=" + avgSpeed +
                ", liftingDirection='" + liftingDirection + '\'' +
                ", warningState='" + warningState + '\'' +
                ", tenantId=" + tenantId +
                ", projectId=" + projectId +
                ", loadAlarm='" + loadAlarm + '\'' +
                ", numberAlarm='" + numberAlarm + '\'' +
                ", windSpeedAlarm='" + windSpeedAlarm + '\'' +
                ", altitudeAlarm='" + altitudeAlarm + '\'' +
                ", tiltAlarm='" + tiltAlarm + '\'' +
                ", roofFlushingAlarm='" + roofFlushingAlarm + '\'' +
                ", upperLimitAlarm='" + upperLimitAlarm + '\'' +
                ", lowerLimitAlarm='" + lowerLimitAlarm + '\'' +
                ", speedWarning='" + speedWarning + '\'' +
                ", numberOfPeople=" + numberOfPeople +
                ", wind=" + wind +
                ", weightAlarm='" + weightAlarm + '\'' +
                ", bedoorWarn='" + bedoorWarn + '\'' +
                ", afdoorWarn='" + afdoorWarn + '\'' +
                '}';
    }
}