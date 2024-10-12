package com.kakarote.admin.entity.BO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2020-12-16
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@ApiModel("租户对接配置列表查询")
public class AdminDeptConfigBO extends PageEntity {

    @ApiModelProperty(value = "配置类型（10：钉钉内部应用、20：企业微信内部应用、……）")
    private String type;

    private String name;

    private String params;

    @ApiModelProperty(value = "状态 0:禁用  1:启用")
    private String status;

    private String messageType;

    private Integer tenantId;
}
