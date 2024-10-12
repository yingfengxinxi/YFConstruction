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
 * 质量资料
 * </p>
 *
 * @author wudw
 * @since 2021-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_material")
@ApiModel(value="QualMaterial对象", description="质量资料")
public class QualMaterial implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "租户ID")
    private Integer tenantId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUserId;

    @ApiModelProperty(value = "材料编号")
    private String materialCode;

    @ApiModelProperty(value = "材料名称")
    private String materialName;

    @ApiModelProperty(value = "材料类型")
    private String materialType;

    @ApiModelProperty(value = "编制单位")
    private String preparedUnit;

    @ApiModelProperty(value = "编制人员")
    private String preparedBy;

    @ApiModelProperty(value = "编制日期")
    private Date preparedTime;

    @ApiModelProperty(value = "编制序号")
    private Integer preparedNumber;

    @ApiModelProperty(value = "负责人")
    private String personCharge;

    @ApiModelProperty(value = "附件批次Id")
    private String batchId;

    @ApiModelProperty(value = "目录ID")
    private Integer catalogId;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否收藏")
    private String collect = "0";


}
