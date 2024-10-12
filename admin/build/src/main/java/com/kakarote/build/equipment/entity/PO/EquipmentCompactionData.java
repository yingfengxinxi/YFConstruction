package com.kakarote.build.equipment.entity.PO;

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
 * 强夯数字化监测
 * </p>
 *
 * @author lzy
 * @since 2022-08-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_compaction_data")
@ApiModel(value="EquipmentCompactionData对象", description="强夯数字化监测")
public class EquipmentCompactionData implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "强夯区段编号")
    private String dynamicCompactionNo;

    @ApiModelProperty(value = "强夯区段名称")
    private String dynamicCompactionName;

    @ApiModelProperty(value = "强夯设备类型 字典项 b_compaction_equi_type")
    private String dynamicCompactionType;

    @ApiModelProperty(value = "夯击遍数")
    private String tampingTimes;

    @ApiModelProperty(value = "强夯锤重")
    private String dynamicCompactionVertical;

    @ApiModelProperty(value = "夯锤落距")
    private String rammerDropDistance;

    @ApiModelProperty(value = "沉降量变化")
    private String settlementVariation;

    @ApiModelProperty(value = "检测报告编号")
    private String testReportNo;

    @ApiModelProperty(value = "检测报告名称")
    private String testReportName;

    @ApiModelProperty(value = "检测结论")
    private String testReportResult;

    @ApiModelProperty(value = "其他")
    private String other;

    @ApiModelProperty(value = "数据产生时间")
    private Date eventTime;

    @ApiModelProperty(value = "强夯监测状态，0：监测中，1：监测完毕")
    private Integer isSupervise;

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
