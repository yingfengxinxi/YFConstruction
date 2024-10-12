package com.kakarote.build.jg.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lzy
 * @since 2021-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_jg_sampling_concrete_process")
@ApiModel(value="JgSamplingConcreteProcess对象", description="")
public class JgSamplingConcreteProcess implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "混凝土取样 batchId")
    private String samplingConcreteId;

    @ApiModelProperty(value = "检验项目")
    private String checkItem;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "性能指标")
    private String performanceIndex;

    @ApiModelProperty(value = "检验结果")
    private String checkResult;

    @ApiModelProperty(value = "单项结论")
    private String signConclusion;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private Integer tenantId;

    private Integer projectId;


}
