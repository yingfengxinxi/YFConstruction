package com.kakarote.build.qual.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zjc
 * @since 2022-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="大体积混凝土基础数据", description="")
public class BQualConcreteBO extends PageEntity {

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
