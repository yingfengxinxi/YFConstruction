package com.kakarote.build.equipment.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 * 设备数据对接平台
 * </p>
 *
 * @author wudw
 * @since 2021-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="EquipmentReceptPlatform查询对象", description="设备数据对接平台")
public class EquipmentReceptPlatformBO extends PageEntity {

    private String name;
}
