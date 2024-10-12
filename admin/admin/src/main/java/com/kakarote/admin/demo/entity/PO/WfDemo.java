package com.kakarote.admin.demo.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
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
 * DEMO表
 * </p>
 *
 * @author wudw
 * @since 2021-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("qdoner_wf_demo")
@ApiModel(value="WfDemo对象", description="DEMO表")
public class WfDemo implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "编号")
    private String number;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "负责人ID")
    private Long ownerUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交")
    private Integer checkStatus;

    @ApiModelProperty(value = "审核记录ID")
    private Integer examineRecordId;

    @ApiModelProperty(value = "批次")
    private String batchId;

    private Integer tenantId;

    private String projectId;

    //Copy 审批
    @TableField(exist = false)
    WfVO wfVO;
}
