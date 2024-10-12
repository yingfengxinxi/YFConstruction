package com.kakarote.build.equipment.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
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
 * 设备信息
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_assets")
@ApiModel(value="EquipmentAssets对象", description="设备信息")
public class EquipmentAssets implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "设备ID")
    @TableId(value = "asset_id", type = IdType.AUTO)
    private Integer assetId;

    @ApiModelProperty(value = "设备编号")
    private String assetCode;

    @ApiModelProperty(value = "设备名称")
    private String assetName;

    @ApiModelProperty(value = "设备位置")
    private Integer locationId;

    @ApiModelProperty(value = "设备类型")
    private Integer typeId;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "规格")
    private String specification;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "状态 字典项1：正常 0：报废")
    private String status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "重要程度 字典项")
    private String level;

    @ApiModelProperty(value = "负责班组")
    private Integer deptId;

    @ApiModelProperty(value = "生产厂家")
    private String factory;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "出厂日期")
    private Date productionDate;

    @ApiModelProperty(value = "供货商")
    private String supplier;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "供货日期")
    private Date supplyDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "验收日期")
    private Date acceptanceDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "安装日期")
    private Date installDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "启动日期")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "报废预期")
    private Date scrapPre;

    @ApiModelProperty(value = "保修期限")
    private Integer serviceTerm;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "报废日期")
    private Date scrapDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "保修截止日期")
    private Date serviceDate;

    @ApiModelProperty(value = "设备原值")
    private Integer original;

    @ApiModelProperty(value = "评估价值")
    private Integer originalPre;

    @ApiModelProperty(value = "设备所属单位")
    private String assetBelong;

    @ApiModelProperty(value = "设备照片")
    private String assetPic;

    @ApiModelProperty(value = "备注说明")
    private String remarks;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    private Long updateUserId;

    private Integer tenantId;

    private Integer projectId;

    private String batchId;

    @TableField(exist = false)
    @ApiModelProperty(value = "位置名称")
    private String locationName;

    @TableField(exist = false)
    @ApiModelProperty(value = "类型名称")
    private String typeName;

    @TableField(exist = false)
    @ApiModelProperty(value = "负责部门")
    private String deptName;

    @TableField(exist = false)
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "对接平台")
    private Integer platform;

    @ApiModelProperty(value = "平台设备标识")
    private String platformEqCode;

    @ApiModelProperty(value = "数据解析器")
    private String dataResolver;
    
    @TableField(exist = false)
    @ApiModelProperty(value = "设备状态：0离线，1在线")
    private String online;

    @ApiModelProperty(value = "相对位置")
    private String planeCoordinate;

    @ApiModelProperty(value = "相对位置pc")
    private String planeCoordinatePc;

    @TableField(exist = false)
    private String monitorType;

    @TableField(exist = false)
    private String deviceChannelId;
    @TableField(exist = false)
    private String url;
    @TableField(exist = false)
    private String ysyFlvUrl;
    @TableField(exist = false)
    private String wvpOpenUrl;
    @TableField(exist = false)
    private String hookCheckBox;
    @TableField(exist = false)
    private String ptzCheckBox;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "上下线时间")
    private Date offlineDate;

    @ApiModelProperty(value = "状态:1在线，0离线")
    private String offlineStatus;

    @TableField(exist = false)
    @ApiModelProperty(value = "最后更新时间")
    private String lastUpdateTime;

    @ApiModelProperty(value = "开启状态:1开启，0关闭")
    private String openStatus;
}
