package com.kakarote.build.safety.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * 安全教育培训管理
 * </p>
 *
 * @author author
 * @since 2021-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_safety_training")
@ApiModel(value="BSafetySafetyTraining对象", description="安全教育培训管理")
public class BSafetySafetyTraining implements Serializable {

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

    @ApiModelProperty(value = "培训计划")
    private String trainingPlanId;

    @ApiModelProperty(value = "培训类型  字典表safety_training_type")
    private String trainingType;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "培训时间")
    private Date trainingTime;

    @ApiModelProperty(value = "培训地点")
    private String trainingAddress;

    @ApiModelProperty(value = "培训内容")
    private String trainingContent;

    @ApiModelProperty(value = "授课人")
    private String trainingTeaching;

    @ApiModelProperty(value = "课时")
    private String trainingTeachingTime;

    @ApiModelProperty(value = "授课地点")
    private String trainingTeachingAddress;

    @ApiModelProperty(value = "负责人")
    private String trainingPeople;


}
