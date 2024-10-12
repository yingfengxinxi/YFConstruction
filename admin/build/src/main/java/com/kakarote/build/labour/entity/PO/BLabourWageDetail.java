package com.kakarote.build.labour.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.poi.hpsf.Decimal;

/**
 * <p>
 * 工资管理详情
 * </p>
 *
 * @author author
 * @since 2021-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_wage_detail")
@ApiModel(value="BLabourWageDetail对象", description="工资管理详情")
public class BLabourWageDetail implements Serializable {

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

    @ApiModelProperty(value = "班组id")
    private String wageDetailTeamName;

    @ApiModelProperty(value = "工资主表id  b_labour_wage 的主键")
    private String wageId;

    @ApiModelProperty(value = "人员id  b_labour_infocollection的主键")
    private String infocollectionId;

    @ApiModelProperty(value = "人员姓名")
    private String infocollectionName;

    @ApiModelProperty(value = "身份证号")
    private String infocollectionCard;

    @ApiModelProperty(value = "发放日期")
    private Date issueDate;

    @ApiModelProperty(value = "日单价")
    private Double dayPrice;

    @ApiModelProperty(value = "工日")
    private Integer manDays;

    @ApiModelProperty(value = "工程量单价")
    private Double quantitiesPrice;

    @ApiModelProperty(value = "工程量")
    private Integer quantities;

    @ApiModelProperty(value = "应发工资")
    private Double wageDetailShould;

    @ApiModelProperty(value = "预支工资")
    private Double wageDetailAdvance;

    @ApiModelProperty(value = "本月余额")
    private Double monthBalance;

    @ApiModelProperty(value = "累计剩余")
    private Double cumulativeBalance;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "年月")
    private String wageDetailMonth;

    @ApiModelProperty(value = "证件类型")
    private String idCardType;

    @ApiModelProperty(value = "总工时")
    private BigDecimal workHours;

    @ApiModelProperty(value = "实发金额")
    private Double actualAmount;

    @ApiModelProperty(value = "是否为补发")
    private String isBackPay;

    @ApiModelProperty(value = "补发月份")
    private String backPayMonth;

    @ApiModelProperty(value = "第三方工资单编号")
    private String thirdPayRollCode;

    @ApiModelProperty(value = "工资卡号")
    private String wagesCardNumber;

    @ApiModelProperty(value = "数据生成方式  0:本地生成  1:接口传入")
    private String dataGeneration;

    @ApiModelProperty(value = "工资卡银行代码")
    private String wagesBankCode;

    @ApiModelProperty(value = "工人工资卡开户行名称")
    private String wagesBankName;

    @ApiModelProperty(value = "代发银行卡号")
    private String undertakesCardNumber;

    @ApiModelProperty(value = "代发银行代码")
    private String undertakesBankCode;

    @ApiModelProperty(value = "代发银行名称")
    private String undertakesBankName;

}
