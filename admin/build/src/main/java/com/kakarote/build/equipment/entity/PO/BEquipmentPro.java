package com.kakarote.build.equipment.entity.PO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 基坑/高支模工程管理 
 * </p>
 *
 * @author wudw
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BEquipmentPro对象", description="基坑/高支模工程管理 ")
public class BEquipmentPro implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "10 基坑 20高支模")
    private String type;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "相对位置X轴")
    private String planeCoordinateX;

    @ApiModelProperty(value = "相对位置Y轴")
    private String planeCoordinateY;

    @ApiModelProperty(value = "工程图片")
    private String imgId;

    @ApiModelProperty(value = "工程图片尺寸")
    private String size;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "c4d模型路径")
    private String c4dUrl;

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

    private String fileId;


}
