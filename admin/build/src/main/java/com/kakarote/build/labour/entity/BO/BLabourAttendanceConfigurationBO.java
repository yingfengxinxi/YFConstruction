package com.kakarote.build.labour.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel("劳务管理--考勤管理")
public class BLabourAttendanceConfigurationBO extends PageEntity {

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

    @ApiModelProperty(value = "星期记录 0：无任何类型（设定类型为1或2的时候该处默认为0）1：周一 2：周二 3：周三 4：周四 5：周五 6：周六 7：周日")
    private String weekData;

    @ApiModelProperty(value = "内容说明 工作日默认填入星期记录对应的内容，非工作日设定时为手动填入内容")
    private String configurationContent;

    @ApiModelProperty(value = "上班日期   设定类型为0时该处默认为空")
    private Date goWorkDate;

    @ApiModelProperty(value = "上班时间")
    private String goWorkTime;

    @ApiModelProperty(value = "下班日期   设定类型为0时该处默认为空")
    private Date afterWorkDate;

    @ApiModelProperty(value = "下班时间")
    private String afterWorkTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "设定类型 0：工作日设定 1：节假日设定 2：特殊日期设定")
    private String configurationType;

    @ApiModelProperty(value = "特殊日期类型 0：无任何类型（设定类型为0或1的时候该处默认为0）1：必须打卡日期 2：不用打卡日期")
    private String specialType;


}