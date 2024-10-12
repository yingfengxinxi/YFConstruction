package com.kakarote.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_admin_system_log")
@ApiModel(value="AdminSystemLog对象", description="系统日志表")
public class AdminSystemLog implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "操作人id")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "请求类型")
    private String requestMethod;

    @ApiModelProperty(value = "请求地址")
    private String url;

    @ApiModelProperty(value = "模块 0其它 1系统  2crm  3oa")
    private Integer types;

    @ApiModelProperty(value = "行为 0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据")
    private Integer behavior;

    @ApiModelProperty(value = "操作对象")
    private String object;

    @ApiModelProperty(value = "操作参数")
    private String parameter;

    @ApiModelProperty(value = "返回值")
    private String returnValue;

    @ApiModelProperty(value = "是否成功")
    private Integer isSuccess;

    @ApiModelProperty(value = "报错信息")
    private String errMsg;

    @ApiModelProperty(value = "操作详情")
    private String detail;
}
