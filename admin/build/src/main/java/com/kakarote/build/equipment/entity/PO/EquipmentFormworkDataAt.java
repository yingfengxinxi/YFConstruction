package com.kakarote.build.equipment.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 高支模实时监测数据
 * </p>
 *
 * @author shz
 * @since 2022-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_equipment_formwork_data_at")
@ApiModel(value="EquipmentFormworkDataAt对象", description="高支模实时监测数据")
public class EquipmentFormworkDataAt implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备ID")
    private Integer equipmentId;

    @ApiModelProperty(value = "监测点id")
    private Integer pointId;

    @ApiModelProperty(value = "监测时间")
    private Date monitorTime;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "监测数据")
    private  String value;

    @ApiModelProperty(value = "x倾角")
    private Double xangle;

    @ApiModelProperty(value = "Y倾角")
    private Double yangle;

    @ApiModelProperty(value = "压力值")
    private Double pressure;

    @ApiModelProperty(value = "沉降值")
    private Double sedimentation;

    @ApiModelProperty(value = "水平位移 ")
    private Double horizontalDisplacement;

    @ApiModelProperty(value = "运行状态；0:离线，1:在线")
    private String deviceStatus;

    @ApiModelProperty(value = "报警状态；0:正常,1:预警,2:报警")
    private String alarmStatus;

    @ApiModelProperty(value = "预警值；无预警:0,电量预警：1,X轴预警:2,Y轴:4,压力:8,沉降:16,水平位移:32")
    private String alarm0;

    @ApiModelProperty(value = "报警值；无预警:0,电量报警：1,X轴报警:2,Y轴:4,压力:8,沉降:16,水平位移:32")
    private String alarm1;


}
