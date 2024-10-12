package com.kakarote.build.labour.entity.PO;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 劳务管理--分包商
 * </p>
 *
 * @author lzy
 * @since 2021-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_subcontractor")
@ApiModel(value="LabourSubcontractor对象", description="劳务管理--分包商")
public class LabourSubcontractor implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "分包商管理主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "分包商名称")
    private String realName;

    @ApiModelProperty(value = "分包商简称")
    private String shortName;

    @ApiModelProperty(value = "分包商类型")
    private String subcontractorType;

    @ApiModelProperty(value = "所属省")
    private Integer province;

    @ApiModelProperty(value = "所属市")
    private Integer city;

    @ApiModelProperty(value = "所属区")
    private Integer district;

    @ApiModelProperty(value = "公司类型/参建单位类型")
    private String corpType;

    @ApiModelProperty(value = "分包资质等级")
    private String overallMerit;

    @ApiModelProperty(value = "单位地址")
    private String unitAddress;

    @ApiModelProperty(value = "法人")
    private String legalRepresentative;

    @ApiModelProperty(value = "法人联系电话")
    private String legalRepresentativePhone;

    @ApiModelProperty(value = "注册资金")
    private BigDecimal registeredCapital;

    @ApiModelProperty(value = "注册时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    @ApiModelProperty(value = "联系人")
    private String principal;

    @ApiModelProperty(value = "联系电话")
    private String principaltel;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "注册地址住建部投诉电话")
    private String buildComplaintCall;

    @ApiModelProperty(value = "注册地人社部投诉电话")
    private String societyComplaintCall;

    @ApiModelProperty(value = "资质")
    private String subject;

    @ApiModelProperty(value = "是否生效 0:否; 1:是")
    private String isConformity;

    @ApiModelProperty(value = "公司简称首拼")
    private String nameSpell;

    @ApiModelProperty(value = "社会信用代码")
    private String corpCode;

    @ApiModelProperty(value = "公司项目经验")
    private String companyScore;

    @ApiModelProperty(value = "公司简介")
    private String companySummary;

    @ApiModelProperty(value = "项目Id")
    private Integer projectId;

    @ApiModelProperty(value = "租户Id")
    private Integer tenantId;


}
