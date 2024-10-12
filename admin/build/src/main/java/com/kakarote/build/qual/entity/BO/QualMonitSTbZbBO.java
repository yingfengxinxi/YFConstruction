package com.kakarote.build.qual.entity.BO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 监测数据填报--子表
 * </p>
 *
 * @author zy
 * @since 2021-03-24
 */
@Data
@ApiModel("监测数据填报--子表")
public class QualMonitSTbZbBO extends PageEntity {

    @ApiModelProperty(value = "主键")
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

//    @ApiModelProperty(value = "租户")
//    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;


}
