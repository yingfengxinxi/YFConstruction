package com.kakarote.build.safety.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.safety.entity.BO.SafetyMixPlantDataBO;
import com.kakarote.build.safety.entity.PO.SafetyMixPlantData;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 拌合站监控基本信息 服务类
 * </p>
 *
 * @author lzy
 * @since 2022-08-30
 */
public interface ISafetyMixPlantDataService extends BaseService<SafetyMixPlantData> {

    BasePage<SafetyMixPlantData> selectByPage(SafetyMixPlantDataBO safetyMixPlantDataBO);

    JSONObject excelImport(MultipartFile file);
}
