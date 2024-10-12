package com.kakarote.admin.service.impl;

import java.util.List;

import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kakarote.admin.entity.PO.AdminDictData;
import com.kakarote.admin.mapper.AdminDictDataMapper;
import com.kakarote.admin.service.IAdminDictDataService;
import com.kakarote.admin.common.AdminDictUtils;

/**
 * 字典 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class AdminDictDataServiceImpl extends BaseServiceImpl<AdminDictDataMapper, AdminDictData> implements IAdminDictDataService
{
    @Autowired
    private AdminDictDataMapper dictDataMapper;

    /**
     * 根据条件分页查询字典数据
     * 
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<AdminDictData> selectDictDataList(AdminDictData dictData)
    {
        return dictDataMapper.selectDictDataList(dictData);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     * 
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue)
    {
        return dictDataMapper.selectDictLabel(dictType, dictValue);
    }

    /**
     * 根据字典数据ID查询信息
     * 
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public AdminDictData selectDictDataById(Long dictCode)
    {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    /**
     * 批量删除字典数据信息
     * 
     * @param dictCodes 需要删除的字典数据ID
     * @return 结果
     */
    @Override
    public int deleteDictDataByIds(Long[] dictCodes)
    {
        int row = dictDataMapper.deleteDictDataByIds(dictCodes);
        if (row > 0)
        {
            AdminDictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * 新增保存字典数据信息
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(AdminDictData dictData)
    {
        if(save(dictData)){
            AdminDictUtils.clearDictCache();
        }
        return 1;
//        int row = dictDataMapper.insertDictData(dictData);
//        if (row > 0)
//        {
//            AdminDictUtils.clearDictCache();
//        }
//        return row;
    }

    /**
     * 修改保存字典数据信息
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(AdminDictData dictData)
    {
        int row = dictDataMapper.updateDictData(dictData);
        if (row > 0)
        {
            AdminDictUtils.clearDictCache();
        }
        return row;
    }
}
