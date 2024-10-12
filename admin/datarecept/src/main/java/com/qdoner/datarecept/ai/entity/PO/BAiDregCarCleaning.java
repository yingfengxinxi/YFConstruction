package com.qdoner.datarecept.ai.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 车辆清洗记录
 * </p>
 *
 * @author author
 * @since 2021-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_ai_dreg_car_cleaning")
@ApiModel(value="BAiDregCarCleaning对象", description="车辆清洗记录")
public class BAiDregCarCleaning implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "车牌号")
    private String licensePlateNumber;

    @ApiModelProperty(value = "车牌颜色 字典项")
    private String licensePlateColor;

    @ApiModelProperty(value = "车型 字典项 1：载重汽车；2：越野汽车；3：倾卸汽车；4：牵引车；5：特种车；6：客车；7：轿车；8挂车；9：半挂车、加长货挂车；")
    private String carType;

    private String carColor;

    @ApiModelProperty(value = "进场时间")
    private Date approachTime;

    @ApiModelProperty(value = "进场图片")
    private String approachImg;

    @ApiModelProperty(value = "出场时间")
    private Date appearanceTime;

    @ApiModelProperty(value = "出场图片")
    private String appearanceImg;

    @ApiModelProperty(value = "清洗时间/抓拍时间")
    private Date cleaningTime;

    @ApiModelProperty(value = "清洗图片/抓拍图片 存URL 或base64位数据")
    private String cleaningImg;

    @ApiModelProperty(value = "抓拍编号 第三方对应数据的唯一编号")
    private String catchNo;

    @ApiModelProperty(value = "抓拍类型 字典项 1：绕道；2：未冲洗；3冲洗时间过短")
    private String alarmType;

    @ApiModelProperty(value = "抓拍视频")
    private String videoUrl;

    @ApiModelProperty(value = "项目名称")
    @TableField(exist = false)
    private String projectName;


}
