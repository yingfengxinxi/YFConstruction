package com.kakarote.build.equipment.entity.PO;

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
 * 机械设备证书管理
 * </p>
 *
 * @author author
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_certificate")
@ApiModel(value="BEquipmentCertificate对象", description="机械设备证书管理")
public class BEquipmentCertificate implements Serializable {

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

    @ApiModelProperty(value = "设备类型")
    private Integer equipmentType;

    @ApiModelProperty(value = "证书种类")
    private String certificateSpecies;

    @ApiModelProperty(value = "证书类型(可多选)")
    private String certificateType;

    @ApiModelProperty(value = "备注")
    private String remarks;


}
