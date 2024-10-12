package com.kakarote.build.project.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 施工建筑安全检查
 * </p>
 *
 * @author author
 * @since 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_progress_security")
@ApiModel(value="BProgressSecurity对象", description="施工建筑安全检查")
public class BProgressSecurity implements Serializable {

    private static final long serialVersionUID=1L;

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
