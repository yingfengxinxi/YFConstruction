package com.kakarote.build.jg.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lzy
 * @since 2021-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_jg_sampling_concrete")
@ApiModel(value="JgSamplingConcrete对象", description="")
public class JgSamplingConcrete implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "工程部位")
    private String engineeringPart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "取样日期")
    private Date samplingDate;

    @ApiModelProperty(value = "见证单位")
    private String witnessUnit;

    @ApiModelProperty(value = "样品编号")
    private String samplesNumber;

    @ApiModelProperty(value = "样品名称")
    private String samplesName;

    @ApiModelProperty(value = "样品数量")
    private Integer number;

    @ApiModelProperty(value = "样品描述")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "检验日期")
    private Date checkDate;

    @ApiModelProperty(value = "取样地点")
    private String samplingSite;

    @ApiModelProperty(value = "检验依据")
    private String cehckGist;

    @ApiModelProperty(value = "检验环境")
    private String checkEnv;

    @ApiModelProperty(value = "设计强度")
    private String designStrength;

    @ApiModelProperty(value = "试件尺寸")
    private Double samplingSize;

    @ApiModelProperty(value = "生产单位")
    private String productionUnit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "制样日期")
    private Date samplePreparationDate;

    @ApiModelProperty(value = "附件 批次id")
    private String batchId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "项目名称")
    @TableField(exist = false)
    private String projectName;


}
