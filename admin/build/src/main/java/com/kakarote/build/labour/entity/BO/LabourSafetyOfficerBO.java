package com.kakarote.build.labour.entity.BO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 证件管理--安全从业人员证件台账
 * </p>
 *
 * @author wnj
 * @since 2021-11-10
 */
@Data
@ApiModel("证件管理--安全从业人员证件台账")
public class LabourSafetyOfficerBO extends PageEntity {

    @ApiModelProperty(value = "采集信息主键")
    private Integer infocolId;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "人员名称")
    private String name;

    @ApiModelProperty(value = "工龄")
    private Integer workAge;

    @ApiModelProperty(value = "工种信息主键")
    private String workTypeId;

    @ApiModelProperty(value = "证书编号")
    private String certificateNo;

    @ApiModelProperty(value = "认证时间")
    private Date authTime;

    @ApiModelProperty(value = "认证状态 0 未认证 1 已认证")
    private String authStatus;

    @ApiModelProperty(value = "安全证书类型种类")
    private String cerCategory;

    @ApiModelProperty(value = "安全证书类型")
    private String certificateType;

    @ApiModelProperty(value = "安全证书名称")
    private String certificateName;

    @ApiModelProperty(value = "安全证书型号")
    private String certificateModel;

    @ApiModelProperty(value = "发证机关")
    private String issuingAuthority;

    @ApiModelProperty(value = "发证日期(证书有效期开始时间)")
    private Date startTime;

    @ApiModelProperty(value = "证书有效期结束时间")
    private Date endTime;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;


}
