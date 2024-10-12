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
 * @since 2022-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="桩基数字化监测", description="")
public class BQualPileFoundationBO extends PageEntity {

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

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "沉桩深度")
    private Double pileDepth;

    @ApiModelProperty(value = "电流")
    private Double current1;

    @ApiModelProperty(value = "混凝土灌入量")
    private Double infusion;

    @ApiModelProperty(value = "工作速度")
    private Double workSpeed;

    @ApiModelProperty(value = "拔管速度")
    private Double extubateSpeed;

    @ApiModelProperty(value = "左右倾斜")
    private Double leftrightTilt;

    @ApiModelProperty(value = "上下倾斜")
    private Double updownTilt;

    @ApiModelProperty(value = "桩编号")
    private String pileMachineId;

    @ApiModelProperty(value = "桩区间id")
    private String areaId;

    @ApiModelProperty(value = "混凝土灌入量")
    private Double recordInfusion;

    @ApiModelProperty(value = "打桩开始时间")
    private Date startTime;

    @ApiModelProperty(value = "打桩结束时间")
    private Date endTime;

    @ApiModelProperty(value = "总时长")
    private Integer totalTime;

    @ApiModelProperty(value = "成孔电流")
    private Double holeCurrent;

    @ApiModelProperty(value = "成孔时间")
    private Date holeTime;

    @ApiModelProperty(value = "成桩时间")
    private Date pileTime;

    @ApiModelProperty(value = "参考灌入量")
    private Double referInfusion;

    @ApiModelProperty(value = "成桩深度")
    private Double recordPileDepth;

    @ApiModelProperty(value = "充盈系数")
    private Double fillFactor;

    @ApiModelProperty(value = "经度")
    private String lng;

    private String lat;

    @ApiModelProperty(value = "X坐标")
    private String coordinateX;

    @ApiModelProperty(value = "Y坐标")
    private String coordinateY;

    @ApiModelProperty(value = "桩基监测状态   b_qual_markingroom_monitoring 0：监测中，1：监测完毕")
    private String isSupervise;

}
