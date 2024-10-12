package com.kakarote.build.project.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author lzy
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("信息公示查询对象")
public class ProjectPublicInfoBO extends PageEntity {

    @ApiModelProperty(value = "公示标题")
    private String publicTitle;

    @ApiModelProperty(value = "公示人")
    private String publicPeople;
}
