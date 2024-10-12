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
 * 施工综合--物料出场
 * </p>
 *
 * @author zy
 * @since 2021-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_complex_materials_out")
@ApiModel(value="ComplexMaterialsOut对象", description="施工综合--物料出场")
public class ComplexMaterialsOut implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "物料进场主键")
    private Integer inId;


    @ApiModelProperty(value = "退场数量")
    private Integer outNumber;


    @ApiModelProperty(value = "退场时间")
    private Date outTime;

    @ApiModelProperty(value = "退料人员id")
    private Long materialClerkId;

    @ApiModelProperty(value = "退料人员")
    private String materialClerk;

    @ApiModelProperty(value = "见证人id")
    private Long witnessId;

    @ApiModelProperty(value = "见证人")
    private String witness;


    @ApiModelProperty(value = "退场原因")
    private String outReason;

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
