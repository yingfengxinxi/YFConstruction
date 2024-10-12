package com.qdoner.datarecept.ai.entity.PO;

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
 * 渣土车记录
 * </p>
 *
 * @author lzy
 * @since 2021-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_ai_dreg_car")
@ApiModel(value="AiDregCar对象", description="渣土车记录")
public class AiDregCar implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "车牌号")
    private String licensePlateNumber;

    @ApiModelProperty(value = "车牌颜色 字典项")
    private String licensePlateColor;

    @ApiModelProperty(value = "车型 字典项 1：载重汽车；2：越野汽车；3：倾卸汽车；4：牵引车；5：特种车；6：客车；7：轿车；8挂车；9：半挂车、加长货挂车；")
    private String carType;

    @ApiModelProperty(value = "车身颜色")
    private String carColor;

    @ApiModelProperty(value = "方向 0：出场  1：进场")
    private String direction;

    @ApiModelProperty(value = "进出时间")
    private Date transferTime;

    @ApiModelProperty(value = "进出图片")
    private String img;

    @ApiModelProperty(value = "进出视频")
    private String video;

    private Integer tenantId;

    private Integer projectId;

    @ApiModelProperty(value = "设备id")
    private Integer equipmentId;

}
