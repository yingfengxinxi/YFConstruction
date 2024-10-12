package com.kakarote.admin.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 仪表盘名称表
 * </p>
 *
 * @author author
 * @since 2020-12-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_admin_instrument_info")
@ApiModel(value="AdminInstrumentInfo对象", description="仪表盘名称表")
public class AdminInstrumentInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "仪表盘id")
    private Integer modelId;

    @ApiModelProperty(value = "仪表盘名称 1、合同金额目标及完成情况 2、数据汇总 4、业绩指标完成率 5、销售漏斗 6、遗忘提醒 7、排行榜")
    private String modelName;

    @ApiModelProperty
    private String icon;
    @ApiModelProperty
    private String iconColor;
    @ApiModelProperty
    private String img;

    @ApiModelProperty(value = "列 1左侧 2右侧")
    private Integer list;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否隐藏 0显示 1隐藏")
    private Integer isHidden;

    @TableField(select = false)
    @ApiModelProperty(value = "租户ID")
    private Integer tenantId;


}
