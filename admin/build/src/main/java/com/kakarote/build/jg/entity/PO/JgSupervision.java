package com.kakarote.build.jg.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 现场安全监督
 * </p>
 *
 * @author wnj
 * @since 2021-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_jg_supervision")
@ApiModel(value="JgSupervision对象", description="现场安全监督")
public class JgSupervision implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "项目名称")
    private Integer projectId;

    @ApiModelProperty(value = "项目地址")
    private String projectAddress;

    @ApiModelProperty(value = "巡查时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date patrolTime;

    @ApiModelProperty(value = "巡查内容")
    private String patrolContent;

    @ApiModelProperty(value = "巡查综述")
    private String patrolOverview;

    @ApiModelProperty(value = "处理意见")
    private String handleOpinion;

    @ApiModelProperty(value = "巡查人")
    private String patrolPeople;

    @ApiModelProperty(value = "陪同巡查人")
    private String patrolPeopleAccom;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "附件")
    private String batchId;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;


}
