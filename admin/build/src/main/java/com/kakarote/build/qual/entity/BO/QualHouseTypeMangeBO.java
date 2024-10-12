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
 * 施工质量--分户验收--户型管理
 * </p>
 *
 * @author wnj
 * @since 2021-06-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("施工质量--分户验收--户型管理")
public class QualHouseTypeMangeBO extends PageEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "户型模板名称")
    private String houseTemplateName;

    @ApiModelProperty(value = "户型图")
    private String houseTypeImg;

    @ApiModelProperty(value = "创建者")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "模板名称")
    @TableField(exist = false)
    private String templateName;

    @ApiModelProperty(value = "验收工序")
    @TableField(exist = false)
    private String acceptWork;
}
