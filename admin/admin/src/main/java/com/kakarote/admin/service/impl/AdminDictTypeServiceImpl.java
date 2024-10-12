package com.kakarote.admin.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import com.kakarote.admin.common.AdminConst;
import com.kakarote.admin.common.AdminDictUtils;
import com.kakarote.admin.entity.BO.AdminDictTypeBO;
import com.kakarote.admin.entity.PO.AdminMessage;
import com.kakarote.admin.mapper.AdminDictTypeMapper;
import com.kakarote.admin.mapper.AdminMessageMapper;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kakarote.admin.entity.PO.AdminDictData;
import com.kakarote.admin.entity.PO.AdminDictType;
import com.kakarote.admin.mapper.AdminDictDataMapper;
import com.kakarote.admin.service.IAdminDictTypeService;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
public class AdminDictTypeServiceImpl extends BaseServiceImpl<AdminDictTypeMapper, AdminDictType> implements IAdminDictTypeService
{

    @Autowired
    private AdminDictDataMapper dictDataMapper;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init()
    {
        List<AdminDictType> dictTypeList = getBaseMapper().selectDictTypeAll();
        for (AdminDictType dictType : dictTypeList)
        {
            List<AdminDictData> dictDatas = dictDataMapper.selectDictDataByType(dictType.getDictType(), dictType.getTenantId());
            AdminDictUtils.setDictCache(dictType.getTenantId(), dictType.getDictType(), dictDatas);
        }
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictTypeBO 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public BasePage<AdminDictType> selectDictTypeList(AdminDictTypeBO dictTypeBO)
    {
        BasePage<AdminDictType> adminMessageBasePage = getBaseMapper().selectDictTypeList(dictTypeBO.parse(),dictTypeBO);
        return adminMessageBasePage;
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<AdminDictData> selectDictDataByType(String dictType)
    {
        List<AdminDictData> dictDatas = AdminDictUtils.getDictCache(dictType);
        if (dictDatas!=null)
        {
            return dictDatas;
        }
        dictDatas = dictDataMapper.selectDictDataByType(dictType, UserUtil.getUser().getTenantId());
        if (dictDatas!=null)
        {
            AdminDictUtils.setDictCache(UserUtil.getUser().getTenantId(), dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public AdminDictType selectDictTypeById(Long dictId)
    {
        return getBaseMapper().selectDictTypeById(dictId);
    }

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Override
    public AdminDictType selectDictTypeByType(String dictType)
    {
        return getBaseMapper().selectDictTypeByType(dictType);
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     * @return 结果
     */
    @Override
    public int deleteDictTypeByIds(Long[] dictIds)
    {
        for (Long dictId : dictIds)
        {
            AdminDictType dictType = selectDictTypeById(dictId);
            if (dictDataMapper.countDictDataByType(dictType.getDictType()) > 0)
            {
                throw new CrmException(500,String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
        }
        int count = getBaseMapper().deleteDictTypeByIds(dictIds);
        if (count > 0)
        {
            AdminDictUtils.clearDictCache();
        }
        return count;
    }

    /**
     * 清空缓存数据
     */
    @Override
    public void clearCache()
    {
        AdminDictUtils.clearDictCache();
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Override
    public int insertDictType(AdminDictType dictType)
    {
        int row = getBaseMapper().insertDictType(dictType);
        if (row > 0)
        {
            AdminDictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDictType(AdminDictType dictType)
    {
        AdminDictType oldDict = getBaseMapper().selectDictTypeById(dictType.getDictId());
        dictDataMapper.updateDictDataType(oldDict.getDictType(), dictType.getDictType());
        int row = getBaseMapper().updateDictType(dictType);
        if (row > 0)
        {
            AdminDictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public String checkDictTypeUnique(AdminDictType dict)
    {
        Long dictId = dict.getDictId()==null ? -1L : dict.getDictId();
        AdminDictType dictType = getBaseMapper().checkDictTypeUnique(dict.getDictType());
        if (dictType!=null && dictType.getDictId().longValue() != dictId.longValue())
        {
            return AdminConst.NOT_UNIQUE;
        }
        return AdminConst.UNIQUE;
    }

    @Override
    public List<AdminDictType> selectCustomList(Map<String, String> params) {

        List<AdminDictType> adminDictTypeList = getBaseMapper().selectCustomList(params);
        return adminDictTypeList;
    }
}
