package com.kakarote.build.safety.entity.PO;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 安全资料变更记录
 * </p>
 *
 * @author wudw
 * @since 2021-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_material_changelog")
@ApiModel(value="SafetyMaterialChangelog对象", description="安全资料变更记录")
public class SafetyMaterialChangelog implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "租户ID")
    private Integer tenantId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUserId;

    @ApiModelProperty(value = "主数据ID")
    private Integer parentId;

    @ApiModelProperty(value = "版本号")
    private String versionCode;

    @ApiModelProperty(value = "变更说明")
    private String changelogExplain;

    @ApiModelProperty(value = "审核人")
    private String examineBy;

    @ApiModelProperty(value = "附件批次Id")
    private String batchId;


}
