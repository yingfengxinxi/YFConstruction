package com.kakarote.build.equipment.entity.PO;

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
 * 设备位置信息
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_location")
@ApiModel(value="EquipmentLocation对象", description="设备位置信息")
public class EquipmentLocation implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "location_id", type = IdType.AUTO)
    private Integer locationId;

    @ApiModelProperty(value = "编码")
    private String locationCode;

    @ApiModelProperty(value = "位置名称")
    private String locationName;

    @ApiModelProperty(value = "父级ID")
    private Integer locationPid;

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
    private String locationPcode;

    @TableField(exist = false)
    private String locationPname;

    @TableField(exist = false)
    private String createUserName;

    @TableField(exist = false)
    private String updateUserName;


}
