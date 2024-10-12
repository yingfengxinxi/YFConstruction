package com.kakarote.build.gb28181.entity.PO;

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
 * @ClassName DeviceProjectRelation
 * @Author wnj58
 * @Date 2022/1/20
 * @purpose
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("device_project_relation")
@ApiModel(value = "DeviceProjectRelation对象", description = "录像设备与项目、设备台账、设备位置的关系表")
public class DeviceProjectRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "录像机id")
    private String deviceId;

    @ApiModelProperty(value = "通道id")
    private String deviceChannelId;

    @ApiModelProperty(value = "ipcLicense(IPC设备注册密码)")
    private String ipcLicense;

    @ApiModelProperty(value = "nvrLicense(NVR设备注册密码)")
    private String nvrLicense;

    @ApiModelProperty(value = "萤石云通道号")
    private String deviceChannelNo;

    @ApiModelProperty(value = "萤石云设备序列号")
    private String deviceSerial;

    @ApiModelProperty(value = "（设备台账）id',")
    private String businessId;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "通道状态（1：在线，0：离线）")
    private String status;

    @ApiModelProperty(value = "IPC状态变动")
    @TableField(exist = false)
    private Boolean statusChange = false;

    @ApiModelProperty(value = "NVR状态（1：在线，0：离线）")
    private String nvrStatus;

    @ApiModelProperty(value = "NVR状态变动")
    @TableField(exist = false)
    private Boolean nvrStatusChange = false;

    @ApiModelProperty(value = "当前设备是否启用（1：启用，0：禁用）")
    private String isEnable;

    @ApiModelProperty(value = "萤石云播放地址(ezopen://a.live)")
    private String url;

    @ApiModelProperty(value = "萤石云播放地址（http://a.flv）")
    private String ysyFlvUrl;

    @ApiModelProperty(value = "wvp视频播放地址")
    private String wvpOpenUrl;

    @ApiModelProperty(value = "url最近更新时间")
    private Date urlRetime;

    @ApiModelProperty(value = "是否吊钩：0否，1是")
    private String hookCheckBox;

    @ApiModelProperty(value = "是否云台控制：0否，1是")
    private String ptzCheckBox;

    @ApiModelProperty(value = "是否是AI设备：0否，1是")
    private String isAiEquipment;

    @ApiModelProperty(value = "AI监测类型（字典项）")
    private String aiMonitorType;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "租户ID")
    private Integer tenantId;

    @ApiModelProperty(value = "项目ID")
    private Integer projectId;

    //设备资产信息
    @TableField(exist = false)
    @ApiModelProperty(value = "设备资产id")
    private Integer assetId;

    @TableField(exist = false)
    @ApiModelProperty(value = "设备资产编码")
    private String assetCode;

    @TableField(exist = false)
    @ApiModelProperty(value = "设备资产名称")
    private String assetName;

    @TableField(exist = false)
    @ApiModelProperty(value = "设备资产名称")
    private String projectName;
}
