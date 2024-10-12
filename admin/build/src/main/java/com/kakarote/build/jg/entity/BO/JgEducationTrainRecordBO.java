package com.kakarote.build.jg.entity.BO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 安全教育培训记录
 * </p>
 *
 * @author wnj
 * @since 2021-11-23
 */
@Data
@ApiModel(value = "教育培训记录")
public class JgEducationTrainRecordBO extends PageEntity {

    @ApiModelProperty(value = "项目名称")
    private Integer projectId;

    @ApiModelProperty(value = "项目编号")
    private String projectCode;

    @ApiModelProperty(value = "培训日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date trainDate;

    @ApiModelProperty(value = "培训开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date trainStartDate;

    @ApiModelProperty(value = "培训结束日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date trainEndDate;

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

}
