package com.kakarote.build.qual.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 大体积混凝土监测数据
 * </p>
 *
 * @author zhang
 * @since 2022-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_concrete_monitoring")
@ApiModel(value="BQualConcreteMonitoring对象", description="大体积混凝土监测数据")
public class BQualConcreteMonitoring implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "测温孔ID   b_qual_concrete_point 表主键")
    private String nickId;

    @ApiModelProperty(value = "电池电量")
    private Double batteryPower;

    @ApiModelProperty(value = "测点温度")
    private Double pointTemp;


}
