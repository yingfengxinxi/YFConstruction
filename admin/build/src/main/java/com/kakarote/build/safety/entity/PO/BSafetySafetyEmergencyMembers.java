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
 * 应急班组成员管理
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_safety_emergency_members")
@ApiModel(value="BSafetySafetyEmergencyMembers对象", description="应急班组成员管理")
public class BSafetySafetyEmergencyMembers implements Serializable {

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

    @ApiModelProperty(value = "安全应急管理id   b_safety_safety_emergency    安全应急管理主键")
    private String pid;

    @ApiModelProperty(value = "人员名称")
    private String membersName;

    @ApiModelProperty(value = "职能")
    private String membersFunction;

    @ApiModelProperty(value = "联系电话")
    private String membersTel;

    @ApiModelProperty(value = "备注")
    private String membersRemarks;


}
