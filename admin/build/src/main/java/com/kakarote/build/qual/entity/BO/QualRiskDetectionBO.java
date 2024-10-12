package com.kakarote.build.qual.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author lzy
 * @since 2022-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="质量隐患排查", description="")
public class QualRiskDetectionBO extends PageEntity {

    @ApiModelProperty(value = "隐患编号")
    private String hiddenNum;

    @ApiModelProperty(value = "巡检部位")
    private String inspectionPosition;

    @ApiModelProperty(value = "数据类型：1：日常质量巡检、2：企业质量巡检")
    private String dataType;

    @ApiModelProperty(value = "问题级别 字典项")
    private String problemLevel;

    @ApiModelProperty(value = "紧急程度 字典项")
    private String urgency;

    @ApiModelProperty(value = "隐患状态 0：已检查；1：已下发；:2：已整改；3：已复检")
    private String infoState;

}
