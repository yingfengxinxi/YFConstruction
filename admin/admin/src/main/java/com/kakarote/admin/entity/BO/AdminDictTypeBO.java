package com.kakarote.admin.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 字典类型表 sys_dict_type
 * 
 * @author ruoyi
 */
@Data
@ApiModel("字典表查询对象")
public class AdminDictTypeBO  extends PageEntity
{

    /** 字典名称 */
    @ApiModelProperty("字典名称")
    private String dictName;

    /** 字典类型 */
    @ApiModelProperty("字典类型")
    private String dictType;

    /** 状态（0正常 1停用） */
    @ApiModelProperty("状态")
    private String status;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 备注 */
    @ApiModelProperty("备注")
    private String remark;

}
