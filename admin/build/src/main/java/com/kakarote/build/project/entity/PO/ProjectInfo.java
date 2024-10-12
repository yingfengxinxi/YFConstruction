package com.kakarote.build.project.entity.PO;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import com.kakarote.core.feign.admin.entity.SimpleUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 项目信息表
 * </p>
 *
 * @author shz
 * @since 2021-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_project_info")
@ApiModel(value="ProjectInfo对象", description="项目信息表")
public class ProjectInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "施工许可证编号")
    private String constructionPermitCode;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "施工单位-统一社会信用代码")
    private String uniscid;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "地市")
    private String city;

    @ApiModelProperty(value = "区县")
    private String district;

    @ApiModelProperty(value = "项目地址")
    private String projectAddress;

    @ApiModelProperty(value = "工程状态")
    private String projectState;

    @ApiModelProperty(value = "商业状态")
    private String businessState;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "合同开工日期")
    private Date contractCommencementDate;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "合同竣工日期")
    private Date contractCompletionDate;

    @ApiModelProperty(value = "工程图片")
    private String projectImg;

    @ApiModelProperty(value = "建筑单位名称")
    private String constructionUnitName;

    @ApiModelProperty(value = "建设单位-统一社会信用代码")
    private String constructionUnitUniscid;

    @ApiModelProperty(value = "土地规划许可证")
    private String landPlanningPermitCode;

    @ApiModelProperty(value = "工程规划许可证编号")
    private String projectPlanningLicenseCode;

    @ApiModelProperty(value = "项目分类")
    private String projectClassify;

    @ApiModelProperty(value = "工程用途")
    private String engineeringPurpose;

    @ApiModelProperty(value = "建筑性质")
    private String constructionNature;

    @ApiModelProperty(value = "总造价(万元)")
    private BigDecimal totalCost;

    @ApiModelProperty(value = "总面积(平方米)")
    private Double totalArea;

    @ApiModelProperty(value = "总长度（米）")
    private Double totalLength;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际开工日期")
    private Date actualCommencementDate;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际竣工日期")
    private Date actualCompletionDate;

    @ApiModelProperty(value = "联系人姓名")
    private String contactsName;

    @ApiModelProperty(value = "联系人电话")
    private String contactsTel;

    @ApiModelProperty(value = "立项文号")
    private String projectCode;

    @ApiModelProperty(value = "立项级别")
    private String createProjectLevel;

    @ApiModelProperty(value = "建筑规模")
    private String constructionScale;

    @ApiModelProperty(value = "是否智慧工地（1是，0否）")
    private Integer isSmartBuild;

    @ApiModelProperty(value = "项目简介")
    private String projectBrief;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "经度")
    private String lng;

    @ApiModelProperty(value = "纬度")
    private String lat;

    @ApiModelProperty(value = "对公账号开户银行")
    private String corporateAccountBank;

    @ApiModelProperty(value = "对公账号")
    private String corporateAccount;

    @ApiModelProperty(value = "代发工资银行")
    private String payBank;

    @ApiModelProperty(value = "代发工资银行账号")
    private String payBankCode;

    @ApiModelProperty(value = "项目简称")
    private String projectAbbreviation;

    @ApiModelProperty(value = "所属国家")
    private String country;

    @ApiModelProperty(value = "所属区域")
    private String region;

    @ApiModelProperty(value = "工程编号")
    private String projectNo;

    @ApiModelProperty(value = "使用资质")
    private String qualification;

    @ApiModelProperty(value = "投标面积")
    private Double tenderArea;

    @ApiModelProperty(value = "土方量")
    private Double earthworkVolume;

    @ApiModelProperty(value = "项目用途")
    private String projectPurpose;

    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "结构类型")
    private String structureType;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "中标日期")
    private Date bidWinningDate;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "中标开工日期")
    private Date bidWinningStartDate;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "中标竣工日期")
    private Date bidWinningCompletionDate;

    @ApiModelProperty(value = "中标金额")
    private BigDecimal bidAmount;

    @ApiModelProperty(value = "承包模式")
    private String contractMode;

    @ApiModelProperty(value = "高大精特")
    private String tallExquisite;

    @ApiModelProperty(value = "投标利润率")
    private Double biddingProfitMargin;

    @ApiModelProperty(value = "经营模式")
    private String managementModel;

    @ApiModelProperty(value = "分包模式")
    private String subcontractingMode;

    @ApiModelProperty(value = "工程状态")
    private String engineeringStatus;

    @ApiModelProperty(value = "商务状态")
    private String businessStatus;

    @ApiModelProperty(value = "项目级别")
    private String projectLevel;

    @ApiModelProperty(value = "主要施工范围")
    private String mainConstructionScope;

    @ApiModelProperty(value = "建筑檐高")
    private Double buildingEavesHeight;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开工令日期")
    private Date commencementOrderDate;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划竣工日期")
    private Date plannedCompletionDate;

    @ApiModelProperty(value = "地上面积")
    private Double abovegroundArea;

    @ApiModelProperty(value = "地下面积")
    private Double undergroundArea;

    @ApiModelProperty(value = "总层数")
    private Integer totalFloors;

    @ApiModelProperty(value = "单位工程数量")
    private Integer unitQuantity;

    @ApiModelProperty(value = "工程类别")
    private String engineeringType;

    @ApiModelProperty(value = "隐患提醒天数")
    private Integer hiddenDangerDaysReminder;

    @ApiModelProperty(value = "隐患禁录天数	")
    private Integer hiddenDangerBanDays;

    @ApiModelProperty(value = "质量目标")
    private String qualityTarget;

    @ApiModelProperty(value = "安全目标")
    private String safetyTarget;

    @ApiModelProperty(value = "创建市优质结构")
    private String qualityStructure;

    @ApiModelProperty(value = "观感质量等级目标")
    private String qualityLevelTarget;

    @ApiModelProperty(value = "优良面积")
    private Double excellentArea;

    @ApiModelProperty(value = "项目经理")
    private String projectManager;

    @ApiModelProperty(value = "项目执行经理")
    private String projectExecutiveManager;

    @ApiModelProperty(value = "项目副经理")
    private String deputyProjectManager;

    @ApiModelProperty(value = "商务经理")
    private String businessManager;

    @ApiModelProperty(value = "项目部总工")
    private String projectDepartmentChief;

    @ApiModelProperty(value = "专项质量员")
    private String specialQualityOfficer;

    @ApiModelProperty(value = "专职安全员")
    private String fullTimeSafetyOfficer;

    @ApiModelProperty(value = "信息化")
    private String informationize;

    @ApiModelProperty(value = "材料员")
    private String materialman;

    @ApiModelProperty(value = "预算员")
    private String budgeter;

    @ApiModelProperty(value = "施工员")
    private String constructionWorkers;

    @ApiModelProperty(value = "成本会计")
    private String costAccounting;

    @ApiModelProperty(value = "劳务管理员")
    private String laborAdministrator;

    @ApiModelProperty(value = "劳务分管领导")
    private String laborLeader;

    @ApiModelProperty(value = "劳务分管领导电话")
    private String laborLeaderTel;

    @ApiModelProperty(value = "项目经理电话")
    private String projectManagerTel;

    @ApiModelProperty(value = "监理单位")
    private String supervisorCompany;

    @ApiModelProperty(value = "设计单位")
    private String designCompany;

    @ApiModelProperty(value = "勘察单位")
    @TableField("Investigation_company")
    private String investigationCompany;

    @ApiModelProperty(value = "工程所在地人社部投诉电话")
    private String societyComplaintCall;

    @ApiModelProperty(value = "工程所在地住建部投诉电话")
    private String buildComplaintCall;

    @ApiModelProperty(value = "甲方住建部投诉电话")
    @TableField("a_society_complaint_call")
    private String aSocietyComplaintCall;

    @ApiModelProperty(value = "甲方人社部投诉电话")
    @TableField("a_build_complaint_call")
    private String aBuildComplaintCall;

    @ApiModelProperty(value = "劳务分包")
    private String laborSubcontract;

    @ApiModelProperty(value = "专业分包")
    private String majorSubcontract;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUserId;

    @ApiModelProperty(value = "传输数据时使用")
    private String appid;

    @ApiModelProperty(value = "sign 加密密匙")
    private String appsecret;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "项目经理列表")
    @TableField(exist = false)
    private List<SimpleUser> projectManagerList;

    @ApiModelProperty(value = "项目编码-全国建筑工人管理服务信息平台返回的编码")
    private String returnProjectCode;

    @ApiModelProperty(value = "平面图")
    private String planeImg;

    @ApiModelProperty(value = "平面图坐标")
    private String planeCoordinate;

    @ApiModelProperty(value = "智慧工地评价-申请数据id")
    private String evalApplyId;

    @ApiModelProperty(value = "施工阶段")
    private String progress;

    @ApiModelProperty(value = "工程投资性质")
    private String investType;

    @ApiModelProperty(value = "工程用途")
    private String purpose;

    @ApiModelProperty(value = "主要结构类型")
    private String subjectStructureType;

    @ApiModelProperty(value = "项目图片batchId")
    private String imgBatchId;

    @ApiModelProperty(value = "平面图BatchId")
    private String planBatchId;

    @ApiModelProperty(value = "评估等级")
    @TableField(exist = false)
    private String appLevel;
}
