package com.kakarote.admin.mapper;

import java.util.List;
import java.util.Map;

import com.kakarote.admin.entity.BO.AdminDictTypeBO;
import com.kakarote.admin.entity.PO.AdminMessage;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import com.kakarote.admin.entity.PO.AdminDictType;
import org.apache.ibatis.annotations.Param;

/**
 * 字典表 数据层
 * 
 * @author ruoyi
 */
public interface AdminDictTypeMapper extends BaseMapper<AdminDictType>
{
    /**
     * 根据条件分页查询字典类型
     * 
     * @return 字典类型集合信息
     */
    BasePage<AdminDictType> selectDictTypeList(BasePage<AdminMessage> parse, @Param("data") AdminDictTypeBO dictTypeBO);

    /**
     * 根据所有字典类型
     * 
     * @return 字典类型集合信息
     */
    List<AdminDictType> selectDictTypeAll();

    /**
     * 根据字典类型ID查询信息
     * 
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    AdminDictType selectDictTypeById(Long dictId);

    /**
     * 根据字典类型查询信息
     * 
     * @param dictType 字典类型
     * @return 字典类型
     */
    AdminDictType selectDictTypeByType(String dictType);

    /**
     * 通过字典ID删除字典信息
     * 
     * @param dictId 字典ID
     * @return 结果
     */
    int deleteDictTypeById(Long dictId);

    /**
     * 批量删除字典类型信息
     * 
     * @param dictIds 需要删除的字典ID
     * @return 结果
     */
    int deleteDictTypeByIds(Long[] dictIds);

    /**
     * 新增字典类型信息
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
    int insertDictType(AdminDictType dictType);

    /**
     * 修改字典类型信息
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
    int updateDictType(AdminDictType dictType);

    /**
     * 校验字典类型称是否唯一
     * 
     * @param dictType 字典类型
     * @return 结果
     */
    AdminDictType checkDictTypeUnique(String dictType);

    List<AdminDictType> selectCustomList(Map<String, String> params);

    List<Map<String,Object>> selectDiyList(Map<String, String> params);
}
