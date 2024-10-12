package com.kakarote.build.safety.entity.PO;

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
 * 施工安全管理附件
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_safety_emergency_file")
@ApiModel(value="BSafetySafetyEmergencyFile对象", description="施工安全管理附件")
public class BSafetySafetyEmergencyFile implements Serializable {

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

    @ApiModelProperty(value = "主数据id")
    private Integer pid;

    @ApiModelProperty(value = "附件名称")
    private String safetyEmergencyFileName;

    @ApiModelProperty(value = "附件后缀")
    private String safetyEmergencyFileSuffix;

    @ApiModelProperty(value = "附件地址")
    private String safetyEmergencyFileAddress;

    @ApiModelProperty(value = "附件类型 0 安全应急管理附件 1 安全资料管理附件")
    private String safetyEmergencyFileType;

    @ApiModelProperty(value = "备注")
    private String safetyEmergencyFileRemarks;


}
