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
 * 建设单位管理
 * </p>
 *
 * @author shz
 * @since 2022-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_construction_unit")
@ApiModel(value="LabourConstructionUnit对象", description="建设单位管理")
public class LabourConstructionUnit implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "企业统一社会信用代码，如果无统一社会信用代码，则用组织机构代码")
    private String corpCode;

    @ApiModelProperty(value = "企业名称")
    private String corpName;

    @ApiModelProperty(value = "单位性质")
    private String corpType;

    private String licenseNum;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "地市")
    private String city;

    @ApiModelProperty(value = "区县")
    private String district;

    @ApiModelProperty(value = "企业营业地址")
    private String address;

    @ApiModelProperty(value = "邮政编码")
    private String zipCode;

    @ApiModelProperty(value = "法定代表人姓名")
    private String legalMan;

    @ApiModelProperty(value = "法定代表人职务")
    private String legalManDuty;

    @ApiModelProperty(value = "法定代表人职称")
    private String legaManProTitle;

    @ApiModelProperty(value = "法定代表人证件类型。参考人员证件类型字典表")
    private String legalManIdCardType;

    @ApiModelProperty(value = "法定代表人证件号码。需要使用AES加密")
    private String legalManIdCardNumber;

    @ApiModelProperty(value = "注册资本（万元）")
    private String regCapital;

    @ApiModelProperty(value = "实收资本(万元)")
    private String factRegCapital;

    @ApiModelProperty(value = "注册资本币种。参考币种字典表")
    private String capitalCurrencyType;

    @ApiModelProperty(value = "注册日期")
    private Date registerDate;

    @ApiModelProperty(value = "成立日期")
    private Date establishDate;

    @ApiModelProperty(value = "办公电话")
    private String officePhone;

    @ApiModelProperty(value = "传真号码")
    private String faxNumber;

    @ApiModelProperty(value = "联系人姓名")
    private String linkman;

    @ApiModelProperty(value = "联系人办公电话")
    private String linkTel;

    @ApiModelProperty(value = "企业联系邮箱")
    private String email;

    @ApiModelProperty(value = "企业网址")
    private String website;

    @ApiModelProperty(value = "企业备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "企业注册地区编码")
    private String areaCode;

    @ApiModelProperty(value = "数据来源(区级区划编码)")
    private String sourceAreaCode;
}
