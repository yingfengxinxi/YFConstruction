package com.kakarote.build.complex.mapper;

import com.kakarote.build.complex.entity.BO.ComplexMaterialsInBO;
import com.kakarote.build.complex.entity.PO.ComplexMaterialsOut;
import com.kakarote.build.wifi.entity.BO.WifiVideoBO;
import com.kakarote.build.wifi.entity.PO.WifiVideo;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 施工综合--物料出场 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-06-16
 */
public interface ComplexMaterialsOutMapper extends BaseMapper<ComplexMaterialsOut> {

    BasePage<Map<String,Object>> selectList(BasePage<ComplexMaterialsOut> page, @Param("data") ComplexMaterialsInBO complexMaterialsInBO);

}
