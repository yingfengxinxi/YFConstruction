package com.qdoner.datarecept.env.entity.PO;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 扬尘在线监测设备数据传输日志
 * </p>
 *
 * @author shz
 * @since 2021-08-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_env_post_log")
@ApiModel(value="EnvPostLog对象", description="扬尘在线监测设备数据传输日志")
public class EnvPostLog implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "项目id，对外开放时使用。")
    private String appid;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddhhmmss", timezone = "GMT+8")
    @ApiModelProperty(value = "时间戳")
    private Date timestamp;

    @ApiModelProperty(value = "签名，用于校验数据的真实性。")
    private String sign;

    @ApiModelProperty(value = "用户传输的JSON数据")
    private String data;

    @ApiModelProperty(value = "错误码 默认为0，0为成功")
    private Integer code;

    @ApiModelProperty(value = "错误信息")
    private String msg;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
