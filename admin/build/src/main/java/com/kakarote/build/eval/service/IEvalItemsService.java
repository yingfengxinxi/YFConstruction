package com.kakarote.build.eval.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.eval.entity.BO.EvalItemsBO;
import com.kakarote.build.eval.entity.PO.EvalItems;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzy
 * @since 2022-02-22
 */
public interface IEvalItemsService extends BaseService<EvalItems> {

    BasePage<EvalItems> selectByPage(EvalItemsBO evalItemsBO);

    List<Map<String, Object>> selectList(EvalItemsBO evalItemsBO);

    /**
     * excel导入
     * @param file file
     */
    public JSONObject excelImport(MultipartFile file);

    List<EvalItems> selectListByClassType(List<String> classTypeList);

    List<EvalItems> selectListByItemName(List<String> itemNameList);
}
