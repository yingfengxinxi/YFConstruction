package com.kakarote.build.equipment.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 信用良好行为
 * </p>
 *
 * @author author
 * @since 2021-11-25
 */
@Data
@ApiModel(value = "智慧安全帽")
public class EquipmentRebarSpotCheckBO extends PageEntity {

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "钢筋类型")
    private String rebarType;

}
