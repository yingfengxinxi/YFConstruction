package com.qdoner.message.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.feign.admin.entity.SimpleUser;
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
 * @since 2021-09-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wm_assign_user")
@ApiModel(value="AssignUser对象", description="")
public class AssignUser implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "类型 部门：10 人员：20")
    private String type;

    @ApiModelProperty(value = "名称")
    private String name;

    private String dataId;

    private Integer tenantId;

    private String code;

    private String targetType;

    @ApiModelProperty(value = "通知部门列表")
    @TableField(exist = false)
    private List<SimpleDept> deptList;

    @ApiModelProperty(value = "通知人列表")
    @TableField(exist = false)
    private List<SimpleUser> userList;

}
