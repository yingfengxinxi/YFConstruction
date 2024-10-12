package com.kakarote.admin.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 部门（租户）菜单对应关系表
 * </p>
 *
 * @author pangzhen
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_admin_dept_menu")
@ApiModel(value="AdminDeptMenu对象", description="部门（租户）菜单对应关系表")
public class AdminDeptMenu implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "部门（租户）ID")
    private Integer deptId;

    @ApiModelProperty(value = "菜单ID")
    private Integer menuId;

    @TableField(select = false)
    @ApiModelProperty(value = "租户ID")
    private Integer tenantId;


}
