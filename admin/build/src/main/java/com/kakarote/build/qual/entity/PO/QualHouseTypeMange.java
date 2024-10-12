package com.kakarote.build.qual.entity.PO;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@TableName("b_qual_house_type_mange")
@ApiModel(value="QualHouseTypeMange对象", description="施工质量--分户验收--户型管理")
public class QualHouseTypeMange implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "户型模板名称")
    private String houseTemplateName;

    @ApiModelProperty(value = "验收模板id")
    private Integer acceptTemplateId;

    @ApiModelProperty(value = "户型图")
    private String houseTypeImg;

    @ApiModelProperty(value = "批次id")
    private String batchId;

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

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @TableField(exist = false)
    @ApiModelProperty(value = "户型名称数组")
    private String houseTemplateNames;

    @ApiModelProperty(value = "模板名称")
    @TableField(exist = false)
    private String templateName;

    @ApiModelProperty(value = "验收工序")
    @TableField(exist = false)
    private String acceptWork;

}
