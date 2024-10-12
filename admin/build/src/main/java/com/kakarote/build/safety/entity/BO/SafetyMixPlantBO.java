package com.kakarote.build.safety.entity.BO;

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
 * 拌合站监控
 * </p>
 *
 * @author wnj
 * @since 2021-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("拌合站监控")
public class SafetyMixPlantBO extends PageEntity {

    private Integer id;

    @ApiModelProperty(value = "拌合站名称")
    private String mixingPlantName;

    @ApiModelProperty(value = "砼强度等级")
    private String concreteGrade;

    @ApiModelProperty(value = "单位工程")
    private String unitWorks;

    @ApiModelProperty(value = "工程部位")
    private String proLocation;

    @ApiModelProperty(value = "试验室配合比")
    private String laboratoryMix;

    @ApiModelProperty(value = "施工配合比")
    private String constructionMix;

    @ApiModelProperty(value = "材料名称")
    private String materialName;

    @ApiModelProperty(value = "材料标准重量")
    private Double materialWeight;

    @ApiModelProperty(value = "实际重量")
    private Double actualWeight;

    @ApiModelProperty(value = "允许误差")
    private String allowableError;

    @ApiModelProperty(value = "实际误差")
    private String actualError;

    @ApiModelProperty(value = "报警级别")
    private String alarmLevel;

    @ApiModelProperty(value = "报警时间")
    private Date alarmTime;

    @ApiModelProperty(value = "报警开始时间")
    private Date alarmTimeStart;
    @ApiModelProperty(value = "报警结束时间")
    private Date alarmTimeEnd;

}
