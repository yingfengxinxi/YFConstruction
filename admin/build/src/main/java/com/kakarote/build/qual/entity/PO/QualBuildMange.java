package com.kakarote.build.qual.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@TableName("b_qual_build_mange")
@ApiModel(value="QualBuildMange对象", description="施工质量--分户验收--楼栋管理")
public class QualBuildMange implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "building_id", type = IdType.AUTO)
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

    @ApiModelProperty(value = "分配户型id")
    private String distribHouseTypeId;

    @ApiModelProperty(value = "层级类型")
    private String treeNodeType;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "0：停用 1：启用  默认启用")
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

    @TableField(exist = false)
    @ApiModelProperty(value = "层号格式模板")
    private String floorIsCheck;

    @TableField(exist = false)
    @ApiModelProperty(value = "房间号格式模板")
    private String roomIsCheck;

    @TableField(exist = false)
    @ApiModelProperty(value = "楼栋名称数组")
    private String buildingNames;
}
