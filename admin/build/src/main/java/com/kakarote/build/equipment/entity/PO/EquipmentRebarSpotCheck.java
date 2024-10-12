package com.kakarote.build.equipment.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 钢筋智能点检功能开发
 * </p>
 *
 * @author lzy
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_rebar_spot_check")
@ApiModel(value="EquipmentRebarSpotCheck对象", description="钢筋智能点检功能开发")
public class EquipmentRebarSpotCheck implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "钢筋类型")
    private String rebarType;

    @ApiModelProperty(value = "规格")
    private String specification;

    @ApiModelProperty(value = "强度等级")
    private String strengthGrade;

    @ApiModelProperty(value = "数量")
    private Integer number;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "点检日期")
    private Date spotCheckDate;

    @ApiModelProperty(value = "点检人员")
    private String spotCheckPeople;

    @ApiModelProperty(value = "点检结果")
    private String spotCheckResult;

    @ApiModelProperty(value = "点检图片 批次Id")
    private String batchId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "项目名称")
    @TableField(exist = false)
    private String projectName;


}
