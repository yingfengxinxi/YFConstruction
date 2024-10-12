package com.kakarote.build.safety.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.safety.entity.BO.SafetyMixPlantBO;
import com.kakarote.build.safety.entity.PO.SafetyMixPlant;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 拌合站监控 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-06-18
 */
public interface ISafetyMixPlantService extends BaseService<SafetyMixPlant> {
    BasePage<SafetyMixPlant> selectByPage(SafetyMixPlantBO SafetyMixPlantBO);

    JSONObject excelImport(MultipartFile file) throws  Exception;
}
