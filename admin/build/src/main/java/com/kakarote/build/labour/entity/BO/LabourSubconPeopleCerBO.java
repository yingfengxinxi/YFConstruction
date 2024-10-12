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
 * 证件管理---分包管理人员证件台账
 * </p>
 *
 * @author zy
 * @since 2021-03-26
 */
@Data
@ApiModel("证件管理---分包管理人员证件台账")
public class LabourSubconPeopleCerBO extends PageEntity {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "分包单位id")
    private Integer sid;

    @ApiModelProperty(value = "隶属人员id")
    private Integer pid;

    @ApiModelProperty(value = "人员类型")
    private String peopleType;

    @ApiModelProperty(value = "岗位证书名称")
    private String cerName;

    @ApiModelProperty(value = "岗位/职位")
    private String postName;

    @ApiModelProperty(value = "归档日期")
    private String endDate;

    @ApiModelProperty(value = "证书编号")
    private String cerCode;

    @ApiModelProperty(value = "有效日期")
    private String endEffectiveDate;


    @ApiModelProperty(value = "创建用户id")
    private Integer createUserid;

    @ApiModelProperty(value = "创建用户name")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


    @ApiModelProperty(value = "关键岗位证照批次id")
    private String batchId;

//    @ApiModelProperty(value = "租户")
//    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;


}
