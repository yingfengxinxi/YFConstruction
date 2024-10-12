package com.kakarote.build.labour.entity.PO;

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
import org.checkerframework.checker.formatter.qual.Format;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 零工签证
 * </p>
 *
 * @author zy
 * @since 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_odd_visa")
@ApiModel(value="LabourOddVisa对象", description="零工签证")
public class LabourOddVisa implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "记录人姓名")
    private String recordPeopleName;

    @ApiModelProperty(value = "记录人ID")
    private Long recordPeopleId;

    @ApiModelProperty(value = "派工时间")
    private Date dispatchTime;

    @ApiModelProperty(value = "派工原因")
    private String dispatchReason;

    @ApiModelProperty(value = "派工单位id")
    private Integer dispatchUnitId;

    @ApiModelProperty(value = "派工单位名称")
    private String dispatchUnitName;

    @ApiModelProperty(value = "派工班组id")
    private Integer dispatchTeamId;

    @ApiModelProperty(value = "派工班组名称")
    private String dispatchTeamName;

    @ApiModelProperty(value = "派工人员id")
    private Integer dispatchPeopleId;

    @ApiModelProperty(value = "派工人员姓名")
    private String dispatchPeopleName;

    @ApiModelProperty(value = "派工内容")
    private String dispatchText;

    @ApiModelProperty(value = "签证部门id")
    private Long visaDepartmentId;

    @ApiModelProperty(value = "签证部门名称")
    private String visaDepartmentName;

    @ApiModelProperty(value = "责任人id")
    private Long responsibleId;

    @ApiModelProperty(value = "责任人姓名")
    private String responsibleName;

    @ApiModelProperty(value = "作业开始时间")
    private Date workTimeStart;

    @ApiModelProperty(value = "作业结束时间")
    private Date workTimeEnd;

    @ApiModelProperty(value = "批次ID")
    private String batchId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.UPDATE)
    private Long updateUserId;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;


}
