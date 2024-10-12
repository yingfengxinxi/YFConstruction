package com.qdoner.datarecept.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 塔机配置表
 * </p>
 *
 * @author shz
 * @since 2021-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_tower_crane")
@ApiModel(value="EquipmentTowerCrane对象", description="塔机配置表")
public class EquipmentTowerCrane implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备id")
    private Integer deviceId;

    @ApiModelProperty(value = "设备状态")
    private Integer deviceStatus;

    @ApiModelProperty(value = "制造许可编号")
    private String manufacturingLicense;

    @ApiModelProperty(value = "规格型号")
    private String model;

    @ApiModelProperty(value = "设备备案编号")
    private String recordNumber;

    @ApiModelProperty(value = "制造厂家")
    private String manufacturer;

    @ApiModelProperty(value = "设备产权单位")
    private String propertyUnit;

    @ApiModelProperty(value = "出厂日期（时间戳 数据精确到毫秒 没有传0）")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date manufactureDate;

    @ApiModelProperty(value = "出厂编号")
    private String manufacturingNo;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "安装单位")
    private String installationUnit;

    @ApiModelProperty(value = "检验合格日期（时间戳 数据精确到毫秒 没有传0）")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date verifyDate;

    @ApiModelProperty(value = "使用登记日期（时间戳 数据精确到毫秒 没有传0）")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date recordDate;

    @ApiModelProperty(value = "前向臂高0.00~99.99单位：m")
    private String foreArmLength;

    @ApiModelProperty(value = "后向臂高0.00~99.99单位：m")
    private String rearArmLength;

    @ApiModelProperty(value = "额定吊重（t）")
    private String maxLoad;

    @ApiModelProperty(value = "最大吊重0.00~99.99t")
    private String maxWeight;

    @ApiModelProperty(value = "额定力矩")
    private String ratedTorque;

    @ApiModelProperty(value = "额定风速0.00~36.90m/s")
    private String ratedWindSpeed;

    @ApiModelProperty(value = "设备编号")
    private String deviceNumber;

    @ApiModelProperty(value = "拆除单位")
    private String dismantleUnit;

    @ApiModelProperty(value = "塔吊设备类型：1-尖头塔，2-平头塔，3-动臂塔")
    private String customTowerCraneType;

    @ApiModelProperty(value = "监测机构")
    private String detectionUnit;

    @ApiModelProperty(value = "数据时间戳(示例精确到毫秒,没有传0)")
    private Integer dataTimeStamp;

    @ApiModelProperty(value = "SIM卡号")
    private String simCardNo;

    @ApiModelProperty(value = "硬件版本号")
    @TableField("hardwareVer")
    private String hardwareVer;

    @ApiModelProperty(value = "协议版本号")
    private String protocolVer;

    @ApiModelProperty(value = "塔机名称编号1~16")
    private String selfCraneNo;

    @ApiModelProperty(value = "塔机类型 0: 动臂吊 1: 塔头平臂吊 2: 平头平臂吊")
    private String craneType;

    @ApiModelProperty(value = "配置载重功能 0:未配置 1:已配置")
    private String weightSet;

    @ApiModelProperty(value = "配置风速功能0:未配置1:已配置")
    private String windSpeedSet;

    @ApiModelProperty(value = "配置幅度功能0:未配置1:已配置")
    private String rangeSet;

    @ApiModelProperty(value = "配置高度功能0:未配置1:已配置")
    private String heightSet;

    @ApiModelProperty(value = "配置角度功能0:未配置1:已配置")
    private String angleSet;

    @ApiModelProperty(value = "配置倾角功能0:未配置1:已配置")
    private String obliguitySet;

    @ApiModelProperty(value = "配置gps功能0:未配置1:已配置")
    private String gpsSet;

    @ApiModelProperty(value = "配置人员识别功能0:未配置1:已配置")
    private String idSet;

    @ApiModelProperty(value = "额定风级0~12级")
    private String ratedWindLevel;

    @ApiModelProperty(value = "最小变幅0.00~99.99m")
    private Float minRange;

    @ApiModelProperty(value = "最大变幅0.00~99.99m")
    private Float maxRange;

    @ApiModelProperty(value = "大臂高0.00~655.35m")
    private Float armHeight;

    @ApiModelProperty(value = "最大高度0.00~655.35m")
    private Float maxHeight;

    @ApiModelProperty(value = "最大角度540.0°")
    private Float maxAngle;

    @ApiModelProperty(value = "最小角度-540.0°")
    private Float minAngle;

    @ApiModelProperty(value = "额定倾角0.00~9.99°")
    private Float ratedObliguity;

    @ApiModelProperty(value = "最大幅度允许最大载重0.00~99.99t")
    @TableField("tc_MS_Load_capacity")
    private Float tcMsLoadCapacity;

    @ApiModelProperty(value = "最大载重达到最大幅度0.00~655.3m")
    @TableField("tc_ML_Max_scope")
    private Float tcMlMaxScope;

    @ApiModelProperty(value = "载重滤波等级 1~9")
    @TableField("differInput")
    private Float differInput;

    @ApiModelProperty(value = "高度报警阈值0.00~655.3m")
    private Float settingMinHeight;

    @ApiModelProperty(value = "载重零点值 0.00~99.99t")
    private Float weightZero;

    @ApiModelProperty(value = "风速零点值0.00~99.99m/s")
    private Float windSpeedZero;

    @ApiModelProperty(value = "角度零点值 0.0~359.9°")
    private Float angleZero;

    @ApiModelProperty(value = "倾角X零点值0.00~9.99°")
    @TableField("dipX_zero")
    private Float dipxZero;

    @ApiModelProperty(value = "倾角Y零点值0.00~9.99°")
    @TableField("dipY_zero")
    private Float dipyZero;

    @ApiModelProperty(value = "动臂吊仰角零点值0.00~99.99°")
    private Float elevationAngleZero;

    @ApiModelProperty(value = "设备序列号")
    @TableField("deviceSN")
    private String deviceSN;

    @ApiModelProperty(value = "经度")
    private String lng;

    @ApiModelProperty(value = "纬度")
    private String lat;

    private Integer tenantId;

    private Integer projectId;
}
