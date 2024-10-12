package com.kakarote.build.labour.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.poi.hpsf.Decimal;

/**
 * <p>
 * 考勤记录管理
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_attendance_record")
@ApiModel(value="BLabourAttendanceRecord对象", description="考勤记录管理")
public class BLabourAttendanceRecord implements Serializable {



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

    @ApiModelProperty(value = "人员id  b_labour_infocollection的主键")
    private String infocollectionId;

    @ApiModelProperty(value = "人员姓名")
    private String infocollectionName;

    @ApiModelProperty(value = "身份证号")
    private String infocollectionCard;

    @ApiModelProperty(value = "考勤卡号")
    private String attendanceNumber;

    @ApiModelProperty(value = "打卡方向 0：进场  1：出场")
    private String clockDirection;

    @ApiModelProperty(value = "打卡时间")
    private Date clockTime;

    @ApiModelProperty(value = "体温")
    private String bodyTemperature;

    @ApiModelProperty(value = "酒精含量")
    private String alcoholicity;

    @ApiModelProperty(value = "血压")
    private String bloodPressure;

    @ApiModelProperty(value = "血氧")
    private String bloodOxygen;

    @ApiModelProperty(value = "数据生成方式 0：导入 1：系统自动 2：其他")
    private String dataGeneration;

    @ApiModelProperty(value = "数据展示判断  0：展示 1：隐藏")
    private String dataShowJudge;

    @ApiModelProperty(value = "班组id")
    private String attendanceRecordTeamName;

    @ApiModelProperty(value = "证件类型")
    private String idCardType;

    @ApiModelProperty(value = "刷卡近照")
    private String image;

    @ApiModelProperty(value = "通道的名称")
    private String channel;

    @ApiModelProperty(value = "通行方式")
    private String attendType;

    @ApiModelProperty(value = "WGS84经度")
    private BigDecimal lng;

    @ApiModelProperty(value = "WGS84纬度")
    private BigDecimal lat;

    @ApiModelProperty(value = "人员性别")
    @TableField(exist = false)
    private String sex;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    @TableField(exist = false)
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    @TableField(exist = false)
    private Date endDate;
}
