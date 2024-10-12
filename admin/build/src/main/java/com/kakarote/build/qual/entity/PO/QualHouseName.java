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
 * 施工质量--分户验收--户型管理--子表--房间名称
 * </p>
 *
 * @author wnj
 * @since 2021-06-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_house_name")
@ApiModel(value = "QualHouseName对象", description = "施工质量--分户验收--户型管理--子表--房间名称")
public class QualHouseName implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "户型模板id")
    private Integer houseTypeId;

    @ApiModelProperty(value = "房间名称")
    private String houseName;

    @ApiModelProperty(value = "验收工序")
    private String acceptWork;

    @ApiModelProperty(value = "验收工序名字")
    private String acceptWorkName;

    @ApiModelProperty(value = "验收模板")
    private Integer acceptTemplate;

    @ApiModelProperty(value = "验收模板名字")
    private String acceptTemplateName;

    @ApiModelProperty(value = "类型")
    private String type;

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

    @ApiModelProperty(value = "判断表格保存状态")
    @TableField(exist = false)
    private String start;
}
