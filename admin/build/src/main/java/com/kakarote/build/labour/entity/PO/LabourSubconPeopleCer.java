package com.kakarote.build.labour.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 证件管理---分包管理人员证件台账
 * </p>
 *
 * @author zy
 * @since 2021-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_subcon_people_cer")
@ApiModel(value="LabourSubconPeopleCer对象", description="证件管理---分包管理人员证件台账")
public class LabourSubconPeopleCer implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "创建用户id")
    private Integer createUserid;

    @ApiModelProperty(value = "创建用户name")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改用户id")
    private Integer updateUserid;

    @ApiModelProperty(value = "修改用户name")
    private String updateUsername;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "关键岗位证照批次id")
    private String batchId;

//    @ApiModelProperty(value = "租户")
//    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;


}
