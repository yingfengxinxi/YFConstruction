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
 * wifi答题教育--试卷规则配置
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@Data
@ApiModel("wifi答题教育--试卷规则配置")
public class WifiTestPaperRulesBO extends PageEntity {

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

    @ApiModelProperty(value = "项目id")
    private Integer projectId;
    @ApiModelProperty(value = "租户id")
    private Integer tenantId;
}
