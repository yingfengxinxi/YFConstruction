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
 * 周界防护报警
 * </p>
 *
 * @author zy
 * @since 2021-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_block_alarm")
@ApiModel(value="SafetyBlockAlarm对象", description="周界防护报警")
public class SafetyBlockAlarm implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "边界区块id")
    private Integer boundaryBlockId;

    @ApiModelProperty(value = "边界区块防护网编号")
    private String boundaryBlockCode;

    @ApiModelProperty(value = "报警时间")
    private Date alarmTime;

    @ApiModelProperty(value = "报警内容")
    private String alarmText;

    @ApiModelProperty(value = "报警类型")
    private String alarmType;

    @ApiModelProperty(value = "处理时间")
    private Date handleTime;

    @ApiModelProperty(value = "处理人id")
    private Long handlePeopleId;

    @ApiModelProperty(value = "处理人姓名")
    private String handlePeopleName;

    @ApiModelProperty(value = "处理措施")
    private String handleMeasures;

    @ApiModelProperty(value = "处理后结论")
    private String handleConclusion;

    @ApiModelProperty(value = "批次id")
    private String batchId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.UPDATE)
    private Long updateUserId;

    private Integer tenantId;

    private Integer projectId;


}
