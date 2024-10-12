package com.kakarote.build.equipment.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
* @Description:基坑/高支模数据查询类
* @author shz
* @date 2022-4-15 10:10
*/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("基坑/高支模数据查询类")
public class EquipmentPitDataAtBakBO  extends PageEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "设备名称")
    private String equipmentName;

    @ApiModelProperty(value = "监测点名称")
    private String pointName;

    @ApiModelProperty(value = "监测点")
    private Integer pointId;

    @ApiModelProperty(value = "监测点id组")
    private List<Integer> pointIds;

    @ApiModelProperty(value = "监测点类型")
    private String monitorType;

    @ApiModelProperty(value = "报警状态")
    private String alarmStatus;

    @ApiModelProperty(value = "监测点编号")
    private String pointCode;
}
