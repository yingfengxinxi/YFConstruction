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
 * 无人机现场巡检
 * </p>
 *
 * @author author
 * @since 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_drone")
@ApiModel(value="BLabourDrone对象", description="无人机现场巡检")
public class BLabourDrone implements Serializable {

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

    @ApiModelProperty(value = "巡检时间")
    private Date inspectionTime;

    @ApiModelProperty(value = "无人机编号")
    private String uavNumber;

    @ApiModelProperty(value = "无人机型号")
    private String uavModel;

    @ApiModelProperty(value = "无人机操作人员")
    private String uavPperators;

    @ApiModelProperty(value = "无人机操作证号")
    private String uavLicenseNumber;

    @ApiModelProperty(value = "巡检影像")
    private String inspectionImage;

    @ApiModelProperty(value = "备注")
    private String remarks;


}
