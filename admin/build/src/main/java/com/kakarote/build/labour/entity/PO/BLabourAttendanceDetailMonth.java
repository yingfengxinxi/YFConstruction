package com.kakarote.build.labour.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 考勤明细管理
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
@Data
@ApiModel("劳务管理--考勤记录管理")
public class BLabourAttendanceDetailMonth implements Serializable {

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

    @ApiModelProperty(value = "考勤主表id   b_labour_attendance 的主键")
    private String attendanceId;

    @ApiModelProperty(value = "人员id  b_labour_infocollection的主键")
    private String infocollectionId;

    @ApiModelProperty(value = "人员姓名")
    private String infocollectionName;

    @ApiModelProperty(value = "身份证号")
    private String infocollectionCard;

    @ApiModelProperty(value = "考勤卡号")
    private String attendanceNumber;

    @ApiModelProperty(value = "考勤日期")
    private Date attendanceDate;

    @ApiModelProperty(value = "是否节假日 默认周六、周日都是1 0：不是节假日 1：是节假日")
    private String infoHoliday;

    @ApiModelProperty(value = "是否异常 当日打卡数据次数小于考勤管理次数的填入 0：非异常、1：异常")
    private String infoAbnormal;

    @ApiModelProperty(value = "是否出勤 为节假日日期的默认为0，至少打过一次卡的为1 0：未出勤  1：已出勤")
    private String infoAttendance;

    @ApiModelProperty(value = "数据统计判断  0：计入 1：不计入")
    private String dataStatisticalJudge;

    @ApiModelProperty(value = "zs")
    private int zs;

    @ApiModelProperty(value = "ri01")
    private int ri01;

    @ApiModelProperty(value = "ri02")
    private int ri02;

    @ApiModelProperty(value = "ri03")
    private int ri03;

    @ApiModelProperty(value = "ri04")
    private int ri04;

    @ApiModelProperty(value = "ri05")
    private int ri05;

    @ApiModelProperty(value = "ri06")
    private int ri06;

    @ApiModelProperty(value = "ri07")
    private int ri07;

    @ApiModelProperty(value = "ri08")
    private int ri08;

    @ApiModelProperty(value = "ri09")
    private int ri09;

    @ApiModelProperty(value = "ri10")
    private int ri10;

    @ApiModelProperty(value = "ri11")
    private int ri11;

    @ApiModelProperty(value = "ri12")
    private int ri12;

    @ApiModelProperty(value = "ri13")
    private int ri13;

    @ApiModelProperty(value = "ri14")
    private int ri14;

    @ApiModelProperty(value = "ri15")
    private int ri15;

    @ApiModelProperty(value = "ri16")
    private int ri16;

    @ApiModelProperty(value = "ri17")
    private int ri17;

    @ApiModelProperty(value = "ri18")
    private int ri18;

    @ApiModelProperty(value = "ri19")
    private int ri19;

    @ApiModelProperty(value = "ri20")
    private int ri20;

    @ApiModelProperty(value = "ri21")
    private int ri21;

    @ApiModelProperty(value = "ri22")
    private int ri22;

    @ApiModelProperty(value = "ri23")
    private int ri23;

    @ApiModelProperty(value = "ri24")
    private int ri24;

    @ApiModelProperty(value = "ri25")
    private int ri25;

    @ApiModelProperty(value = "ri26")
    private int ri26;

    @ApiModelProperty(value = "ri27")
    private int ri27;

    @ApiModelProperty(value = "ri28")
    private int ri28;

    @ApiModelProperty(value = "ri29")
    private int ri29;

    @ApiModelProperty(value = "ri30")
    private int ri30;

    @ApiModelProperty(value = "ri31")
    private int ri31;



}
