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
 * 验收人员管理
 * </p>
 *
 * @author wnj
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_check_personnel")
@ApiModel(value="QualCheckPersonnel对象", description="验收人员管理")
public class QualCheckPersonnel implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "人员名称")
    private String personName;

    @ApiModelProperty(value = "联系电话")
    private String personPhone;

    @ApiModelProperty(value = "性别")
    private String personSex;

    @ApiModelProperty(value = "单位")
    private String personCom;

    @ApiModelProperty(value = "工作职责")
    private String workDuty;

    @ApiModelProperty(value = "验收内容")
    private String acceptContent;

    @ApiModelProperty(value = "人员资质")
    private String personSeniority;

    @ApiModelProperty(value = "人员照片")
    private String personPhoto;

    @ApiModelProperty(value = "批次id")
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
