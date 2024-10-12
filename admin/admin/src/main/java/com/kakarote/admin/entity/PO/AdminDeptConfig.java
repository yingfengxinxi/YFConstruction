package com.kakarote.admin.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @author author
 * @since 2020-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_admin_dept_config")
@ApiModel(value="AdminDeptConfig对象", description="")
public class AdminDeptConfig implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "配置类型（10：钉钉内部应用、20：企业微信内部应用、……）")
    private String type;

    private String name;

    private String params;

    @ApiModelProperty(value = "状态 0:禁用  1:启用")
    private String status;

    private String messageType;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Integer tenantId;


}
