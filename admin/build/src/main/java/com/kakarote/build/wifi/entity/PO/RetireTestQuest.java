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
 * 试卷问题
 * </p>
 *
 * @author wnj
 * @since 2021-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_retire_test_quest")
@ApiModel(value="RetireTestQuest对象", description="试卷问题")
public class RetireTestQuest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "原问题id")
    private String fromQuestId;

    @ApiModelProperty(value = "试卷id")
    private String testId;

    @ApiModelProperty(value = "题型(1:单选2:多选3：填空4：问答)")
    private String questType;

    @ApiModelProperty(value = "试题内容")
    private String questContent;

    @ApiModelProperty(value = "试题详解")
    private String questExplanation;

    @ApiModelProperty(value = "试题分值")
    private Integer questScore;

    @ApiModelProperty(value = "是否正确（1:正确0:错误）")
    private String ifTrue;

    @ApiModelProperty(value = "正确选项")
    private String trueOption;

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
