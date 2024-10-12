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
 * wifi教育答题--问题表
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_wifi_questions")
@ApiModel(value="WifiQuestions对象", description="wifi教育答题--问题表")
public class WifiQuestions implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "试题类别")
    private String typeId;

    @ApiModelProperty(value = "题型(1:单选，2:多选，3:填空，4:问答)")
    private String questType;

    @ApiModelProperty(value = "试题内容")
    private String questContent;

    @ApiModelProperty(value = "试题详解")
    private String questExplanation;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Long updateUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    private Integer tenantId;

    private Integer projectId;


}
