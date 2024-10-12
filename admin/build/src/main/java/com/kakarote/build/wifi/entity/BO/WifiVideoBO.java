package com.kakarote.build.wifi.entity.BO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.web.servlet.tags.Param;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 教育视频配置--主表
 * </p>
 *
 * @author zy
 * @since 2021-06-01
 */
@Data
@ApiModel("教育视频配置--主表")
public class WifiVideoBO extends PageEntity {

    @ApiModelProperty(value = "教育模板名称")
    private String name;

    @ApiModelProperty(value = "适应工种id")
    private String workTypeId;

    @ApiModelProperty(value = "是否启用")
    private String ifStartUse;


    @ApiModelProperty(value = "视频模板id")
    private String vId;

    private Long projectId;

    private Long tenantId;


}
