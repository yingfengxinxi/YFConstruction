package com.qdoner.datarecept.env.entity.PO;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 扬尘在线监测设备数据
 * </p>
 *
 * @author shz
 * @since 2021-08-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_env_post_data")
@ApiModel(value="EnvPostData对象", description="扬尘在线监测设备数据")
public class EnvPostData implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备id")
    private Integer equipmentId;

    @ApiModelProperty(value = "监控类型 字典项")
    @TableField("monitorType")
    private String monitorType;

    @ApiModelProperty(value = "监测值，整数字符串")
    @TableField("monitorValue")
    private String monitorValue;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "传输记录id")
    private Integer logId;

    private Integer tenantId;

    private Integer projectId;


}
