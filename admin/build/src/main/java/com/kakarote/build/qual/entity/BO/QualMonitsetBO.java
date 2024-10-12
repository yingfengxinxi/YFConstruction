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

/**
 * <p>
 * 监测项设置
 * </p>
 *
 * @author zy
 * @since 2021-03-24
 */
@Data
@ApiModel("监测项设置")
public class QualMonitsetBO extends PageEntity {


    @ApiModelProperty(value = "监测主键")
    private Integer id;

    @ApiModelProperty(value = "监测项编码")
    private String code;

    @ApiModelProperty(value = "监测项名称")
    private String name;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "公式")
    private String publicity;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "关联设备名称")
    private String assDeviceName;

    @ApiModelProperty(value = "报警上限阀值")
    private Integer alarmMax;

    @ApiModelProperty(value = "报警下限阀值")
    private Integer alarmMin;

    @ApiModelProperty(value = "检测项类型(1实测实量；2标养室监测；3检测检验)")
    private String type;

//    @ApiModelProperty(value = "租户")
//    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    private String sValue;

    private String note;
}
