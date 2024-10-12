package com.kakarote.build.safety.entity.PO;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 拌合站监控基本信息
 * </p>
 *
 * @author lzy
 * @since 2022-08-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_mix_plant_data")
@ApiModel(value="SafetyMixPlantData对象", description="拌合站监控基本信息")
public class SafetyMixPlantData implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商砼厂家")
    private String manufactor;

    @ApiModelProperty(value = "砼强度等级")
    private String grade;

    @ApiModelProperty(value = "工程部位")
    private String position;

    @ApiModelProperty(value = "试验室材料登记号")
    private String labMaterialNum;

    @ApiModelProperty(value = "试验室材料种类")
    private String labMaterialName;

    @ApiModelProperty(value = "试验室材料占比（%）")
    private Double labMaterialVolume;

    @ApiModelProperty(value = "工地材料登记号")
    private String materialNum;

    @ApiModelProperty(value = "工地材料种类")
    private String materialName;

    @ApiModelProperty(value = "工地用量（kg/m³）")
    private Double materialVolume;

    @ApiModelProperty(value = "工地允许误差")
    private Double allowError;

    @ApiModelProperty(value = "工地实际误差")
    private Double actualError;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    private Integer updateUserId;

    private Integer tenantId;

    private Integer projectId;


}
