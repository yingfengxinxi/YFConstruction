package com.kakarote.build.ai.entity.BO;


import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("视屏警报查询对象")
public class BAiDregCarCleaningBO extends PageEntity {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "车牌号")
    private String licensePlateNumber;

    @ApiModelProperty(value = "车牌颜色 字典项")
    private String licensePlateColor;

    @ApiModelProperty(value = "车型 字典项 1：载重汽车；2：越野汽车；3：倾卸汽车；4：牵引车；5：特种车；6：客车；7：轿车；8挂车；9：半挂车、加长货挂车；")
    private String carType;

    private String carColor;

    @ApiModelProperty(value = "进场时间")
    private Date approachTime;

    @ApiModelProperty(value = "进场图片")
    private String approachImg;

    @ApiModelProperty(value = "出场时间")
    private Date appearanceTime;

    @ApiModelProperty(value = "出场图片")
    private String appearanceImg;

    @ApiModelProperty(value = "清洗时间")
    private Date cleaningTime;

    @ApiModelProperty(value = "清洗图片")
    private String cleaningImg;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
