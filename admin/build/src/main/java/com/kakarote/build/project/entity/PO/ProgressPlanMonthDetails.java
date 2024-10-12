package com.kakarote.build.project.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.kakarote.core.feign.wf.entity.WfVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 进度管理--月计划明细
 * </p>
 *
 * @author zy
 * @since 2021-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_progress_plan_month_details")
@ApiModel(value="ProgressPlanMonthDetails对象", description="进度管理--月计划明细")
public class ProgressPlanMonthDetails implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "模板id")
    private Integer mid;

    @ApiModelProperty(value = "总计划明细表的模板明细id")
    private String planDetailMdId;

    @ApiModelProperty(value = "计划月份")
    private String planMonth;

    @ApiModelProperty(value = "序号")
    private Integer serialNumber;

    @ApiModelProperty(value = "等级")
    private Integer level;

    @ApiModelProperty(value = "任务名称")
    private String name;

    @ApiModelProperty(value = "父任务")
    private String parentId;

    @ApiModelProperty(value = "计划工期天数")
    private Integer planGqiNumber;

    @ApiModelProperty(value = "关联楼座ID")
    private Integer relatedBuildingId;

    @ApiModelProperty(value = "是否里程碑")
    private String ifMilestone;

    @ApiModelProperty(value = "是否关键路线")
    private String ifCriticalRoute;

    @ApiModelProperty(value = "前置节点")
    private String preNode;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "计划开始日期")
    private String planStartDate;

    @ApiModelProperty(value = "计划结束日期")
    private String planEndDate;

    @ApiModelProperty(value = "实际开始日期")
    private String actualStartDate;

    @ApiModelProperty(value = "实际结束日期")
    private String actualEndDate;

    @ApiModelProperty(value = "实际工期天数")
    private Integer actualGqiNumber;

    @ApiModelProperty(value = "延误天数")
    private Integer delayDays;

    @ApiModelProperty(value = "设计总量")
    private Integer designTotal;

    @ApiModelProperty(value = "计量单位")
    private String unit;

    @ApiModelProperty(value = "本月计划")
    private String currentMonthPlan;

    @ApiModelProperty(value = "月累完成")
    private String monthTired;

    @ApiModelProperty(value = "本月占比%")
    private Integer currentMonthPercent;

    @ApiModelProperty(value = "开累完成")
    private String finishTired;

    @ApiModelProperty(value = "开累占比%")
    private Integer finishPercent;

    @ApiModelProperty(value = "计划产值(万元)")
    private Integer planOutputValue;

    @ApiModelProperty(value = "实际产值(万元)")
    private Integer actualOutputValue;

    @ApiModelProperty(value = "计划完成比例%")
    private Integer planFinishPersent;

    @ApiModelProperty(value = "实际完成比例%")
    private Integer actualFinishPersent;

    @ApiModelProperty(value = "延期责任")
    private String delayDuty;

    @ApiModelProperty(value = "责任单位")
    private Integer responsibleParty;

    @ApiModelProperty(value = "延期原因")
    private String delayReason;

    @ApiModelProperty(value = "延期原因描述")
    private String delayReasonDescript;

    @ApiModelProperty(value = "填报说明")
    private String fillInDescription;

    @ApiModelProperty(value = "采取措施")
    private String measures;

    @ApiModelProperty(value = "审核记录ID")
    private Integer examineRecordId;

    @ApiModelProperty(value = "负责人ID")
    private Long ownerUserId;

    @ApiModelProperty(value = "0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交")
    private Integer checkStatus;

    @ApiModelProperty(value = "施工部位")
    private String constructionSite;

    @ApiModelProperty(value = "责任人")
    private String responsibleUser;

    @ApiModelProperty(value = "参与人")
    private String participateUser;

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

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    @TableField(exist = false)
    @ApiModelProperty(value = "前置节点名称")
    private String preNodeName;


    //Copy 审批
    @TableField(exist = false)
    WfVO wfVO;

}
