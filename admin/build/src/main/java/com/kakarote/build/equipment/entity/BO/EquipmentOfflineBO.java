package com.kakarote.build.equipment.entity.BO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("设备上线下记录查询类")
public class EquipmentOfflineBO  extends PageEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备id")
    private Integer equipmentId;

    @ApiModelProperty(value = "设备名称")
    private String equipmentName;

    private String equipmentCode;

    private String projectName;

    @ApiModelProperty(value = "状态1上线，2离线")
    private Integer status;

    @ApiModelProperty(value = "检测时间")
    private Date monitorTime;

    private Integer tenantId;

    private Integer projectId;

    @TableField(exist = false)
    private Integer typeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}
