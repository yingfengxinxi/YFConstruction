package com.kakarote.build.complex.service;

import com.kakarote.build.complex.entity.BO.ComplexMaterialsInBO;
import com.kakarote.build.complex.entity.PO.ComplexMaterialsIn;
import com.kakarote.build.wifi.entity.BO.WifiVideoBO;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 施工综合--物料进场 服务类
 * </p>
 *
 * @author zy
 * @since 2021-06-16
 */
public interface IComplexMaterialsInService extends BaseService<ComplexMaterialsIn> {

    public BasePage<Map<String,Object>> selectList(ComplexMaterialsInBO complexMaterialsInBO);


}
