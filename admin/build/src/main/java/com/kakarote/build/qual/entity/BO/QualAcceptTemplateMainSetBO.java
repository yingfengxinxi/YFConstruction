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
 * 施工质量--分户验收--模板设置--主表
 * </p>
 *
 * @author wnj
 * @since 2021-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("施工质量--分户验收--模板设置--主表")
public class QualAcceptTemplateMainSetBO extends PageEntity {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "模板名称")
    private String templateName;

    @ApiModelProperty(value = "验收工序")
    private String acceptWork;
}
