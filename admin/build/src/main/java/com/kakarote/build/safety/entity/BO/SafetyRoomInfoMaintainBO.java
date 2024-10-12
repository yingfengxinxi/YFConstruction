package com.kakarote.build.safety.entity.BO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 库房信息维护
 * </p>
 *
 * @author wnj
 * @since 2021-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("库房信息维护")
public class SafetyRoomInfoMaintainBO extends PageEntity {

    private Integer id;

    @ApiModelProperty(value = "库房编号")
    private String roomNum;

    @ApiModelProperty(value = "库房名称 ")
    private String roomName;

    @ApiModelProperty(value = "库房存储材料类型")
    private String roomMateriType;

    @ApiModelProperty(value = "库房管理员姓名")
    private String roomAdminName;

    @ApiModelProperty(value = "库房管理员身份证号")
    private String roomAdminCard;

    @ApiModelProperty(value = "库房管理员性别(0:男,1:女)")
    private String roomAdminSex;

    @ApiModelProperty(value = "库房管理员联系方式")
    private String roomAdminPhone;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;


}
