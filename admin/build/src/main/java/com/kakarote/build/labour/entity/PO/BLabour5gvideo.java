package com.kakarote.build.labour.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 5G超高清全景视频上传
 * </p>
 *
 * @author author
 * @since 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_5gvideo")
@ApiModel(value="BLabour5gvideo对象", description="5G超高清全景视频上传")
public class BLabour5gvideo implements Serializable {

    private static final long serialVersionUID=1L;

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

    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "设备状态 1：在线；0：离线")
    private String deviceStatus;

    @ApiModelProperty(value = "全景视频播放组件URL, 可以iframe集成播放")
    private String url;

    @ApiModelProperty(value = "视频类型 m3u8web")
    private String videoType;

    @ApiModelProperty(value = "调用全景视频播放组件时的token")
    private String token;

    @ApiModelProperty(value = "备注")
    private String remarks;


}