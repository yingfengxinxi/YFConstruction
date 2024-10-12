package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.LabourOddVisaBO;
import com.kakarote.build.labour.entity.PO.LabourOddVisa;
import com.kakarote.build.wifi.entity.BO.WifiVideoBO;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 零工签证 服务类
 * </p>
 *
 * @author zy
 * @since 2021-06-07
 */
public interface ILabourOddVisaService extends BaseService<LabourOddVisa> {

    public BasePage<Map<String,Object>> selectList(LabourOddVisaBO labourOddVisaBO);

    public void exportExcel(LabourOddVisaBO labourOddVisaBO, HttpServletResponse httpServletResponse);
}
