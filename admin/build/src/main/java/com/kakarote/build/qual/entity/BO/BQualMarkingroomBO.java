package com.kakarote.build.qual.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author pz
 * @since 2021-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="分部分项管理", description="")
public class BQualMarkingroomBO extends PageEntity {

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

    @ApiModelProperty(value = "最低湿度报警值")
    private Double maxHumidity;

    @ApiModelProperty(value = "监测状态")
    private String isSupervise;

}
