package com.kakarote.build.labour.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 劳务管理--工种
 * </p>
 *
 * @author zy
 * @since 2021-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_worktype")
@ApiModel(value="LabourWorktype对象", description="劳务管理--工种")
public class LabourWorktype implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "工种管理主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "工种")
    private String workType;

    @ApiModelProperty(value = "工种首字母简拼")
    @TableField("work_type_JP")
    private String workTypeJp;

    @ApiModelProperty(value = "是否特殊工种")
    @TableField("work_type_ifspecial")
    private String workTypeIfspecial;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "项目")
    private Integer projectId;


}
