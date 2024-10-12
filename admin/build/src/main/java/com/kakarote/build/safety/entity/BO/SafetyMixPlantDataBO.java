package com.kakarote.build.safety.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class SafetyMixPlantDataBO extends PageEntity {

    @ApiModelProperty(value = "拌合站名称")
    private String mixingPlantName;

    @ApiModelProperty(value = "砼强度等级")
    private String concreteGrade;

}
