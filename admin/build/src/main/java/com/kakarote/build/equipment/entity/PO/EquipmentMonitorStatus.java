package com.kakarote.build.equipment.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备监测-监测项状态记录表
 * </p>
 *
 * @author wudw
 * @since 2021-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_monitor_status")
@ApiModel(value="EquipmentMonitorStatus对象", description="设备监测-监测项状态记录表")
public class EquipmentMonitorStatus implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "监测点id")
    private Integer pointId;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "状态描述")
    private String des;

    @ApiModelProperty(value = "监测时间")
    private Date monitorTime;

    @ApiModelProperty(value = "数据传输标准版本号")
    private String vsn;

    private Integer tenantId;

    private Integer projectId;


}
