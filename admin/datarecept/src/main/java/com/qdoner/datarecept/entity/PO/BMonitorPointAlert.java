package com.qdoner.datarecept.entity.PO;

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
 * 监测点警报
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BMonitorPointAlert对象", description="监测点警报")
public class BMonitorPointAlert implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "监测点id")
    private Integer monitorPointId;

    @ApiModelProperty(value = "设备id")
    private Integer equipmentId;

    @ApiModelProperty(value = "监测数据(报警值)")
    private String monitorData;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "处理时间")
    private Date handleTime;

    @ApiModelProperty(value = "监测数据(恢复值)")
    private String recoveryData;

    @ApiModelProperty(value = "处理人")
    private String handleBy;

    @ApiModelProperty(value = "处理措施")
    private String handleMeasures;

    @ApiModelProperty(value = "处理后结论")
    private String handleConclu;

    @ApiModelProperty(value = "处理状态（0：未处理，1：已处理）")
    private String handleState;

    @ApiModelProperty(value = "过程图片批次id")
    private String batchId;

    @ApiModelProperty(value = " 创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    private Long updateUserId;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "提醒类型  0预警提醒，1警报提醒 2控制")
    private String alertType;

    @ApiModelProperty(value = "设备报警类型 该字段用来判断当前数据是否是设备离线报警数据 字典b_monitor_alert_type")
    private String monitorAlertType;


}
