package com.kakarote.build.qual.entity.PO;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 监测数据填报--子表
 * </p>
 *
 * @author zy
 * @since 2021-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_monit_stb_zb")
@ApiModel(value="QualMonitSTbZb对象", description="监测数据填报--子表")
public class QualMonitSTbZb implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "主表id")
    private Integer pid;

    @ApiModelProperty(value = "监测项id")
    private Integer sid;

    @ApiModelProperty(value = "监测名称")
    private String sname;

    @ApiModelProperty(value = "值")
    private String svalue;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "创建用户")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新用户")
    private Long updateUserId;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

//    @ApiModelProperty(value = "租户")
//    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;


}
