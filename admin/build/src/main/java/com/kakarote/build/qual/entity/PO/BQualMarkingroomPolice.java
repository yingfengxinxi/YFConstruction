package com.kakarote.build.qual.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 标养室报警信息
 * </p>
 *
 * @author zhang
 * @since 2022-07-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_markingroom_police")
@ApiModel(value="BQualMarkingroomPolice对象", description="标养室报警信息")
public class BQualMarkingroomPolice implements Serializable {

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
