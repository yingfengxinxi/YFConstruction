package com.kakarote.build.ai.entity.PO;

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
 * 渣土车辆智能分析
 * </p>
 *
 * @author lzy
 * @since 2021-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_ai_dreg_car_alert")
@ApiModel(value="AiDregCarAlert对象", description="渣土车辆智能分析")
public class AiDregCarAlert implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "编号")
    private String code;

    @ApiModelProperty(value = "车牌号")
    private String licensePlateNumber;

    @ApiModelProperty(value = "车牌颜色 字典项")
    private String licensePlateColor;

    @ApiModelProperty(value = "车型 字典项 1：载重汽车；2：越野汽车；3：倾卸汽车；4：牵引车；5：特种车；6：客车；7：轿车；8挂车；9：半挂车、加长货挂车；")
    private String carType;

    @ApiModelProperty(value = "车辆颜色")
    private String carColor;

    @ApiModelProperty(value = "方向 0：出场  1：进场")
    private String direction;

    @ApiModelProperty(value = "进出时间")
    private Date transferTime;

    @ApiModelProperty(value = "进出图片")
    private String img;

    @ApiModelProperty(value = "进出视频")
    private String video;

    @ApiModelProperty(value = "警报时间")
    private Date alertTime;

    @ApiModelProperty(value = "警报类型")
    private String alertType;

    @ApiModelProperty(value = "警报内容")
    private String alertContent;

    @ApiModelProperty(value = "警报图片")
    private String alertImg;

    @ApiModelProperty(value = "警报视频")
    private String alertVideo;

    @ApiModelProperty(value = "警报数据")
    private String alertData;

    @ApiModelProperty(value = "处置人")
    private String handleBy;

    @ApiModelProperty(value = "处理内容")
    private String handleContent;

    @ApiModelProperty(value = "处置时间")
    private Date handleTime;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    private Long updateUserId;

    @ApiModelProperty(value = "设备id")
    private Integer equipmentId;

    private Integer tenantId;

    private Integer projectId;

    @TableField(exist = false)
    @ApiModelProperty(value = "项目名称")
    private String projectName;



}
