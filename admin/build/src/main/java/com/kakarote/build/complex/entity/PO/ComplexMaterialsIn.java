package com.kakarote.build.complex.entity.PO;

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
 * 施工综合--物料进场
 * </p>
 *
 * @author zy
 * @since 2021-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_complex_materials_in")
@ApiModel(value="ComplexMaterialsIn对象", description="施工综合--物料进场")
public class ComplexMaterialsIn implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "物料名称")
    private String name;

    @ApiModelProperty(value = "物料编号")
    private String code;

    @ApiModelProperty(value = "规格")
    private String specification;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "进场数量")
    private Integer inNumber;

    @ApiModelProperty(value = "计量单位")
    private String unit;

    @ApiModelProperty(value = "合格证件")
    private String qualifiedCertificate;

    @ApiModelProperty(value = "进场时间")
    private Date inTime;

    @ApiModelProperty(value = "材料员id")
    private Long materialClerkId;

    @ApiModelProperty(value = "材料员")
    private String materialClerk;

    @ApiModelProperty(value = "送货人id")
    private Long deliveryManId;

    @ApiModelProperty(value = "送货人")
    private String deliveryMan;

    @ApiModelProperty(value = "生产厂家id")
    private Long manufacturerId;

    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    @ApiModelProperty(value = "供应单位id")
    private Long supplyUnitId;

    @ApiModelProperty(value = "供应单位")
    private String supplyUnit;

    @ApiModelProperty(value = "购买单位id")
    private Long purchaseUnitId;

    @ApiModelProperty(value = "购买单位")
    private String purchaseUnit;

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


}
