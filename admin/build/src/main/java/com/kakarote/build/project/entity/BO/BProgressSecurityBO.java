package com.kakarote.build.project.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 形象进度查询对象
 * </p>
 *
 * @author lzy
 * @since 2021-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("施工建筑安全检查")
public class BProgressSecurityBO extends PageEntity {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "检查评定项目")
    private String securityEvaluationId;

    @ApiModelProperty(value = "检查项目")
    private String securityEvaluationName;

    @ApiModelProperty(value = "检查子项")
    private String securityEvaluationSubitem;

    @ApiModelProperty(value = "扣分标准")
    private String securityEvaluationStandard;

    @ApiModelProperty(value = "应得分数")
    private Integer shouldScore;

    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "备注")
    private String remarks;
}
