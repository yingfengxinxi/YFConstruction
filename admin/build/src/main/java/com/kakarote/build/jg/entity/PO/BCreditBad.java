package com.kakarote.build.jg.entity.PO;

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
 * 信用不良行为
 * </p>
 *
 * @author author
 * @since 2021-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_credit_bad")
@ApiModel(value="BCreditBad对象", description="信用不良行为")
public class BCreditBad implements Serializable {

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
    private String badDept;

    @ApiModelProperty(value = "登记人姓名")
    private String badDeptNpersonnel;

    @ApiModelProperty(value = "登记日期")
    private Date badDeptDate;

    @ApiModelProperty(value = "不良行为类别")
    private String badType;

    @ApiModelProperty(value = "不良行为代码")
    private String badCode;

    @ApiModelProperty(value = "不良行为描述")
    private String badDescribe;

    @ApiModelProperty(value = "不良行为发生日期")
    private Date badDate;

    @ApiModelProperty(value = "不良行为发生地行政区划")
    private String badDivision;

    @ApiModelProperty(value = "项目编码")
    private String projectNumber;

    @ApiModelProperty(value = "处罚部门")
    private String punishmentDept;

    @ApiModelProperty(value = "处罚部门级别")
    private String punishmentdDeptLevel;

    @ApiModelProperty(value = "处罚依据")
    private String punishmentBasis;

    @ApiModelProperty(value = "处罚决定内容")
    private String punishmentContent;

    @ApiModelProperty(value = "处罚决定文号")
    private String punishmentDocument;

    @ApiModelProperty(value = "处罚日期")
    private Date punishmentDate;

    @ApiModelProperty(value = "处罚截止日期")
    private Date punishmentDateAsof;


}
