package com.kakarote.build.wifi.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
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
 * wifi教育答题--试卷规则配置详情
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_wifi_test_paper_rules_del")
@ApiModel(value="WifiTestPaperRulesDel对象", description="wifi教育答题--试卷规则配置详情")
public class WifiTestPaperRulesDel implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "试卷配置规则id")
    private String paperRulesId;

    @ApiModelProperty(value = "试题类别id")
    private String typeId;

    @ApiModelProperty(value = "题型(1:单选2:多选3：填空4：问答)")
    private String questType;

    @ApiModelProperty(value = "分值")
    private Integer questScore;

    @ApiModelProperty(value = "数量")
    private Integer questCount;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Long updateUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    private Integer projectId;

    private Integer tenantId;


}
