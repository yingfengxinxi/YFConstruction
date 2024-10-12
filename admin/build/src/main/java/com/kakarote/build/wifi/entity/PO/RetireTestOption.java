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
 * 试卷问题选项
 * </p>
 *
 * @author wnj
 * @since 2021-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_retire_test_option")
@ApiModel(value="RetireTestOption对象", description="试卷问题选项")
public class RetireTestOption implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "选项id")
    private String id;

    @ApiModelProperty(value = "试题id")
    private String questId;

    @ApiModelProperty(value = "试题库选项标记")
    private String answerFlag;

    @ApiModelProperty(value = "试题库试题选项")
    private String options;

    @ApiModelProperty(value = "答案标记（0:错误1:正确）")
    private String optionFlag;

    @ApiModelProperty(value = "是否用户选项(1:是0:否)")
    private String ifUserOption;

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
