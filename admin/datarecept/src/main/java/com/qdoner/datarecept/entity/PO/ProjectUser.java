package com.qdoner.datarecept.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
