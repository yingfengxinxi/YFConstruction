package com.kakarote.build.wifi.entity.BO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 试卷问题选项
 * </p>
 *
 * @author wnj
 * @since 2021-05-25
 */
@Data
@ApiModel("试卷问题选项")
public class RetireTestOptionBO implements Serializable {

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

}
