package com.kakarote.build.qual.entity.BO;

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
 * 施工质量--分户验收--填报--主表
 * </p>
 *
 * @author wnj
 * @since 2021-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("施工质量--分户验收--填报--主表")
public class QualHouseAcceptFillMainBO extends PageEntity {


    @ApiModelProperty(value = "主键")
    private Integer acceptId;

    @ApiModelProperty(value = "楼座id")
    private byte[] buildingId;

    @ApiModelProperty(value = "房号id")
    private String houseId;

    @ApiModelProperty(value = "验收工序")
    private String acceptWork;

    @ApiModelProperty(value = "验收模板id")
    private Integer templateNameId;

    @ApiModelProperty(value = "验收时间")
    private Date acceptTime;

    @ApiModelProperty(value = "验收人")
    private String acceptBy;

    @ApiModelProperty(value = "验收结论")
    private String acceptConclusion;

    @ApiModelProperty(value = "是否通过")
    private String isAdopt;

    @ApiModelProperty(value = "定位--经度(APP专用)")
    private String locationLon;

    @ApiModelProperty(value = "定位--纬度(APP专用)")
    private String locationLat;

    @ApiModelProperty(value = "人员现场照片批次(APP专用)")
    private String batchId;

    private String building;

    private String house;

    private String template;

    private String floor;
}
