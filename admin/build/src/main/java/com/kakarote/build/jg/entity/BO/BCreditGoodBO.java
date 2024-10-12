package com.kakarote.build.jg.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 信用良好行为
 * </p>
 *
 * @author author
 * @since 2021-11-25
 */
@Data
@ApiModel(value = "信用良好行为")
public class BCreditGoodBO extends PageEntity {

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

    @ApiModelProperty(value = "数据类型   0  企业  1个人")
    private String crediType;

    @ApiModelProperty(value = "姓名")
    private String personnelName;

    @ApiModelProperty(value = "证件类型")
    private String personnelCardType;

    @ApiModelProperty(value = "证件号码")
    private String personnelCard;

    @ApiModelProperty(value = "所在企业统一社会信用代码")
    private String enterpriseUniscid;

    @ApiModelProperty(value = "所在企业名称")
    private String enterpriseName;

    @ApiModelProperty(value = "登记部门")
    private String goodDept;

    @ApiModelProperty(value = "登记人姓名")
    private String goodDeptNpersonnel;

    @ApiModelProperty(value = "登记日期")
    private Date goodDeptDate;

    @ApiModelProperty(value = "良好行为描述")
    private String goodDescribe;

    @ApiModelProperty(value = "良好行为发生日期")
    private Date goodDate;

    @ApiModelProperty(value = "良好行为发生地行政区划")
    private String goodDivision;

    @ApiModelProperty(value = "项目编码")
    private String projectNumber;

    @ApiModelProperty(value = "奖励部门")
    private String rewardDept;

    @ApiModelProperty(value = "奖励部门级别")
    private String rewardDeptLevel;

    @ApiModelProperty(value = "奖励决定内容")
    private String rewardContent;

    @ApiModelProperty(value = "奖励决定文号")
    private String rewardDocument;

    private Date rewardDate;

}
