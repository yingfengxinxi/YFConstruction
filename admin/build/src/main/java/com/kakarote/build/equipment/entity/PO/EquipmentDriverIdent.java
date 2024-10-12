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
 * 机械设备司机识别
 * </p>
 *
 * @author zy
 * @since 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_driver_ident")
@ApiModel(value="EquipmentDriverIdent对象", description="机械设备司机识别")
public class EquipmentDriverIdent implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备编号")
    private String equCode;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "上岗时间")
    private Date startingDate;

    @ApiModelProperty(value = "下岗时间")
    private Date endingDate;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.UPDATE)
    private Long updateUserId;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "证书id")
    private String certificateId;

    @ApiModelProperty(value = "司机图片URL")
    private String photoUrl;

    @ApiModelProperty(value = "设备类型 1塔吊 2升降机")
    private String deviceType;

    @ApiModelProperty(value = "姓名")
    private String name;
}
