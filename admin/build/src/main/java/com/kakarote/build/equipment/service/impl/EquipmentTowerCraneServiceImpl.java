package com.kakarote.build.equipment.service.impl;

import com.kakarote.build.equipment.entity.PO.EquipmentTowerCrane;
import com.kakarote.build.equipment.mapper.EquipmentTowerCraneMapper;
import com.kakarote.build.equipment.service.IEquipmentTowerCraneService;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 塔机配置表 服务实现类
 * </p>
 *
 * @author shz
 * @since 2021-11-18
 */
@Service
public class EquipmentTowerCraneServiceImpl extends BaseServiceImpl<EquipmentTowerCraneMapper, EquipmentTowerCrane> implements IEquipmentTowerCraneService {

    @Override
    public EquipmentTowerCrane getByDeviceId(Integer deviceId) {
        return getBaseMapper().getByDeviceId(deviceId);
    }

    @Override
    public Map getTdgx(Map map) {
        UserInfo userInfo =  UserUtil.getUser();
        Map res = new HashMap();
        map.put("projectId",userInfo.getProjectId());
        res.put("number",getBaseMapper().getTowerCraneNumber(map));
        res.put("frequency",getBaseMapper().getTowerCraneFrequency(map));
        res.put("weight",getBaseMapper().getTowerCraneWeight(map));
        res.put("time",getBaseMapper().getTowerCraneTime(map));
        return res;
    }

    @Override
    public List<Map> getTzphb(Map map) {
        UserInfo userInfo =  UserUtil.getUser();
        map.put("projectId",userInfo.getProjectId());
        List<Map> list = getBaseMapper().getDZPHB(map);
        return list;
    }

    @Override
    public Map getQtdzxlfx(Map map) {
        UserInfo userInfo =  UserUtil.getUser();
        map.put("projectId",userInfo.getProjectId());
        List<Map> list = getBaseMapper().getQTDZXLFX(map);
        List<Map> list2 = getBaseMapper().getQTDZXLFXSJ(map);
        Map res = new HashMap();
        List<String> dateList= list.stream().map(u -> (String)u.get("name")).collect(Collectors.toList());
        List<BigDecimal> valueList1= list.stream().map(u -> (BigDecimal)u.get("value")).collect(Collectors.toList());
        List<BigDecimal> valueList2= list2.stream().map(u -> (BigDecimal)u.get("value")).collect(Collectors.toList());
        res.put("dateList",dateList);
        res.put("valueList1",valueList1);
        res.put("valueList2",valueList2);
        return res;
    }

    @Override
    public Map getXhdzcs(Map map) {
        UserInfo userInfo =  UserUtil.getUser();
        map.put("projectId",userInfo.getProjectId());
        List<Map> list = getBaseMapper().getQTDZXLFX(map);
        Map res = new HashMap();
        List<String> dateList= list.stream().map(u -> (String)u.get("name")).collect(Collectors.toList());
        List<BigDecimal> valueList1= list.stream().map(u -> (BigDecimal)u.get("value")).collect(Collectors.toList());
        res.put("dateList",dateList);
        res.put("valueList",valueList1);
        return res;
    }

    @Override
    public Map getDtzlztbfb(Map map) {
        UserInfo userInfo =  UserUtil.getUser();
        map.put("projectId",userInfo.getProjectId());
        List<Map> list = getBaseMapper().getDZZL(map);
        List<Object> dateList= list.stream().map(u -> u.get("time")).collect(Collectors.toList());
        List<Double> valueList1= list.stream().map(u -> (Double)u.get("dz")).collect(Collectors.toList());
        List<Double> valueList2= list.stream().map(u -> (Double)u.get("zzb")).collect(Collectors.toList());
        Map res = new HashMap();
        res.put("dateList",dateList);
        res.put("valueList1",valueList1);
        res.put("valueList2",valueList2);
        return res;
    }

    @Override
    public List getHsztddzcs(Map map) {
        UserInfo userInfo =  UserUtil.getUser();
        map.put("projectId",userInfo.getProjectId());
        List<Object> res = new ArrayList<>();
        List<Map> towerCranes = getBaseMapper().selectAllTowerCranr();
        List<String> list1 = new ArrayList<>();
        List<Object> list2 = new ArrayList<>();
        List<Object> list3 = new ArrayList<>();
        List<Object> list4 = new ArrayList<>();
        List<Object> list5 = new ArrayList<>();
        list1.add("product");
        list2.add("第一周");
        list3.add("第二周");
        list4.add("第三周");
        list5.add("第四周");
        for(Map towerCrane:towerCranes){
            map.put("equId",towerCrane.get("assetId"));
            List<Integer> list = getBaseMapper().getJSZTDCS(map);
            list1.add((String)towerCrane.get("assetName"));
            list2.add(list.get(0));
            list3.add(list.get(1));
            list4.add(list.get(2));
            list5.add(list.get(3));

        }
        res.add(list1);
        res.add(list2);
        res.add(list3);
        res.add(list4);
        res.add(list5);
        return res;
    }
}
