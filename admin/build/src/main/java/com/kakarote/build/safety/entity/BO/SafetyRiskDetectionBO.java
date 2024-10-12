package com.kakarote.build.safety.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 安全隐患排查
 * </p>
 *
 * @author lzy
 * @since 2022-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("安全隐患排查")
public class SafetyRiskDetectionBO extends PageEntity {

    @ApiModelProperty(value = "隐患编号")
    private String hiddenNum;

    @ApiModelProperty(value = "隐患类别")
    private String category;

    @ApiModelProperty(value = "隐患等级")
    private String level;

    @ApiModelProperty(value = "风险点名称")
    private String riskName;

    @ApiModelProperty(value = "隐患状态 0：已检查；1：已下发；:2：已整改；3：已复检")
    private String infoState;

    @ApiModelProperty(value = "项目id")
    private String projectId;

}
