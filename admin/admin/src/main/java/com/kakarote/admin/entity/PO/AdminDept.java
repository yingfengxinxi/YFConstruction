package com.kakarote.admin.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_admin_dept")
@ApiModel(value="AdminDept对象", description="部门表")
public class AdminDept implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "dept_id", type = IdType.AUTO)
    private Integer deptId;

    @ApiModelProperty(value = "父级ID 顶级部门为0")
    private Integer pid;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "排序 越大越靠后")
    private Integer num;

    @ApiModelProperty(value = "部门备注")
    private String remark;

    @TableField(exist = false)
    @ApiModelProperty(value = "菜单列表")
    private Map<String, List<Integer>> rules;

    @TableField(exist = false)
    @ApiModelProperty(value = "菜单id列表")
    private List<Integer> menuIds = new ArrayList<>();

    @TableField(select = false)
    @ApiModelProperty(value = "租户ID")
    private Integer tenantId;

    @ApiModelProperty("状态：0停用,1启用")
    private String status;

    @ApiModelProperty("有效开始日期'")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveStartDate;

    @ApiModelProperty("有效结束日期''")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveEndDate;
}
