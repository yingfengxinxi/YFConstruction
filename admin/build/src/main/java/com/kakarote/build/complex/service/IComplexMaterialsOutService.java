package com.kakarote.build.complex.service;

import com.kakarote.build.complex.entity.BO.ComplexMaterialsInBO;
import com.kakarote.build.complex.entity.PO.ComplexMaterialsOut;
import com.kakarote.build.wifi.entity.BO.WifiVideoBO;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 施工综合--物料出场 服务类
 * </p>
 *
 * @author zy
 * @since 2021-06-16
 */
public interface IComplexMaterialsOutService extends BaseService<ComplexMaterialsOut> {

    public BasePage<Map<String,Object>> selectList(ComplexMaterialsInBO complexMaterialsInBO);


}
