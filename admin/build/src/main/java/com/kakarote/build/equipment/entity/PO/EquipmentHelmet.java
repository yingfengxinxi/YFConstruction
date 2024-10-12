package com.kakarote.build.equipment.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 智能安全帽
 * </p>
 *
 * @author lzy
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_helmet")
@ApiModel(value="EquipmentHelmet对象", description="智能安全帽")
public class EquipmentHelmet implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备编号")
    private String imei;

    @ApiModelProperty(value = "项目人员id")
    private Integer peopleId;

    @ApiModelProperty(value = "呼叫状态 字典项 1-呼救 0-非呼救")
    private String sos;

    @ApiModelProperty(value = "电压")
    private String vol;

    @ApiModelProperty(value = "信号强度")
    private String gsm;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "项目名称")
    @TableField(exist = false)
    private String projectName;

    @ApiModelProperty(value = "项目人员名字")
    @TableField(exist = false)
    private String peopleName;

    @ApiModelProperty(value = "设备供应商")
    private String deviceFactory;
    @ApiModelProperty(value = "协议版本号")
    private String protocolVer;
    @ApiModelProperty(value = "数据类型")
    private String eventName;

}
