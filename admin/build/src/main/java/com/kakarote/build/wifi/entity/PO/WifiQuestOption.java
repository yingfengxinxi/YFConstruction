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
 * wifi教育答题--问题选项表
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_wifi_quest_option")
@ApiModel(value="WifiQuestOption对象", description="wifi教育答题--问题选项表")
public class WifiQuestOption implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "问题表id")
    private String questId;

    @ApiModelProperty(value = "试题库选项标记")
    private String answerFlag;

    @ApiModelProperty(value = "试题库试题选项")
    private String options;

    @ApiModelProperty(value = "答案标记（0:错误，1:正确）")
    private String optionsFlag;

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
