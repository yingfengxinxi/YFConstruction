package com.kakarote.admin.entity.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhangzhiwei
 */
@Data
@ToString
@ApiModel(value="部门查询对象", description="部门对象")
public class AdminDeptVO {

    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    @ApiModelProperty(value = "部门ID2")
    private Integer id;

    @ApiModelProperty(value = "上级部门ID，0为最上级")
    private Integer pid;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "部门label")
    private String label;

    @TableField(exist = false)
    @ApiModelProperty(value = "菜单列表")
    private Map<String, List<Integer>> rules;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.label = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
        this.id = deptId;
    }

    @ApiModelProperty(value = "下级部门列表")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<AdminDeptVO> children;

    @ApiModelProperty("状态：0停用,1启用")
    private String status;

    @ApiModelProperty("有效开始日期'")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveStartDate;

    @ApiModelProperty("有效结束日期''")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveEndDate;
}
