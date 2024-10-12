package com.kakarote.build.equipment.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 水电报警
 * </p>
 *
 * @author lzy
 * @since 2021-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_hydropower_alarm")
@ApiModel(value="EquipmentHydropowerAlarm对象", description="水电报警")
public class EquipmentHydropowerAlarm implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "监测区域")
    private Integer locationId;

    @ApiModelProperty(value = "能源名称 1：电量；2：水量；")
    private String energyName;

    @ApiModelProperty(value = "报警时间")
    private Date alarmTime;

    @ApiModelProperty(value = "监测开始时间")
    private Date startTime;

    @ApiModelProperty(value = "监测结束时间")
    private Date endTime;

    @ApiModelProperty(value = "累计上期使用量")
    private String accumulativePrevious;

    @ApiModelProperty(value = "本期使用量")
    private String currentUsage;

    @ApiModelProperty(value = "累计使用量")
    private String cumulativeUsage;

    @ApiModelProperty(value = "本期使用指标")
    private String currentIndex;

    @ApiModelProperty(value = "累计使用指标")
    private String cumulativeIndex;

    @ApiModelProperty(value = "超限类型 1：当期；2：累计；")
    private String transfiniteType;

    @ApiModelProperty(value = "超限数量")
    private String transfiniteNumber;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "位置名称")
    @TableField(exist = false)
    private String locationName;

    @ApiModelProperty(value = "项目名称")
    @TableField(exist = false)
    private String projectName;

}
