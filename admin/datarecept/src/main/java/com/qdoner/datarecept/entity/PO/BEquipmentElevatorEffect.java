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
 * 升降机功效数据
 * </p>
 *
 * @author wudw
 * @since 2022-07-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BEquipmentElevatorEffect对象", description="升降机功效数据")
public class BEquipmentElevatorEffect implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "升降机id")
    private Integer assetId;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "载重")
    private Double weight;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "载重百分比")
    private Double percentRate;

    @ApiModelProperty(value = "人数")
    private Integer numberOfPeople;

    @ApiModelProperty(value = "开始楼层")
    private Integer startFloor;

    @ApiModelProperty(value = "呼叫楼层")
    private Integer endFloor;

    @ApiModelProperty(value = "风速")
    private Double wind;

    @ApiModelProperty(value = "升降机运行速度")
    private Double speed;

    @ApiModelProperty(value = "倾斜角度")
    private Double angle;

    @ApiModelProperty(value = "Bypass开关状态 0关闭，1开启")
    private String bypass;

    @ApiModelProperty(value = "预警码；无则填0")
    private Integer earlyAlarm;

    @ApiModelProperty(value = "报警码；无则填0")
    private Integer alarm;

    @ApiModelProperty(value = "y倾斜角度")
    private Double angleY;

    @ApiModelProperty(value = "本次起升起点高度")
    private Double startHeight;

    @ApiModelProperty(value = "本次起升终点高度")
    private Double endHeight;

    @ApiModelProperty(value = "本次起升的行程高度")
    private Double height;

    @ApiModelProperty(value = "本次起升方向")
    private String direction;
}
