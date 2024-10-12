package com.kakarote.build.labour.entity.PO;

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
 * 企业报验
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_inspection")
@ApiModel(value="BLabourInspection对象", description="企业报验")
public class BLabourInspection implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateBy;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "验收阶段   b_inspection_phase 验收阶段字典")
    private String inspectionPhase;

    @ApiModelProperty(value = "申请说明")
    private String inspectionContent;

    @ApiModelProperty(value = "附件id")
    private String batchId;

    @ApiModelProperty(value = "申请状态   b_inspection_type 申请状态字典")
    private String inspectionType;

    @ApiModelProperty(value = "是否整改   后台判断当前数据是否需要整改 0否 1是   默认 0")
    private String rectificationInfo;

    @ApiModelProperty(value = "备注")
    private String remarks;


}
