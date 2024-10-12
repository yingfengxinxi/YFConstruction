package com.kakarote.build.project.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @author wudw
 * @since 2021-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_project_user")
@ApiModel(value="ProjectUser对象", description="")
public class ProjectUser implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    private Integer userId;

    private Integer projectId;

    @TableField(exist = false)
    private String projectName;

    private Integer tenantId;
}
