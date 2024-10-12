package com.kakarote.build.equipment.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 监测点设置
 * </p>
 *
 * @author lzy
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("监测点设置查询对象")
public class EquipmentMonitorPointBO extends PageEntity {

    @ApiModelProperty(value = "监测项类型 字典项 1：基坑监测、2：塔机监测、3；升降机监测、9：环境监测")
    private String monitorType;

    @ApiModelProperty(value = "监测点名称")
    private String pointName;

    private Integer projectId;

    @ApiModelProperty(value = "监测项子类型 字典项")
    private String monitorSonType;
}
