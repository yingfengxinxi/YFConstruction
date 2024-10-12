package com.kakarote.build.jg.entity.BO;

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
@ApiModel(value = "信用良好行为")
public class JgSamplingConcreteBO extends PageEntity {

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "样品名称")
    private String samplesName;

}
