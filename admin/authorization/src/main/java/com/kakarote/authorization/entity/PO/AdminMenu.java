package com.kakarote.authorization.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * 后台菜单表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
@Data
@Accessors(chain = true)
@TableName("wk_admin_menu")
@ApiModel(value="AdminMenu对象", description="后台菜单表")
public class AdminMenu implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "菜单ID")
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;

    @ApiModelProperty(value = "上级菜单ID")
    private Integer parentId;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "名称")
    private String menuName;

    @ApiModelProperty(value = "权限标识")
    private String realm;

    @ApiModelProperty(value = "权限URL")
    private String realmUrl;

    @ApiModelProperty(value = "所属模块")
    private String realmModule;

    @ApiModelProperty(value = "路由")
    private String path;

    @ApiModelProperty(value = "前端组件")
    private String component;

    @ApiModelProperty(value = "类型  1目录 2 菜单 3 按钮 4特殊")
    private Integer menuType;

    @ApiModelProperty(value = "排序（同级有效）")
    private Integer sort;

    @ApiModelProperty(value = "状态 1 启用 0 禁用")
    private Integer status;

    @ApiModelProperty(value = "菜单说明")
    private String remarks;

    @TableField(exist = false)
    @ApiModelProperty(value = "数据权限")
    private Integer dataType;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否存在下级节点")
    private boolean hasChildren;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminMenu adminMenu = (AdminMenu) o;
        return Objects.equals(menuId, adminMenu.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId);
    }
}
