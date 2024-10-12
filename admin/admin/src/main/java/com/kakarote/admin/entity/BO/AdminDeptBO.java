package com.kakarote.admin.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author zhangzhiwei
 */
@Data
@ToString
@ApiModel(value="部门编辑对象", description="部门对象")
public class AdminDeptBO {

    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    @ApiModelProperty(value = "上级部门ID，0为最上级")
    @NotNull
    private Integer pid;

    @ApiModelProperty(value = "部门名称")
    @NotNull
    @Size(max = 20)
    private String name;

    @ApiModelProperty("状态：0停用,1启用")
    private String status;

    @ApiModelProperty("有效开始日期'")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveStartDate;

    @ApiModelProperty("有效结束日期''")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveEndDate;
}
