package com.kakarote.build.equipment.entity.BO;

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
 * 机械设备司机识别
 * </p>
 *
 * @author zy
 * @since 2021-07-26
 */
@Data
@ApiModel("机械设备司机识别")
public class EquipmentDriverIdentBO extends PageEntity {


    private Integer id;

    @ApiModelProperty(value = "设备编号")
    private String equCode;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "上岗时间")
    private Date startingDate;

    @ApiModelProperty(value = "证书id")
    private String certificateId;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;


}
