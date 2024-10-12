package com.kakarote.build.labour.entity.PO;

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
 * 考勤管理
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_attendance")
@ApiModel(value="BLabourAttendance对象", description="考勤管理")
public class BLabourAttendance implements Serializable {

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

    @ApiModelProperty(value = "班组id")
    private String attendanceTeamName;

    @ApiModelProperty(value = "考勤月份")
    private String attendanceMonth;

    @ApiModelProperty(value = "打卡次数")
    private Integer attendanceClock;

    @ApiModelProperty(value = "分包单位")
    private String attendanceSubcontractor;

    @ApiModelProperty(value = "劳务工种")
    private String attendanceWorkId;

    @ApiModelProperty(value = "人数")
    private String infocollectionNumber;


}
