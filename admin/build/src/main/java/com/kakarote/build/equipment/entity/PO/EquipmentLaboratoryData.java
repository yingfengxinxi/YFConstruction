package com.kakarote.build.equipment.entity.PO;

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
 * 试验室远程监控
 * </p>
 *
 * @author lzy
 * @since 2022-08-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_laboratory_data")
@ApiModel(value="EquipmentLaboratoryData对象", description="试验室远程监控")
public class EquipmentLaboratoryData implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "报警时间")
    private Date alarmTime;

    @ApiModelProperty(value = "报警类型 1：预警；2：报警；")
    private Integer alarmLevel;

    @ApiModelProperty(value = "报警内容")
    private String alarmContent;

    @ApiModelProperty(value = "使用部位")
    private String applicationSite;

    @ApiModelProperty(value = "其他")
    private String other;

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
