package com.kakarote.build.equipment.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
* @Description:升降机数据查询类
* @author shz
* @date 2022-4-14 9:54
*/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("升降机数据查询类")
public class EquipmentElevatorDataAtBO  extends PageEntity {

    @ApiModelProperty(value = "升降机id")
    private Integer assetId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "设备名称")
    private String equipmentName;

    private Integer tenantId;

    private Integer projectId;

    private String projectName;

    private String alertType;
}
