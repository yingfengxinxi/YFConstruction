package com.kakarote.build.jg.entity.PO;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 安全教育培训记录
 * </p>
 *
 * @author wnj
 * @since 2021-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_jg_education_train_record")
@ApiModel(value="JgEducationTrainRecord对象", description="教育培训记录")
public class JgEducationTrainRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "项目编号")
    private String projectCode;

    @ApiModelProperty(value = "培训日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date trainDate;

    @ApiModelProperty(value = "培训时长")
    private Integer trainDuration;

    @ApiModelProperty(value = "培训名称")
    private String trainName;

    @ApiModelProperty(value = "培训类型")
    private String trainType;

    @ApiModelProperty(value = "培训人")
    private String trainPeople;

    @ApiModelProperty(value = "培训机构")
    private String trainInstitutions;

    @ApiModelProperty(value = "培训地址")
    private String trainAddress;

    @ApiModelProperty(value = "培训简述")
    private String trainDescribe;

    @ApiModelProperty(value = "附件")
    private String batchId;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;


}
