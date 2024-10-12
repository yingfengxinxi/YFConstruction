package com.kakarote.build.qual.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 质量资料收藏表
 * </p>
 *
 * @author wudw
 * @since 2021-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_material_user")
@ApiModel(value="QualMaterialUser对象", description="质量资料收藏表")
public class QualMaterialUser implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer materialId;

    private Integer userId;

    @ApiModelProperty(value = "租户ID")
    private Integer tenantId;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;


}
