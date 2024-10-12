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
 * 监测点设置
 * </p>
 *
 * @author lzy
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("监测项历史数据查询对象")
public class EquipmentMonitorDataBO extends PageEntity {

    @ApiModelProperty(value = "监测项类型 字典项 1：基坑监测、2：塔机监测、3；升降机监测、9：环境监测")
    private String monitorType;

    @ApiModelProperty(value = "监测点名称")
    private String pointName;

    @ApiModelProperty(value = "监测点编号")
    private String pointCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "环境监测项子类型 1：扬尘类型(PM2.5、PM10) 2：噪音类型(噪音) 3：气象类型(温度、湿度、风力、风向、风速)")
    private String envMonitorSonType;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "报警状态 1：正常 2：预警 3：报警")
    private String alarmStatus;

    @ApiModelProperty(value = "监测点")
    private List<Integer> pointIds;

    @ApiModelProperty(value = "设备名称")
    private Integer equipmentId;

    @ApiModelProperty(value = "点位sql")
    private String pointsSql;

    @ApiModelProperty(value = "历史数据查询类型")
    private String dataType;

}
