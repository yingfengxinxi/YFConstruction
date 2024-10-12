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
 * 应急事件处置明细
 * </p>
 *
 * @author zy
 * @since 2021-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_emergency_handling_detail")
@ApiModel(value="SafetyEmergencyHandlingDetail对象", description="应急事件处置明细")
public class SafetyEmergencyHandlingDetail implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "应急事件处置明细id")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "主表id")
    private String pid;

    @ApiModelProperty(value = "处置类型")
    private String handleType;

    @ApiModelProperty(value = "情况说明")
    private String informationNote;

    @ApiModelProperty(value = "处置措施")
    private String handleMeasures;

    @ApiModelProperty(value = "处置人")
    private String handleUserName;

    @ApiModelProperty(value = "处置时间")
    private Date handleTime;

    @ApiModelProperty(value = "批次id")
    private String batchId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.UPDATE)
    private Integer updateUserId;

    private Integer tenantId;

    private Integer projectId;


}
