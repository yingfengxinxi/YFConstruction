package com.kakarote.build.labour.entity.BO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @Title: LabourWorktypeBO
 * @Package com.kakarote.build.labour.entity.BO
 * @Description: 描述
 * @author: ZangYu
 * @date: 2021-03-15 11:38
 */
@Data
@ApiModel("劳务管理--工种查询对象")
public class LabourWorktypeBO extends PageEntity {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "工种")
    private String workType;

    @ApiModelProperty(value = "工种首字母简拼")
    private String workTypeJp;

    @ApiModelProperty(value = "是否特殊工种")
    private String workTypeIfspecial;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}