package com.kakarote.build.equipment.entity.BO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 *
 * @author lzy
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("设备资产查询对象")
public class EquipmentAssetsBO extends PageEntity {

    @ApiModelProperty(value = "设备类型")
    private Integer typeId;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    private Integer locationId;

    @ApiModelProperty(value = "资产编号/名称")
    private String search;

    private String status;
    /**
     * 设备状态查询使用
     */
    private String online;
    @ApiModelProperty(value = "监测类型")
    private String monitorType;
    @ApiModelProperty(value = "监测类型")
    private List<String> monitorTypeList;

    @ApiModelProperty(value = "判断设备是否为吊钩：0否，1是")
    private String hookCheckBox;


    /**
     * 设备状态查询使用
     */
    @ApiModelProperty(value = "1 环境设备 2机械设备 3视频设备")
    private String selectType;
}

