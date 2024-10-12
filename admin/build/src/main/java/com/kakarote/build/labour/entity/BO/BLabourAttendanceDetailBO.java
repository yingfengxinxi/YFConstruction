package com.kakarote.build.labour.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
@ApiModel("劳务管理--考勤明细管理")
public class BLabourAttendanceDetailBO extends PageEntity {

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


}