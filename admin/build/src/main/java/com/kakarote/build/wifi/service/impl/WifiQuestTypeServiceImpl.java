package com.kakarote.build.wifi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.project.entity.PO.ProgressModelsetDetails;
import com.kakarote.build.wifi.entity.PO.WifiQuestType;
import com.kakarote.build.wifi.mapper.WifiQuestTypeMapper;
import com.kakarote.build.wifi.service.IWifiQuestTypeService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * WiFi教育答题--问题类别管理 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@Service
public class WifiQuestTypeServiceImpl extends BaseServiceImpl<WifiQuestTypeMapper, WifiQuestType> implements IWifiQuestTypeService {

    @Override
    public List<Map> queryChildrenByMap(Map<String, Object> map) {
        return getBaseMapper().queryChildrenByMap(map);
    }

    @Override
    public List<Map> selectTree() {
        List<WifiQuestType> list = query()
                .ne("tenant_id",0)
                .and(wrapper -> wrapper
                        .ne("project_id", 0)
                        .or()
                        .isNull("project_id")
                )
                .orderByAsc("type_num")
                .orderByDesc("create_time").list();
        List<Map> recursionMap = recursion("0", list);
        return recursionMap;
    }

    /**
     * 删除全部子节点
     * @param id
     */
    @Override
    public void delById(String id) {
        //先查询此数据是否是父节点
        List<WifiQuestType> list = query().eq("parentId", id).
                ne("tenant_id",0).
                and(wrapper -> wrapper
                        .ne("project_id", 0)
                        .or()
                        .isNull("project_id")
                ).list();

        QueryWrapper<WifiQuestType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.ne("tenant_id",0);
        queryWrapper.and(wrapper -> wrapper
                .ne("project_id", 0)
                .or()
                .isNull("project_id")
        );
        if(list.size() > 0){//是父节点
            for(int i = 0 ; i < list.size(); i++){
                delById(list.get(i).getId());
                //removeById(id);
                //remove(queryWrapper);
                getBaseMapper().deleteTypeById(id);
            }
        }else{
            //removeById(id);
            getBaseMapper().deleteTypeById(id);
            remove(queryWrapper);
        }

    }


    public List<Map> recursion(String id,List<WifiQuestType> listAll){
        List<Map> all = new ArrayList<>();
        List<WifiQuestType> list = listAll.stream().filter(down -> (down.getParentId().equals(id))).collect(Collectors.toList());
        if(list.size()<=0){
            return null;
        }
        for(WifiQuestType catalog :list){
            Map obj = new HashMap();
            obj.put("id",catalog.getId());
            obj.put("name",catalog.getTypeName());

            List children = recursion(catalog.getId(),listAll);
            if(children != null){
                obj.put("children",children);
            }else{
                obj.put("children",null);
            }
            all.add(obj);
        }
        return  all;
    }

}
