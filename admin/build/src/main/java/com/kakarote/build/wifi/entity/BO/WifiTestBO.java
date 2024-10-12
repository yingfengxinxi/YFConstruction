package com.kakarote.build.wifi.entity.BO;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 生成试卷
 * </p>
 *
 * @author wnj
 * @since 2021-05-25
 */
@Data
@ApiModel("生成试卷")
public class WifiTestBO extends PageEntity {

    @ApiModelProperty(value = "主键")
    private String testId;

    @ApiModelProperty(value = "试卷规则")
    private String paperRulesId;

    @ApiModelProperty(value = "适用工种")
    private String workTypeId;

    @ApiModelProperty(value = "试卷总分值")
    private Integer paperScore;

    @ApiModelProperty(value = "试题数量")
    private Integer paperCount;

    @ApiModelProperty(value = "答题人员")
    private String answerId;

    @ApiModelProperty(value = "答题人员姓名")
    private String answerName;

    @ApiModelProperty(value = "答题人员手机号")
    private String answerPhone;

    @ApiModelProperty(value = "总得分")
    private Integer questScore;
    @ApiModelProperty(value = "查询得分范围下限")
    private Integer questScoreStart;
    @ApiModelProperty(value = "查询得分范围上限")
    private Integer questScoreEnd;
}
