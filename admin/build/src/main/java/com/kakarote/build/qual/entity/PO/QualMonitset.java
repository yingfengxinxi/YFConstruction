package com.kakarote.build.qual.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 监测项设置
 * </p>
 *
 * @author zy
 * @since 2021-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_monitset")
@ApiModel(value="QualMonitset对象", description="监测项设置")
public class QualMonitset implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "监测主键")
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty(value = "检测项类型(1实测实量；2标养室监测)")
    private String type;

//    @ApiModelProperty(value = "租户")
//    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建人id")
    private Integer createUserId;

    @ApiModelProperty(value = "修改人id")
    private Integer updateUserId;

    @ApiModelProperty(value = "创建人name")
    private String createUserName;

    @ApiModelProperty(value = "修改人name")
    private String updateUserName;

}
