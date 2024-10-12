package com.kakarote.build.qual.entity.PO;

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
 * 标养室基本信息
 * </p>
 *
 * @author zhang
 * @since 2022-07-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_markingroom")
@ApiModel(value="BQualMarkingroom对象", description="标养室基本信息")
public class BQualMarkingroom implements Serializable {

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

    @ApiModelProperty(value = "标养室名称")
    private String curingRoomName;

    @ApiModelProperty(value = "最低温度报警值")
    private Double minTemp;

    @ApiModelProperty(value = "最高温度报警值")
    private Double maxTemp;

    @ApiModelProperty(value = "最低湿度报警值")
    private Double minHumidity;

    @ApiModelProperty(value = "最高湿度报警值")
    private Double maxHumidity;

    @ApiModelProperty(value = "监测状态")
    private String isSupervise;


}
