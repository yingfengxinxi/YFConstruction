package com.kakarote.build.qual.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author pz
 * @since 2021-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="分部分项管理", description="")
public class BQualMarkingroomPoliceBO extends PageEntity {

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

    @ApiModelProperty(value = "标养室id")
    private String curingRoomId;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "报警类型  b_qual_markingroom_police_type  humidity：湿度 ，temp：温度")
    private String alarmType;

    @ApiModelProperty(value = "报警类型描述")
    private String alarmTypeDes;

    @ApiModelProperty(value = "报警值")
    private Double alarmValue;

    @ApiModelProperty(value = "恢复值")
    private Double recoverValue;

    @ApiModelProperty(value = "数据产生时间")
    private Date eventTime;

    @ApiModelProperty(value = "报警结束时间")
    private Date endAlarmTime;

    @ApiModelProperty(value = "提示信息")
    private String notification;

}
