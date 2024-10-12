package com.kakarote.build.labour.entity.PO;

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
 * 工资管理
 * </p>
 *
 * @author author
 * @since 2021-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_wage")
@ApiModel(value="BLabourWage对象", description="工资管理")
public class BLabourWage implements Serializable {

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
    private String wageTeamName;

    @ApiModelProperty(value = "年月")
    private String wageMonth;

    @ApiModelProperty(value = "分包单位")
    private String wageSubcontractor;

    @ApiModelProperty(value = "劳务工种")
    private String wageWorkId;

    @ApiModelProperty(value = "人数")
    private String infocollectionNumber;

    @ApiModelProperty(value = "总工日")
    private Integer wageManDays;

    @ApiModelProperty(value = "总工程量")
    private Integer wageQuantities;

    @ApiModelProperty(value = "应发工资")
    private Double wageShould;

    @ApiModelProperty(value = "预支工资")
    private Double wageDvance;

    @ApiModelProperty(value = "累计剩余")
    private Double wageaCumulativeBalance;

    @ApiModelProperty(value = "发放日期")
    private String wageDate;

}
