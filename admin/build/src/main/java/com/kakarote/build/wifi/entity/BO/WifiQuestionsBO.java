package com.kakarote.build.wifi.entity.BO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * wifi教育答题--问题表
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@Data
@ApiModel("wifi教育答题--问题表")
public class WifiQuestionsBO extends PageEntity {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "试题类别")
    private String typeId;

    @ApiModelProperty(value = "题型(1:单选，2:多选，3:填空，4:问答)")
    private String questType;

    @ApiModelProperty(value = "试题内容")
    private String questContent;


}
