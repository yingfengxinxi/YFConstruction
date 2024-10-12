package com.kakarote.admin.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.admin.entity.PO.AdminRoleMenuDp;
import com.kakarote.admin.mapper.AdminRoleMenuDpMapper;
import com.kakarote.admin.service.IAdminRoleMenuDpService;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shz
 * @since 2022-09-07
 */
@Service
public class AdminRoleMenuDpServiceImpl extends BaseServiceImpl<AdminRoleMenuDpMapper, AdminRoleMenuDp> implements IAdminRoleMenuDpService {

    @Override
    @Transactional
    public boolean updateRoleMenu(Map map) {
        Integer roleId = (Integer) map.get("roleId");
        JSONObject object = new JSONObject().fluentPut("role_id", roleId);
        removeByMap(object);
        Map menus = (Map) map.get("menus");
        Set<String> set =  ((Map)menus.get("supervise")).keySet();
        Set<String> set2 =  ((Map)menus.get("construction")).keySet();
        Set<String> set3 =  ((Map)menus.get("threeDimensional")).keySet();
        List<AdminRoleMenuDp> list = new ArrayList<>();
        for(String menu:set){
            if((boolean)((Map) menus.get("supervise")).get(menu)){
                AdminRoleMenuDp adminRoleMenuDp = new AdminRoleMenuDp();
                adminRoleMenuDp.setPlatform("supervise");
                adminRoleMenuDp.setRoleId(roleId);
                adminRoleMenuDp.setMenu(menu);
                list.add(adminRoleMenuDp);
            }

        }
        for(String menu:set2){
            if((boolean)((Map) menus.get("construction")).get(menu)){
                AdminRoleMenuDp adminRoleMenuDp = new AdminRoleMenuDp();
                adminRoleMenuDp.setPlatform("construction");
                adminRoleMenuDp.setRoleId(roleId);
                adminRoleMenuDp.setMenu(menu);
                list.add(adminRoleMenuDp);
            }

        }
        for(String menu:set3){
            if((boolean)((Map) menus.get("threeDimensional")).get(menu)){
                AdminRoleMenuDp adminRoleMenuDp = new AdminRoleMenuDp();
                adminRoleMenuDp.setPlatform("threeDimensional");
                adminRoleMenuDp.setRoleId(roleId);
                adminRoleMenuDp.setMenu(menu);
                list.add(adminRoleMenuDp);
            }
        }
        return saveBatch(list);
    }

    @Override
    public Map getByUser() {
        UserInfo user = UserUtil.getUser();
        List<Integer> roles =  user.getRoles();
        QueryWrapper<AdminRoleMenuDp> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id",roles);
        List<AdminRoleMenuDp> list = list(queryWrapper);
        Map<String,Map> map = getMap();
        for(AdminRoleMenuDp adminRoleMenuDp:list){
            map.get(adminRoleMenuDp.getPlatform()).put(adminRoleMenuDp.getMenu(),1);
        }
        return map;
    }

    public Map<String,Map> getMap(){
        Map<String,Map> map = new HashMap();
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        Map map3 = new HashMap();
        map.put("supervise",map1);
        map.put("construction",map2);
        map.put("threeDimensional",map3);
        return map;
    }
}
