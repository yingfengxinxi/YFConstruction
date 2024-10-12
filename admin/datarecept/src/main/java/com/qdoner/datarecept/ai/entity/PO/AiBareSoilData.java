package com.qdoner.datarecept.ai.entity.PO;

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
 * 裸土报警数据
 * </p>
 *
 * @author lzy
 * @since 2022-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_ai_bare_soil_data")
@ApiModel(value="AiBareSoilData对象", description="裸土报警数据")
public class AiBareSoilData implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "报警位置")
    private String alarmLocation;

    @ApiModelProperty(value = "报警类型 字典项 b_bare_soil_alarm_type")
    private String alarmType;

    @ApiModelProperty(value = "报警内容")
    private String alarmContent;

    @ApiModelProperty(value = "报警时间")
    private Date alarmTime;

    @ApiModelProperty(value = "报警图片")
    private String alarmImg;

    @ApiModelProperty(value = "报警视频")
    private String alarmVideo;

    @ApiModelProperty(value = "第三方数据唯一编号")
    private String onlyNumber;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private Integer tenantId;

    @ApiModelProperty(value = "项目名称")
    @TableField(exist = false)
    private String projectName;


}
