package com.kakarote.build.safety.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.safety.entity.BO.BSafetyProductionBO;
import com.kakarote.build.safety.entity.PO.BSafetyProduction;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 安全生产评价 服务类
 * </p>
 *
 * @author zjc
 * @since 2022-06-08
 */
public interface IBSafetyProductionService extends BaseService<BSafetyProduction> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BSafetyProductionBO> selectList(BSafetyProductionBO bSafetyProductionBO);

    public String add(BSafetyProduction bSafetyProduction);

    public String edit(BSafetyProduction bSafetyProduction);

    /**
     * excel导入
     * @param file file
     */
    public JSONObject excelImport(MultipartFile file);
}
