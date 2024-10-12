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
 * 疫情进出场统计
 * </p>
 *
 * @author zhang
 * @since 2022-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_attendance_epidemic")
@ApiModel(value="BLabourAttendanceEpidemic对象", description="疫情进出场统计")
public class BLabourAttendanceEpidemic implements Serializable {

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

    @ApiModelProperty(value = "打卡方向 0：进场  1：出场")
    private String clockDirection;

    @ApiModelProperty(value = "打卡时间")
    private Date clockTime;

    @ApiModelProperty(value = "健康码状态 b_health_code  0:绿色  1：黄色  2：红色")
    private String healthCode;

    @ApiModelProperty(value = "核酸检测时间")
    private Date nucleicAcidTime;

    @ApiModelProperty(value = "进出场体温")
    private String bodyTemperature;

    @ApiModelProperty(value = "备注")
    private String remarks;


}
