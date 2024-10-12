package com.kakarote.build.qual.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 施工质量--分户验收--填报--子表
 * </p>
 *
 * @author wnj
 * @since 2021-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_house_accept_fill_son")
@ApiModel(value="QualHouseAcceptFillSon对象", description="施工质量--分户验收--填报--子表")
public class QualHouseAcceptFillSon implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "主表id")
    private Integer pid;

    @ApiModelProperty(value = "验收类型")
    private String acceptType;

    @ApiModelProperty(value = "验收项目")
    private String acceptPro;

    @ApiModelProperty(value = "验收内容")
    private String acceptContent;

    @ApiModelProperty(value = "验收结果")
    private String acceptResult;

    @ApiModelProperty(value = "是否通过")
    private String isAdopt;

    @ApiModelProperty(value = "验收附件批次")
    private String batchId;

    @ApiModelProperty(value = "创建者")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;


}
