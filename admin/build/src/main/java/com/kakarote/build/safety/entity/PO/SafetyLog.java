package com.kakarote.build.safety.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 安全日志
 * </p>
 *
 * @author wudw
 * @since 2021-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_log")
@ApiModel(value="SafetyLog对象", description="安全日志")
public class SafetyLog implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    private Integer tenantId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @ApiModelProperty(value = "施工日期")
    private Date constructionDate;

    @ApiModelProperty(value = "累计施工天数")
    private Integer constructionDays;

    @ApiModelProperty(value = "天气情况")
    private String weatherCondition;

    @ApiModelProperty(value = "最高温度")
    private Double maxTemperature;

    @ApiModelProperty(value = "最低温度")
    private Double minTemperature;

    @ApiModelProperty(value = "风向")
    private String windDirection;

    @ApiModelProperty(value = "风力")
    private String windPower;

    @ApiModelProperty(value = "施工部位")
    private String constructionSite;

    @ApiModelProperty(value = "施工现场管理情况")
    private String management;

    @ApiModelProperty(value = "防范情况")
    private String prevention;

    @ApiModelProperty(value = "监理记录")
    private String supervisionRecord;

    @ApiModelProperty(value = "管理人员签字")
    private String signUrl;

    @TableField(exist = false)
    @ApiModelProperty(value = "填报人")
    private String userName;

}
