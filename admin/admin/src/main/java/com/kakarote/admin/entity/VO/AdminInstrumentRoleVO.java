package com.kakarote.admin.entity.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 仪表盘权限表
 * </p>
 *
 * @author author
 * @since 2020-12-08
 */
@Data
@ApiModel(value="AdminInstrumentRole对象", description="仪表盘权限表")
public class AdminInstrumentRoleVO {

    @ApiModelProperty(value = "角色id")
    private Integer roleId;

    @ApiModelProperty(value = "模块id 1、合同金额目标及完成情况 2、数据汇总 4、业绩指标完成率 5、销售漏斗 6、遗忘提醒 7、排行榜")
    private Integer modelId;

    @ApiModelProperty(value = "仪表盘名称 1、合同金额目标及完成情况 2、数据汇总 4、业绩指标完成率 5、销售漏斗 6、遗忘提醒 7、排行榜")
    private String modelName;

    @ApiModelProperty
    private String icon;
    @ApiModelProperty
    private String iconColor;
    @ApiModelProperty
    private String img;

    @ApiModelProperty(value = "数据权限 1、本人，2、本人及下属，3、本部门，4、本部门及下属部门，5、全部 ")
    private Integer dataType;

    @ApiModelProperty(value = "列 1左侧 2右侧")
    private Integer list;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否隐藏 0显示 1隐藏")
    private Integer isHidden;

}
