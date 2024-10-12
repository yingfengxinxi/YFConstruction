package com.kakarote.build.safety.entity.PO;

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
 * 安全生产评价
 * </p>
 *
 * @author zjc
 * @since 2022-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_production")
@ApiModel(value="BSafetyProduction对象", description="安全生产评价")
public class BSafetyProduction implements Serializable {

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

    @ApiModelProperty(value = "评定分类")
    private String productionId;

    @ApiModelProperty(value = "评定项目")
    private String productionName;

    @ApiModelProperty(value = "评分标准")
    private String productionStandard;

    @ApiModelProperty(value = "评分方法")
    private String productionMethods;

    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "备注")
    private String remarks;


}
