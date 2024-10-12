package com.qdoner.message.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
 * @since 2021-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wm_exchanges")
@ApiModel(value="Exchanges对象", description="")
public class Exchanges implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "备注说明")
    private String descr;

    private String name;

    private String type;

    @ApiModelProperty(value = "持久化")
    private String durability;

    @ApiModelProperty(value = "自动删除")
    private String autoDelete;

    @ApiModelProperty(value = "内部使用")
    private String internal;

    @ApiModelProperty(value = "扩展参数")
    private String arguments;

    @ApiModelProperty(value = "系统内置")
    private String isSys;

    @ApiModelProperty(value = "最近一次部署时间")
    private Date deployTime;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
