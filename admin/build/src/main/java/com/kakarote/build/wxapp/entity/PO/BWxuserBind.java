package com.kakarote.build.wxapp.entity.PO;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 微信用户绑定表

 * </p>
 *
 * @author wudw
 * @since 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BWxuserBind对象", description="微信用户绑定表 ")
public class BWxuserBind implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "openID")
    @TableId(value = "openid")
    private String openid;

    private String username;

    private String password;

    private String name;

    private String phone;

    @ApiModelProperty(value = "0：正常  1：禁用")
    private String status;

    @ApiModelProperty(value = "禁用人")
    private Long stopBy;

    @ApiModelProperty(value = "禁用时间")
    private Date stopTime;

    private Integer tenantId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
