package com.kakarote.admin.service;

import java.util.List;
import java.util.Map;

import com.kakarote.admin.entity.BO.AdminDictTypeBO;
import com.kakarote.admin.entity.PO.AdminDictData;
import com.kakarote.admin.entity.PO.AdminDictType;
import com.kakarote.core.entity.BasePage;

/**
 * 字典 业务层
 * 
 * @author ruoyi
 */
public interface IAdminDictTypeService
{
    /**
     * 根据条件分页查询字典类型
     * 
     * @param dictTypeBO 字典类型信息
     * @return 字典类型集合信息
     */
    public BasePage<AdminDictType> selectDictTypeList(AdminDictTypeBO dictTypeBO);

    /**
     * 根据字典类型查询字典数据
     * 
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    public List<AdminDictData> selectDictDataByType(String dictType);

    /**
     * 根据字典类型ID查询信息
     * 
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    public AdminDictType selectDictTypeById(Long dictId);

    /**
     * 根据字典类型查询信息
     * 
     * @param dictType 字典类型
     * @return 字典类型
     */
    public AdminDictType selectDictTypeByType(String dictType);

    /**
     * 批量删除字典信息
     * 
     * @param dictIds 需要删除的字典ID
     * @return 结果
     */
    public int deleteDictTypeByIds(Long[] dictIds);

    /**
     * 清空缓存数据
     */
    public void clearCache();

    /**
     * 新增保存字典类型信息
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
    public int insertDictType(AdminDictType dictType);

    /**
     * 修改保存字典类型信息
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
    public int updateDictType(AdminDictType dictType);

    /**
     * 校验字典类型称是否唯一
     * 
     * @param dictType 字典类型
     * @return 结果
     */
    public String checkDictTypeUnique(AdminDictType dictType);

    public List<AdminDictType> selectCustomList(Map<String,String> params);
}
