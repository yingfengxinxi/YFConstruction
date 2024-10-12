package com.kakarote.core.feign.wf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务审批对象
 */
@Data
public class WfVO {

    @ApiModelProperty(value = "业务类型")
    @TableField(exist = false)
    private Integer businessType;

    @ApiModelProperty(value = "审批人")
    @TableField(exist = false)
    private Long checkUserId;

    @ApiModelProperty(value = "是否提交")
    @TableField(exist = false)
    private Boolean isSubmit;

    @ApiModelProperty(value = "数据ID")
    @TableField(exist = false)
    private Object objectId;
}
