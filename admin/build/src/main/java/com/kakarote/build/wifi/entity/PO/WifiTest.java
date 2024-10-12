package com.kakarote.build.wifi.entity.PO;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 生成试卷
 * </p>
 *
 * @author wnj
 * @since 2021-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_wifi_test")
@ApiModel(value="WifiTest对象", description="生成试卷")
public class WifiTest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId (value="test_id")
    private String testId;

    @ApiModelProperty(value = "试卷规则")
    private String paperRulesId;

    @ApiModelProperty(value = "适用工种")
    private String workTypeId;

    @ApiModelProperty(value = "试卷总分值")
    private Integer paperScore;

    @ApiModelProperty(value = "试题数量")
    private Integer paperCount;

    @ApiModelProperty(value = "答题人员id")
    private String answerId;

    @ApiModelProperty(value = "答题人员姓名")
    private String answerName;

    @ApiModelProperty(value = "答题人员手机号")
    private String answerPhone;

    @ApiModelProperty(value = "总得分")
    private Integer questScore;

    @ApiModelProperty(value = "通过分值")
    private Integer pastScope;

    @ApiModelProperty(value = "创建者id")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新者id")
    private Long updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;


}
