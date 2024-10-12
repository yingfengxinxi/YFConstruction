package com.kakarote.build.project.service.impl;

import com.kakarote.build.project.entity.PO.ProgressPlanDayFillin;
import com.kakarote.build.project.mapper.ProgressPlanDayFillinMapper;
import com.kakarote.build.project.service.IProgressPlanDayFillinService;
import com.kakarote.build.utils.DateUtil;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 计划管理---每日填报 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-07-16
 */
@Service
public class ProgressPlanDayFillinServiceImpl extends BaseServiceImpl<ProgressPlanDayFillinMapper, ProgressPlanDayFillin> implements IProgressPlanDayFillinService {

    @Override
    public List<Map> selectByMap(Map map) throws Exception{
        List<Map> mapsAll  = new ArrayList<>();

        List<Map> maps = getBaseMapper().selectByMap(map);

        if(maps.size() == 1 && String.valueOf(maps.get(0).get("id")).equals("null")){
            Map map1 = maps.get(0);
            String weekStartDate = String.valueOf(map1.get("planStartDate"));
            String weekEndDate = String.valueOf(map1.get("planEndDate"));
            Long twoDateNum = DateUtil.getTwoDateNum(weekStartDate, weekEndDate);
//            String weekPlanIndex = String.valueOf(map1.get("weekPlanIndex"));

            for(int i = 0; i <= twoDateNum; i++){
                Map map2 = new HashMap();
                String relatedBuildingId = String.valueOf(map1.get("relatedBuildingId"));
                if(relatedBuildingId != null && !relatedBuildingId.equals("") && !relatedBuildingId.equals("null") ){
                    String[] split = relatedBuildingId.split(",");
                    List<Integer> list1 = new ArrayList<>();
                    for(String string : split){
                        Integer integer = Integer.valueOf(string);
                        list1.add(integer);
                    }
                    map2.put("relatedBuildingId",list1);
                }


                map2.put("name",map1.get("name"));
                map2.put("planWeekId",map1.get("planWeekId"));
//                map2.put("weekPlanIndex",map1.get("weekPlanIndex"));

                String s = DateUtil.addDate(weekStartDate, i);

                map2.put("dayDate",s);

                mapsAll.add(map2);
            }
        }else {
            for(Map map1 : maps){
                String relatedBuildingId = String.valueOf(map1.get("relatedBuildingId"));
                if(relatedBuildingId != null && !relatedBuildingId.equals("") && !relatedBuildingId.equals("null") ){
                    String[] split = relatedBuildingId.split(",");
                    List<Integer> list1 = new ArrayList<>();
                    for(String string : split){
                        Integer integer = Integer.valueOf(string);
                        list1.add(integer);
                    }
                    map1.put("relatedBuildingId",list1);
                }
            }
            mapsAll = maps;
        }
        return mapsAll;
    }
}
