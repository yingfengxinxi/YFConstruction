package com.kakarote.build.equipment.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 信用良好行为
 * </p>
 *
 * @author author
 * @since 2021-11-25
 */
@Data
@ApiModel(value = "智慧安全帽定位数据")
public class EquipmentHelmetGpsBO extends PageEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "定位时间-查询开始时间")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "定位时间-查询结束时间")
    private Date endTime;

    @ApiModelProperty(value = "智慧安全帽Id")
    private Integer helmetId;

}
