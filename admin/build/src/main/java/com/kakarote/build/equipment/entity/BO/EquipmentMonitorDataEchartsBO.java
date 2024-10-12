package com.kakarote.build.equipment.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 监测数据-echarts图使用
 * </p>
 *
 * @author lzy
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("监测项历史数据查询对象")
public class EquipmentMonitorDataEchartsBO extends PageEntity {

    @ApiModelProperty(value = "监测项类型 字典项 1：基坑监测、2：塔机监测、3；升降机监测、9：环境监测")
    private String monitorType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "监测点")
    private List<Integer> pointIds;

    @ApiModelProperty(value = "最小值")
    private Integer minValue;

    @ApiModelProperty(value = "最大值")
    private Integer maxValue;
}
