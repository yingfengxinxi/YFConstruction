package com.kakarote.build.material.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 物料称重
 * </p>
 *
 * @author author
 * @since 2021-08-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_material_weighing")
@ApiModel(value="BMaterialWeighing对象", description="物料称重")
public class BMaterialWeighing implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "过磅单号")
    private String weighingOrderOn;

    @ApiModelProperty(value = "车牌号")
    private String licenseNumber;

    @ApiModelProperty(value = "物料类型")
    private String materialType;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "规格型号")
    private String specificationsType;

    @ApiModelProperty(value = "发货单位")
    private String deliveryUnit;

    @ApiModelProperty(value = "收货单位")
    private String thegoodsUnit;

    @ApiModelProperty(value = "运输单位")
    private String transportUnit;

    @ApiModelProperty(value = "司机")
    private String theDriver;

    @ApiModelProperty(value = "毛重")
    private Double grossWeight;

    @ApiModelProperty(value = "皮重")
    private Double tareWeight;

    @ApiModelProperty(value = "净重")
    private Double thenetWeight;

    @ApiModelProperty(value = "扣杂")
    private Double buckleMiscellaneous;

    @ApiModelProperty(value = "实重")
    private Double actualWeight;

    @ApiModelProperty(value = "单价")
    private Double unitPrice;

    @ApiModelProperty(value = "金额")
    private Double amountOf;

    @ApiModelProperty(value = "数量")
    private Double numberOf;


}
