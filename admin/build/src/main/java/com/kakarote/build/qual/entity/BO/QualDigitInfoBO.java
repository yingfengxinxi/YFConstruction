package com.kakarote.build.qual.entity.BO;

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
 * 数字质量资料
 * </p>
 *
 * @author pz
 * @since 2021-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("数字质量资料")
public class QualDigitInfoBO extends PageEntity {

    @ApiModelProperty(value = "编码")
    private String infoCode;

    @ApiModelProperty(value = "名称")
    private String infoName;

    @ApiModelProperty(value = "提交时间")
    private Date submitDate;

    @ApiModelProperty(value = "提交人")
    private String submitUserId;

}
