package com.kakarote.build.safety.entity.PO;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 应急物资管理
 * </p>
 *
 * @author wnj
 * @since 2021-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_emergency_material")
@ApiModel(value="SafetyEmergencyMaterial对象", description="应急物资管理")
public class SafetyEmergencyMaterial implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value="id",type=IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "审批状态  0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废")
    private Integer checkStatus;

    @ApiModelProperty(value = "审核记录ID")
    private Integer examineRecordId;

    @ApiModelProperty(value = "'负责人ID  若没有业务不需要负责人，则存入创建人即可")
    private Long ownerUserId;

    @NotNull
    @ApiModelProperty(value = "物资编号")
    private String emergencyMaterialNumId;

    @NotNull
    @ApiModelProperty(value = "物资名称")
    private String emergencyMaterialName;

    @ApiModelProperty(value = "物资类型（1.生活保障物资、2.工程材料与机械加工设备、3.应急装备及配套物资）")
    private String emergencyMaterialType;

    @ApiModelProperty(value = "物资型号")
    private String emergencyMaterialModel;

    @ApiModelProperty(value = "物资数量")
    private Integer emergencyMaterialNumber;

    @ApiModelProperty(value = "物资所在位置")
    private String emergencyMaterialPosition;

    @ApiModelProperty(value = "联系电话")
    private String emergencyMaterialPhone;

    @ApiModelProperty(value = "安全应急物资管理负责人姓名")
    private String emergencyMaterialPerson;

}
