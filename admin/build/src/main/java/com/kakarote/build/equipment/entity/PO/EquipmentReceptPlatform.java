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
 * 设备数据对接平台
 * </p>
 *
 * @author wudw
 * @since 2021-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_recept_platform")
@ApiModel(value="EquipmentReceptPlatform对象", description="设备数据对接平台")
public class EquipmentReceptPlatform implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "平台名称")
    private String name;

    @ApiModelProperty(value = "密钥")
    private String secret;

    @ApiModelProperty(value = "IP白名单")
    private String ips;

    @ApiModelProperty(value = "状态 1:启用 0:停用")
    private String status;

    @ApiModelProperty(value = "备注说明")
    private String remarks;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    private Long updateUserId;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "设备标识解析")
    private String resCode;

    @ApiModelProperty(value = "默认设备解析器")
    private String defEqResolver;

    @ApiModelProperty(value = "配置类型（0：平台 1：报文）")
    private String type;

    @ApiModelProperty(value = "报文端口 （10000 - 60000）")
    private String port;
}
