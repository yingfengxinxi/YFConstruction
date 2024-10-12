package com.kakarote.build.eval.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.eval.entity.BO.EvalItemsBO;
import com.kakarote.build.eval.entity.PO.EvalItems;
import com.kakarote.build.eval.entity.PO.EvalLevel;
import com.kakarote.build.eval.mapper.EvalItemsMapper;
import com.kakarote.build.eval.service.IEvalItemsService;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceRecord;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzy
 * @since 2022-02-22
 */
@Service
public class EvalItemsServiceImpl extends BaseServiceImpl<EvalItemsMapper, EvalItems> implements IEvalItemsService {

    @Override
    public BasePage<EvalItems> selectByPage(EvalItemsBO evalItemsBO) {
        evalItemsBO.setOrdersBd("a.sort",true);
        BasePage<EvalItems> list = this.getBaseMapper().selectByPage(evalItemsBO.parse(), evalItemsBO);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectList(EvalItemsBO evalItemsBO) {
        List<Map<String, Object>> list = this.getBaseMapper().selectItemList(evalItemsBO);
        return list;
    }

    /**
     * excel导入打分项
     *
     * @param file file
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject excelImport(MultipartFile file) {
        List<List<Object>> errList = new ArrayList<>();
        String filePath = getFilePath(file);
        Long userId = UserUtil.getUser().getUserId();
        AtomicReference<Integer> num = new AtomicReference<>(0);
        ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {

            if (rowIndex > 1) {
                EvalItems evalItems =new EvalItems();
                String uuid= UUID.randomUUID().toString().replaceAll("-", "");
                evalItems.setId(uuid);
                evalItems.setCreateBy(userId);
                evalItems.setCreateTime(new Date());
                String classType = rowList.get(0).toString().trim();
                if (classType != null && !classType.equals("")) {
                    evalItems.setClassType(classType);
                }
                String itemType = rowList.get(1).toString().trim();
                if (itemType != null && !itemType.equals("")) {
                    evalItems.setItemType(itemType);
                }
                String itemName = rowList.get(2).toString().trim();
                if (itemName != null && !itemName.equals("")) {
                    evalItems.setItemName(itemName);
                }
                String itemDes = rowList.get(3).toString().trim();
                if (itemDes != null && !itemDes.equals("")) {
                    evalItems.setItemDes(itemDes);
                }
                String minData = rowList.get(4).toString().trim();
                if (minData != null && !minData.equals("")) {
                    BigDecimal decimal = new BigDecimal(minData);
                    evalItems.setMinData(decimal);
                }
                String maxData = rowList.get(5).toString().trim();
                if (maxData != null && !maxData.equals("")) {
                    BigDecimal decimal = new BigDecimal(maxData);
                    evalItems.setMaxData(decimal);
                }
                String levels = rowList.get(6).toString().trim();
                if (levels != null && !levels.equals("")) {
                    evalItems.setLevels(levels);
                }

                String sort = rowList.get(7).toString().trim();
                if (sort != null && !sort.equals("")) {
                    evalItems.setSort(sort);
                }
                evalItems.setStatus("1");
                save(evalItems);
            }
        });
        FileUtil.del(filePath);
        JSONObject result = new JSONObject().fluentPut("totalSize", num.get()).fluentPut("errSize", 0);

        return result;
    }

    @Override
    public List<EvalItems> selectListByClassType(List<String> classTypeList) {
        List<EvalItems> list = this.getBaseMapper().selectListByClassType(classTypeList);
        return list;
    }

    @Override
    public List<EvalItems> selectListByItemName(List<String> itemNameList) {
        List<EvalItems> list = this.getBaseMapper().selectListByItemName(itemNameList);
        return list;
    }

    private String getFilePath(MultipartFile file) {
        String dirPath = FileUtil.getTmpDirPath();
        try {
            InputStream inputStream = file.getInputStream();
            File fromStream = FileUtil.writeFromStream(inputStream, dirPath + "/" + IdUtil.simpleUUID() + file.getOriginalFilename());
            return fromStream.getAbsolutePath();
        } catch (IOException e) {
            throw new CrmException(SystemCodeEnum.SYSTEM_UPLOAD_FILE_ERROR);
        }
    }
}
