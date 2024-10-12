package com.qdoner.datarecept.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 设备类型信息
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_type")
@ApiModel(value = "EquipmentType对象", description = "设备类型信息")
public class EquipmentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "type_id", type = IdType.AUTO)
    private Integer typeId;

    @ApiModelProperty(value = "类型编号")
    private String typeCode;

    @ApiModelProperty(value = "类型名称")
    private String typeName;

    @ApiModelProperty(value = "类型名称")
    private Integer alarmHourTime;

    @ApiModelProperty(value = "父id")
    private Integer typePid;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    private Long updateUserId;

    private Integer tenantId;

    private Integer projectId;

    @TableField(exist = false)
    private String typePcode;

    @TableField(exist = false)
    private String typePname;

    @TableField(exist = false)
    private String createUserName;

    @TableField(exist = false)
    private String updateUserName;

    @TableField(exist = false)
    @ApiModelProperty(value = "判断是否有子级")
    private boolean hasChildren;

    @TableField(exist = false)
    @ApiModelProperty(value = "判断是否叶子节点")
    private boolean leaf;

    @ApiModelProperty(value = "监测类型")
    private String monitorType;

    @ApiModelProperty(value = "设备分布图")
    private String planeImg;

    @ApiModelProperty(value = "是否扩展设备信息")
    private String ifExpInfo;

    @ApiModelProperty(value = "扩展信息前端组件")
    private String expInfoCom;
}
