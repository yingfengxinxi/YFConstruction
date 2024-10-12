package com.kakarote.build.project.entity.BO;

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
 * 进度管理--模板设置--主表
 * </p>
 *
 * @author zy
 * @since 2021-04-28
 */
@Data
@ApiModel("进度管理--模板设置--主表")
public class ProgressModelsetBO extends PageEntity {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "名称")
    private String name;

}
