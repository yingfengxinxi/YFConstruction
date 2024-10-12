package com.kakarote.build.jg.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 信用良好行为
 * </p>
 *
 * @author author
 * @since 2021-11-25
 */
@Data
@ApiModel(value = "信用良好行为")
public class JgSamplingConcreteProcessBO extends PageEntity {

    @ApiModelProperty(value = "混凝土取样id")
    private Integer samplingConcreteId;

}
