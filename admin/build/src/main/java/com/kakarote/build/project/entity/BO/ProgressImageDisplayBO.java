package com.kakarote.build.project.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 形象进度查询对象
 * </p>
 *
 * @author lzy
 * @since 2021-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("形象进度展示查询对象")
public class ProgressImageDisplayBO extends PageEntity {

}
