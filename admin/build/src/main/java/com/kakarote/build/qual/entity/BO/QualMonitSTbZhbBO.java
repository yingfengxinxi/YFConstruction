package com.kakarote.build.qual.entity.BO;

import com.baomidou.mybatisplus.annotation.*;
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
 * 监测数据填报--主表
 * </p>
 *
 * @author zy
 * @since 2021-03-24
 */
@Data
@ApiModel("监测数据填报--主表")
public class QualMonitSTbZhbBO extends PageEntity {

    @ApiModelProperty(value = "监测数据填报主键")
    private Integer id;

    @ApiModelProperty(value = "编号")
    private String code;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "数据日期")
    private String dataDate;

//    @ApiModelProperty(value = "租户")
//    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;


}
