package com.kakarote.build.wxapp.entity.BO;

import com.baomidou.mybatisplus.annotation.TableId;
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
 * 微信用户绑定表

 * </p>
 *
 * @author wudw
 * @since 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="BWxuserBind对象", description="微信用户绑定表 ")
public class BWxuserBindBO  extends PageEntity {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

}
