package com.kakarote.build.equipment.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 升降电梯配置信息
 * </p>
 *
 * @author wudw
 * @since 2021-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BEquipmentElevator对象", description="升降电梯配置信息")
public class BEquipmentElevator implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备id")
    private Integer equipmentId;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "制造许可编号")
    private String manufacturingLicense;

    @ApiModelProperty(value = "设备备案编号")
    private String recordNumber;

    @ApiModelProperty(value = "制造厂家")
    private String manufacturer;

    @ApiModelProperty(value = "设备产权单位")
    private String propertyUnit;

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

    @ApiModelProperty(value = "前向臂高")
    @TableField("foreArm_length")
    private String forearmLength;

    @ApiModelProperty(value = "后向臂高")
    @TableField("rearArm_length")
    private String reararmLength;

    @ApiModelProperty(value = "大臂高度（m）")
    private Float armHeight;

    @ApiModelProperty(value = "额定吊重（t）")
    private Float maxLoad;

    @ApiModelProperty(value = "额定力矩")
    private Float ratedTorque;

    @ApiModelProperty(value = "设备编号")
    private String deviceNumber;

    @ApiModelProperty(value = "拆除单位")
    private String dismantleUnit;

    @ApiModelProperty(value = "额定载重")
    private Double ratedLoad;

    @ApiModelProperty(value = "监测机构")
    private String detectionUnit;

    @ApiModelProperty(value = "额定人数")
    private Integer ratedPerson;

    @ApiModelProperty(value = "产权单位")
    private String rentCompany;

    @ApiModelProperty(value = "产权编号")
    private String titleNumber;

    @ApiModelProperty(value = "检测日期（时间戳 数据精确到毫秒 没有传0）")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date detectionQualifiedDate;

    @ApiModelProperty(value = "安装单位代码")
    private String installCompanyCode;

    @ApiModelProperty(value = "安装照片")
    private String installationPhoto;

    @ApiModelProperty(value = "检测编号")
    private String testNumber;

    @ApiModelProperty(value = "检测证书编号")
    private String checkCertificateNo;

    @ApiModelProperty(value = "检测证书照片")
    private String inspectionCertificatePic;

    @ApiModelProperty(value = "使用登记编号")
    private String useRegistrationNumber;

    @ApiModelProperty(value = "登记证书照片")
    private String registerCertificatePic;

    @ApiModelProperty(value = "设备自编号(检到位)")
    private String projectDeviceNo;

    @ApiModelProperty(value = "重量空载值")
    private String noLoad;

    @ApiModelProperty(value = "重量空载实际值")
    private String noLoadActual;

    @ApiModelProperty(value = "重量负载值")
    private String loadValue;

    @ApiModelProperty(value = "重量赋值实际值")
    private String loadValueActual;

    @ApiModelProperty(value = "高度低端值")
    private String lowEnd;

    @ApiModelProperty(value = "高度低端实际值")
    private String lowEndActual;

    @ApiModelProperty(value = "高度低端值")
    private String topEnd;

    @ApiModelProperty(value = "高度低端实际值")
    private String topEndActual;

    @ApiModelProperty(value = "高度预警值")
    private String heightWarningValue;

    @ApiModelProperty(value = "高度报警值")
    private String heightAlarmValue;

    @ApiModelProperty(value = "载重预警值")
    private String weightWarningValue;

    @ApiModelProperty(value = "载重报警值")
    private String weightAlarmValue;

    @ApiModelProperty(value = "速度预警值")
    private String speedWarningValue;

    @ApiModelProperty(value = "速度报警值")
    private String speedAlarmValue;
}
