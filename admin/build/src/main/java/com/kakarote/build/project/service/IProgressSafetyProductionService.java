package com.kakarote.build.project.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.project.entity.BO.ProgressSafetyProductionBO;
import com.kakarote.build.project.entity.PO.ProgressSafetyProduction;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shz
 * @since 2022-06-14
 */
public interface IProgressSafetyProductionService extends BaseService<ProgressSafetyProduction> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<ProgressSafetyProductionBO> selectList(ProgressSafetyProductionBO bProgressSecurityBO);

    public String add(ProgressSafetyProduction bProgressSecurity);

    public String edit(ProgressSafetyProduction bProgressSecurity);

    /**
     * excel导入
     * @param file file
     */
    public JSONObject excelImport(MultipartFile file);

}
