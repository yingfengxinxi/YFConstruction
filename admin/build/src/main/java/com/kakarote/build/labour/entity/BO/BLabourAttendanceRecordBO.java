package com.kakarote.build.labour.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
@ApiModel("劳务管理--考勤记录管理")
public class BLabourAttendanceRecordBO extends PageEntity {

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

    @ApiModelProperty(value = "数据生成方式 0：导入 1：系统自动 2：其他")
    private String dataGeneration;

    @ApiModelProperty(value = "数据展示判断  0：展示 1：隐藏")
    private String dataShowJudge;

    @ApiModelProperty(value = "班组id")
    private String attendanceRecordTeamName;

    @ApiModelProperty(value = "班组长名")
    private String teamLeaderName;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String endTime;

    /**
     * @description
     * @author wnj58 
     * @Params 
     * @updateTime 2022/5/19 14:15
     * @return 
     * @throws 
     */
    @ApiModelProperty(value = "工人类型")
    private String workRole;
}