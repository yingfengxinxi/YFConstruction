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
 * 塔机功效数据
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BEquipmentTowerEffect对象", description="塔机功效数据")
public class BEquipmentTowerEffect implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "塔机id")
    private Integer assetId;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "平均吊重")
    private Double weight;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "最大幅度")
    private Double maxRadius;

    @ApiModelProperty(value = "起始回转角度")
    private Double startAngle;

    @ApiModelProperty(value = "最大力矩百分比")
    private Double maxPercent;

    @ApiModelProperty(value = "最大高度")
    private Double maxHeight;

    @ApiModelProperty(value = "卸吊点的回转角度")
    private Double endAngle;

    @ApiModelProperty(value = "本次吊物过程中的最小幅度")
    private Double minRadius;

    @ApiModelProperty(value = "本次吊物过程中的最大起重本次吊物最大风速")
    private Double maxLoad;

    @ApiModelProperty(value = "本次吊物最大风速")
    private Double maxWindSpeed;

    @ApiModelProperty(value = "本次吊物过程中的最小高度")
    private Double minHeight;

    @ApiModelProperty(value = "力矩最大点系统预警；无则填0")
    private Integer maxMomentEarlyAlarm;

    @ApiModelProperty(value = "卸吊点的系统预警状态编码；无则填0")
    private Integer endEarlyAlarm;

    @ApiModelProperty(value = "起吊点的系统预警状态编码；无则填0")
    private Integer startEarlyAlarm;

    @ApiModelProperty(value = "力矩最大点系统报警状态编码；无则填0")
    private Integer maxMomentAlarm;

    @ApiModelProperty(value = "卸吊点的系统报警状态编码；无则填0")
    private Integer endAlarm;

    @ApiModelProperty(value = "起吊点的系统报警状态编码；无则填0")
    private Integer startAlarm;

    @ApiModelProperty(value = "起吊点违章码；无则填0")
    private Integer startPeccancy;

    @ApiModelProperty(value = "卸吊点违章码；无则填0")
    private Integer endPeccancy;

    @ApiModelProperty(value = "力矩最大点违章码；无则填0")
    private Integer maxMomentPeccancy;

    @ApiModelProperty(value = "起吊点幅度")
    private Float liftingPointRadius;

    @ApiModelProperty(value = "起吊点高度")
    private Float liftingPointHeight;

    @ApiModelProperty(value = "卸吊点幅度")
    private Float liftingPointRadiusUnload;

    @ApiModelProperty(value = "卸吊点高度")
    private Float liftingPointHeightUnload;

}
