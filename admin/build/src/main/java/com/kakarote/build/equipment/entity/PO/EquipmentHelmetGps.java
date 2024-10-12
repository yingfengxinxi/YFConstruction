package com.kakarote.build.equipment.entity.PO;

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
 * 智慧安全帽-定位数据
 * </p>
 *
 * @author lzy
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_helmet_gps")
@ApiModel(value="EquipmentHelmetGps对象", description="智慧安全帽-定位数据")
public class EquipmentHelmetGps implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "智能安全帽id")
    private Integer helmetId;

    @ApiModelProperty(value = "定位时间")
    private Date uploadTime;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "x轴三维坐标")
    private Double xAxis;

    @ApiModelProperty(value = "y轴三维坐标")
    private Double yAxis;

    @ApiModelProperty(value = "z轴三维坐标")
    private Double zAxis;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "标签号码")
    private String tagNumber;

    @ApiModelProperty(value = "电池电量1-10 表示")
    private String batteryPower;

    @ApiModelProperty(value = "信号强度")
    private String signalIntensity;

    @ApiModelProperty(value = "设备类型编码")
    private String deviceType;


}
