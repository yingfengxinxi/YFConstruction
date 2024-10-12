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
 * 应急事件处置明细
 * </p>
 *
 * @author zy
 * @since 2021-04-06
 */
@Data
@ApiModel("应急事件处置明细")
public class SafetyEmergencyHandlingDetailBO extends PageEntity {

    @ApiModelProperty(value = "主表id")
    private String pid;

    @ApiModelProperty(value = "处置类型")
    private String handleType;

    @ApiModelProperty(value = "处置人")
    private String handleUserName;

    @ApiModelProperty(value = "处置时间")
    private Date handleTime;

}
