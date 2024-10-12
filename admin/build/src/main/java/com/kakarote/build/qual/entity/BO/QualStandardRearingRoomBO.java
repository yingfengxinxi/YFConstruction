package com.kakarote.build.qual.entity.BO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zjc
 * @since 2022-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("标养室监测数据查询对象")
public class QualStandardRearingRoomBO extends PageEntity {

    @ApiModelProperty(value = "设备名称")
    @TableField(exist = false)
    private String assetName;

    @ApiModelProperty(value = "设备编号")
    @TableField(exist = false)
    private String assetCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "报警类型 字典值 b_qual_standard_rearing_room")
    private String warnType;

}
