package com.kakarote.build.project.service;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.project.entity.BO.BProgressSecurityBO;
import com.kakarote.build.project.entity.PO.BProgressSecurity;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 施工建筑安全检查 服务类
 * </p>
 *
 * @author author
 * @since 2022-06-01
 */
public interface IBProgressSecurityService extends BaseService<BProgressSecurity> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BProgressSecurityBO> selectList(BProgressSecurityBO bProgressSecurityBO);

    public String add(BProgressSecurity bProgressSecurity);

    public String edit(BProgressSecurity bProgressSecurity);

    /**
     * excel导入
     * @param file file
     */
    public JSONObject excelImport(MultipartFile file);

}
