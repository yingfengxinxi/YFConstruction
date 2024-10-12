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
 * 见证取样表
 * </p>
 *
 * @author author
 * @since 2021-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_sampling_witness")
@ApiModel(value="BQualSamplingWitness对象", description="见证取样表")
public class BQualSamplingWitness implements Serializable {

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

    @ApiModelProperty(value = "取样人")
    private String samplers;

    @ApiModelProperty(value = "取样单位")
    private String samplingUnit;

    @ApiModelProperty(value = "见证人")
    private String witness;

    @ApiModelProperty(value = "见证单位")
    private String witnessUnit;

    @ApiModelProperty(value = "取样日期")
    private Date samplingDate;

    @ApiModelProperty(value = "试件类型（字典项：水泥、钢筋、防水材料、混凝土外加剂）")
    private String specimenType;

    @ApiModelProperty(value = "试件详细信息")
    private String specimenDetails;

    @ApiModelProperty(value = "附件id")
    private String batchId;

    @ApiModelProperty(value = "委托人")
    private String client;

    @ApiModelProperty(value = "委托单位")
    private String clientUnit;

    @ApiModelProperty(value = "检测单位")
    private String testingUnit;

    @ApiModelProperty(value = "接受委托人")
    private String acceptingClient;

    @ApiModelProperty(value = "接受委托人")
    private String clientBatchId;

    @ApiModelProperty(value = "委托附件id")
    private Date clientDate;


}
