package com.kakarote.build.project.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 单体楼座信息
 * </p>
 *
 * @author wudw
 * @since 2021-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_single_building_info")
@ApiModel(value="SingleBuildingInfo对象", description="单体楼座信息")
public class SingleBuildingInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "楼座")
    private String building;

    @ApiModelProperty(value = "单元数")
    private String unitsNumber;

    @ApiModelProperty(value = "地上层数")
    private Integer abovegroundNumber;

    @ApiModelProperty(value = "地下层数")
    private Integer undergroundNumber;

    @ApiModelProperty(value = "是否公共区域")
    private String isPublic;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUserId;

    @ApiModelProperty(value = "父级")
    private Integer parentId;

    @TableField(exist = false)
    @ApiModelProperty(value = "类型名称")
    private String typeName;


}
