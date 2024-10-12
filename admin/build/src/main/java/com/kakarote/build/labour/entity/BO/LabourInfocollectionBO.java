package com.kakarote.build.labour.entity.BO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 劳务管理--信息采集
 * </p>
 *
 * @author zy
 * @since 2021-03-17
 */
@Data
@ApiModel("劳务管理--信息采集")
public class LabourInfocollectionBO extends PageEntity {

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
    private String issuingAuthority;

    @ApiModelProperty(value = "有效期启")
    private String dateStart;

    @ApiModelProperty(value = "有效期止")
    private String dateEnd;

    @ApiModelProperty(value = "所属工种，工种ID")
    private Integer workTypeId;

    @ApiModelProperty(value = "进场信息--是否班组长")
    private String ifTeamLeader;

    @ApiModelProperty(value = "进场信息--班组名称ID")
    private Integer teamName;

    @ApiModelProperty(value = "进场信息--分包单位ID")
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

    @ApiModelProperty(value = "进场信息--银行卡号")
    private String bankCardNumber;

    @ApiModelProperty(value = "进场信息--邮政编码")
    private String postalCode;

    @ApiModelProperty(value = "进场信息--联系电话")
    private String phone;

    @ApiModelProperty(value = "进场信息--考勤卡号")
    private String attendanceNumber;

    @ApiModelProperty(value = "进场信息--是否签订劳动合同")
    private String laborContract;

    @ApiModelProperty(value = "进场信息--是否上传政府平台")
    private String uploadGovernmentPlatform;

    @ApiModelProperty(value = "合同信息--签订日期")
    private String signingDate;

    @ApiModelProperty(value = "合同信息--生效日期")
    private String effectiveDate;

    @ApiModelProperty(value = "合同信息--失效日期")
    private String expirationDate;

    @ApiModelProperty(value = "是否是零工，默认不是：0")
    private String ifOddjob;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "批次 比如图片批次")
    private String batchId;

    @ApiModelProperty(value="是否特殊工种")
    private String workTypeIfspecial;

    @ApiModelProperty(value = "工人工种字典")
    private String workTypeDict;

    @ApiModelProperty(value = "工人类型")
    private String workRole;

}
