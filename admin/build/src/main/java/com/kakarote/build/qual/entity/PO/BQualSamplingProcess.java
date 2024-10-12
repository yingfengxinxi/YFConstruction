package com.kakarote.build.qual.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 见证取样过程表
 * </p>
 *
 * @author author
 * @since 2021-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_sampling_process")
@ApiModel(value="BQualSamplingProcess对象", description="见证取样过程表")
public class BQualSamplingProcess implements Serializable {

    private static final long serialVersionUID=1L;

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

    @ApiModelProperty(value = "取样主表ID")
    private String samplingId;

    @ApiModelProperty(value = "过程类型（字典项：复试取样、见证送检、试验检测、结果认证、不合格反馈）")
    private String processType;

    @ApiModelProperty(value = "情况说明")
    private String description;

    @ApiModelProperty(value = "操作时间(默认当前时间)")
    private Date operationTime;

    @ApiModelProperty(value = "操作人(默认当前人员)")
    private String operationUser;

    @ApiModelProperty(value = "附件id")
    private String batchId;


}
