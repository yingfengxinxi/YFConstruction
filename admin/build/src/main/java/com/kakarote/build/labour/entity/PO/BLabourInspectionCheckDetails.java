package com.kakarote.build.labour.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 企业报验检查表详细
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_inspection_check_details")
@ApiModel(value="BLabourInspectionCheckDetails对象", description="企业报验检查表详细")
public class BLabourInspectionCheckDetails implements Serializable {

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

    @ApiModelProperty(value = "企业报验检查表id")
    private String checkId;

    @ApiModelProperty(value = "检查项目")
    private String checkProject;

    @ApiModelProperty(value = "检查部位")
    private String checkParts;

    @ApiModelProperty(value = "检查情况")
    private String checkSituation;

    @ApiModelProperty(value = "检查结果")
    private String checkResults;

    @ApiModelProperty(value = "处置情况")
    private String checkDisposal;

    @ApiModelProperty(value = "责任方")
    private String responsibility;

    @ApiModelProperty(value = "附件id")
    private String batchId;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "备注")
    private Integer sort;

    @ApiModelProperty(value = "上传文件数量")
    @TableField(exist = false)
    private String fileNumber;


}
