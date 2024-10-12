package com.kakarote.build.jg.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 视频监控执法记录
 * </p>
 *
 * @author wnj
 * @since 2021-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_jg_enforce_record")
@ApiModel(value="JgEnforceRecord对象", description="视频监控执法记录")
public class JgEnforceRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "发现时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date findDate;

    @ApiModelProperty(value = "发现人")
    private String findPeople;

    @ApiModelProperty(value = "发现问题")
    private String findProblem;

    @ApiModelProperty(value = "处罚措施")
    private String punishMeasures;

    @ApiModelProperty(value = "处罚条例")
    private String punishReg;

    @ApiModelProperty(value = "整改期限")
    private String rectTerm;

    @ApiModelProperty(value = "整改要求")
    private String rectReq;

    @ApiModelProperty(value = "执法依据附件（视频截图或录像）")
    private String batchId;

    @ApiModelProperty(value = "整改单位")
    private String rectUnit;

    @ApiModelProperty(value = "整改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date rectTime;

    @ApiModelProperty(value = "整改结果")
    private String rectResult;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;
}
