package com.kakarote.core.feign.build.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 劳务管理--信息采集
 * </p>
 *
 * @author zy
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_infocollection")
@ApiModel(value="LabourInfocollection对象", description="劳务管理--信息采集")
public class LabourInfocollection implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "信息采集主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "民族")
    private String nation;

    @ApiModelProperty(value = "身份证头像Url")
    private String imgCardUrl;

    @ApiModelProperty(value = "出生日期")
    private String birthday;

    @ApiModelProperty(value = "家庭住址")
    private String address;

    @ApiModelProperty(value = "发证机关")
    @TableField("Issuing_authority")
    private String issuingAuthority;

    @ApiModelProperty(value = "有效期启")
    private String dateStart;

    @ApiModelProperty(value = "有效期止")
    private String dateEnd;

    @ApiModelProperty(value = "职业健康")
    private String occupationalHealth;

    @ApiModelProperty(value = "所属工种,工种id")
    private Integer workTypeId;

    @ApiModelProperty(value = "工人类型")
    private String workRole;

    @ApiModelProperty(value = "管理岗位 工人类型为管理人员时，管理岗位为必填")
    private String manageType;

    @ApiModelProperty(value = "进场信息--是否班组长")
    private String ifTeamLeader;

    @ApiModelProperty(value = "进场信息--班组名称Id")
    private Integer teamName;

    @ApiModelProperty(value = "进场信息--分包单位Id")
    private Integer subcontractor;

    @ApiModelProperty(value = "进场信息--进场状态")
    private String enterStatus;

    @ApiModelProperty(value = "进场信息--进场日期")
    private String enterDate;

    @ApiModelProperty(value = "进场信息--文化程度")
    private String educationLevel;

    @ApiModelProperty(value = "进场信息--计价方式")
    private String valuationMethod;

    @ApiModelProperty(value = "进场信息--每日工资")
    private String dailyWage;

    @ApiModelProperty(value = "进场信息--政治面貌")
    private String politicalStatus;

    @ApiModelProperty(value = "进场信息--开户银行")
    private String bankAccount;

    @ApiModelProperty(value = "进场信息--发放工资银行名称")
    private String payRollBankName;

    @ApiModelProperty(value = "进场信息--发放工资卡银行联号")
    private String bankLinkNumber;

    @ApiModelProperty(value = "进场信息--银行卡号")
    private String bankCardNumber;

    @ApiModelProperty(value = "进场信息--邮政编码")
    private String postalCode;

    @ApiModelProperty(value = "进场信息--联系电话")
    private String phone;

    @ApiModelProperty(value = "进场信息--考勤卡号")
    private String attendanceNumber;

    @ApiModelProperty(value = "进场信息--发卡时间")
    private String issueCardDate;

    @ApiModelProperty(value = "进场信息--是否购买工伤或意外伤害保险")
    private String hasBuyInsurance;

    @ApiModelProperty(value = "进场信息--加入公会时间")
    private String joinedTime;

    @ApiModelProperty(value = "进场信息--特长")
    private String specialty;

    @ApiModelProperty(value = "进场信息--是否有重大病史")
    private String hasBadMedicalHistory ;

    @ApiModelProperty(value = "进场信息--紧急联系人姓名")
    private String urgentLinkMan;

    @ApiModelProperty(value = "进场信息--紧急联系方式")
    private String urgentLinkManPhone;

    @ApiModelProperty(value = "进场信息--婚姻状况")
    private String maritalStatus;

    @ApiModelProperty(value = "进场信息--是否签订劳动合同")
    private String laborContract;

    @ApiModelProperty(value = "进场信息--是否上传政府平台")
    private String uploadGovernmentPlatform;

    @ApiModelProperty(value = "合同信息--合同期限类型")
    private String contractPeriodType;

    @ApiModelProperty(value = "合同信息--签订日期")
    private String signingDate;

    @ApiModelProperty(value = "合同信息--生效日期")
    private String effectiveDate;

    @ApiModelProperty(value = "合同信息--失效日期")
    private String expirationDate;

    @ApiModelProperty(value = "合同信息--合同附件 批次id")
    private String attachments;

    @ApiModelProperty(value = "是否是零工，默认不是：0")
    private String ifOddjob;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "批次 比如图片批次")
    private String batchId;

    @ApiModelProperty(value = "工号")
    private String jobNumber;

    @ApiModelProperty(value = "评价")
    private String evaluation;

    @ApiModelProperty(value = "工人工种字典")
    private String workTypeDict;

    @ApiModelProperty(value = "工龄")
    private String workAge;

    @TableField(exist = false)
    @ApiModelProperty(value="劳务工种文字描述")
    private String workTypeName;

    @TableField(exist = false)
    @ApiModelProperty(value="班组名文字描述")
    private String teamNameName;

    @TableField(exist = false)
    @ApiModelProperty(value="分包商文字描述")
    private String realNameName;

    @ApiModelProperty(value="创建时间")
    private Date createTime;

    @ApiModelProperty(value="修改时间")
    private Date updateTime;
}
