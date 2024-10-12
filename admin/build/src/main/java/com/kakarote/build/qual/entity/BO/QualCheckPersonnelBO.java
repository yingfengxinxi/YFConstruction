package com.kakarote.build.qual.entity.BO;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 验收人员管理
 * </p>
 *
 * @author wnj
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("验收人员管理")
public class QualCheckPersonnelBO extends PageEntity {

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

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;
}
