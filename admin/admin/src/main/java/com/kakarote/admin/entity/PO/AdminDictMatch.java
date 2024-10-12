package com.kakarote.admin.entity.PO;

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
 * 
 * </p>
 *
 * @author lzy
 * @since 2021-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_admin_dict_match")
@ApiModel(value="AdminDictMatch对象", description="")
public class AdminDictMatch implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "本系统字典类型")
    private String selfDict;

    @ApiModelProperty(value = "本系统字典值")
    private String selfDictValue;

    @ApiModelProperty(value = "对应系统编码")
    private String matchSystem;

    @ApiModelProperty(value = "对应系统字典类型")
    private String matchDict;

    @ApiModelProperty(value = "对应系统值")
    private String matchValue;

    @ApiModelProperty(value = "对应系统名称")
    private String matchSysName;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    private Integer tenantId;

    @ApiModelProperty(value = "备注")
    private String remarks;

}
