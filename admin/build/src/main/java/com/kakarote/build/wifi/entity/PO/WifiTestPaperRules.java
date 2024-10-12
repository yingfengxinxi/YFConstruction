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
 * wifi答题教育--试卷规则配置
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_wifi_test_paper_rules")
@ApiModel(value="WifiTestPaperRules对象", description="wifi答题教育--试卷规则配置")
public class WifiTestPaperRules implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "试卷名称")
    private String paperRulesName;

    @ApiModelProperty(value = "适应工种")
    private String workTypeId;

    @ApiModelProperty(value = "试卷总分值")
    private Integer paperScore;

    @ApiModelProperty(value = "试题数量")
    private Integer paperCount;

    @ApiModelProperty(value = "通过分值")
    private Integer pastScope;

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
