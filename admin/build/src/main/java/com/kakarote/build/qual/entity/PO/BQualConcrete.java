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
 * 大体积混凝土基础数据
 * </p>
 *
 * @author zhang
 * @since 2022-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_concrete")
@ApiModel(value="BQualConcrete对象", description="大体积混凝土基础数据")
public class BQualConcrete implements Serializable {

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

    @ApiModelProperty(value = "浇筑体名称")
    private String betonName;

    @ApiModelProperty(value = "大体积混凝土厚度")
    private Double thickness;

    @ApiModelProperty(value = "入模时间")
    private Date intoModelTime;

    @ApiModelProperty(value = "入模温度")
    private Double intoModelTemp;

    @ApiModelProperty(value = "表里温差控制值")
    private Double tempControlValue;

    @ApiModelProperty(value = "大体积混凝土监测状态 b_qual_markingroom_monitoring 0：监测中，1：监测完毕")
    private String isSupervise;


}
