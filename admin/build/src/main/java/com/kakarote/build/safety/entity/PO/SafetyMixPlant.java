package com.kakarote.build.safety.entity.PO;

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
 * 拌合站监控
 * </p>
 *
 * @author wnj
 * @since 2021-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_mix_plant")
@ApiModel(value="SafetyMixPlant对象", description="拌合站监控")
public class SafetyMixPlant implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "拌合站名称")
    private String mixingPlantName;

    @ApiModelProperty(value = "砼强度等级")
    private String concreteGrade;

    @ApiModelProperty(value = "单位工程")
    private String unitWorks;

    @ApiModelProperty(value = "工程部位")
    private String proLocation;

    @ApiModelProperty(value = "试验室配合比")
    private String laboratoryMix;

    @ApiModelProperty(value = "施工配合比")
    private String constructionMix;

    @ApiModelProperty(value = "材料名称")
    private String materialName;

    @ApiModelProperty(value = "材料标准重量")
    private Double materialWeight;

    @ApiModelProperty(value = "实际重量")
    private Double actualWeight;

    @ApiModelProperty(value = "允许误差")
    private String allowableError;

    @ApiModelProperty(value = "实际误差")
    private String actualError;

    @ApiModelProperty(value = "报警级别 1：预警；2报警；")
    private String alarmLevel;

    @ApiModelProperty(value = "报警时间")
    private Date alarmTime;

    @ApiModelProperty(value = "处理人")
    private Long handleUserId;

    @ApiModelProperty(value = "处理措施")
    private String handleMeasures;

    @ApiModelProperty(value = "处理后结论")
    private String handleConclusion;

    @ApiModelProperty(value = "批次")
    private String batchId;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "处理人名称")
    @TableField(exist = false)
    private String handleUserName;


}
