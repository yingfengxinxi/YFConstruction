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
 * 施工质量--分户验收--楼栋管理
 * </p>
 *
 * @author wnj
 * @since 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("施工质量--分户验收--楼栋管理")
public class QualBuildMangeBO extends PageEntity {

    private Integer buildingId;

    @ApiModelProperty(value = "父级id")
    private Integer parentId;

    @ApiModelProperty(value = "楼栋名称")
    private String buildingName;

    @ApiModelProperty(value = "业态类型")
    private String businessType;

    @ApiModelProperty(value = "楼层数量")
    private Integer floorNumber;

    @ApiModelProperty(value = "每层户数")
    private Integer roomInFloor;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "0：停用 1：启用 ")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remarks;

    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Long updateBy;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    private Integer tenantId;

    private Integer projectId;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否存在下级节点")
    private boolean hasChildren;
}
