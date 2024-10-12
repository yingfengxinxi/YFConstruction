package com.kakarote.build.equipment.entity.BO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 萤石设备信息
 * </p>
 *
 * @author wnj
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("萤石设备信息查询类")
public class EquipmentFluoriteCloudBO extends PageEntity {

    @ApiModelProperty(value = "萤石设备ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "录像机id")
    private String deviceId;

    @ApiModelProperty(value = "通道id")
    private String deviceChannelId;

    @ApiModelProperty(value = "萤石云通道号")
    private String deviceChannelNo;

    @ApiModelProperty(value = "萤石云设备序列号")
    private String deviceSerial;

    @ApiModelProperty(value = "设备资产id")
    private String businessId;

    @ApiModelProperty(value = "类型：2使用萤石云地址")
    private String type;

    @ApiModelProperty(value = "通道状态（1：在线，0：离线）")
    private String status;

    @ApiModelProperty(value = "当前设备是否启用（1：启用，0：禁用）" +
            "(说明：启用会同步萤石云设备状态，禁用默认设备离线，如果是其他值，且设备在线状态为null，则更新设备在线状态为离线，否则忽略更新)")
    private String isEnable;

    @ApiModelProperty(value = "萤石云播放地址(ezopen://a.live)")
    private String url;

    @ApiModelProperty(value = "萤石云播放地址（http://a.flv）")
    private String ysyFlvUrl;

    @ApiModelProperty(value = "wvp视频播放地址(现在为萤石云播放地址：http://a.m3u8)")
    private String wvpOpenUrl;

    @ApiModelProperty(value = "是否吊钩：0否，1是")
    private String hookCheckBox;

    @ApiModelProperty(value = "是否云台控制：0否，1是")
    private String ptzCheckBox;

    @ApiModelProperty(value = "是否AI设备：0否，1是")
    private String isAiEquipment;

    @ApiModelProperty(value = "AI监测类型（字典项）")
    private String aiMonitorType;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "排序")
    private Date createTime;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "设备ID")
    @TableField(exist = false)
    private Integer assetId;

    @ApiModelProperty(value = "设备编号")
    @TableField(exist = false)
    private String assetCode;

    @ApiModelProperty(value = "设备名称")
    @TableField(exist = false)
    private String assetName;

}
