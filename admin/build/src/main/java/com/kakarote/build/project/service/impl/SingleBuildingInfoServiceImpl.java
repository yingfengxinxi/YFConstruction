package com.kakarote.build.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.project.entity.PO.SingleBuildingInfo;
import com.kakarote.build.project.mapper.SingleBuildingInfoMapper;
import com.kakarote.build.project.service.IProgressPlanManageDetailsService;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.build.project.service.ISingleBuildingInfoService;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 单体楼座信息 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-03-15
 */
@Service
public class SingleBuildingInfoServiceImpl extends BaseServiceImpl<SingleBuildingInfoMapper, SingleBuildingInfo> implements ISingleBuildingInfoService {
    @Autowired
    private IProjectInfoService iProjectInfoService;
    @Autowired
    private IProgressPlanManageDetailsService iProgressPlanManageDetailsService;

    @Override
    public Map selectLoft() {
        //用于计划生成  的   明细表  的  关联楼座（楼座表）
        UserInfo userInfo = UserUtil.getUser();
        Long projectId = userInfo.getProjectId();
        QueryWrapper qu1 = new QueryWrapper();
        qu1.eq("parent_id",projectId);
        qu1.select("id", "building").orderByDesc("create_time");
        List<Map> list1 = list(qu1);

        //用于计划生成  的   主表  的  最高楼座（楼座表）
        QueryWrapper qu2 = new QueryWrapper();
        qu2.select("max(aboveground_number+underground_number) as maxLoft");
        Map<String,Object> map = getMap(qu2);

        //用于计划生成  的   主表  的  开始日期（项目表）
        QueryWrapper qu3 = new QueryWrapper();
        qu3.select("date_format(contract_commencement_date,'%Y-%m-%d') as ht",
                "date_format(actual_commencement_date,'%Y-%m-%d') as sj",
                "date_format(commencement_order_date,'%Y-%m-%d') as kgl");
        List list = iProjectInfoService.getBaseMapper().selectMaps(qu3);

        //用于计划生成  的  子表  的  新增节点（获取最小节点）
        QueryWrapper qu4 = new QueryWrapper();
        qu4.select("min(mdid) mdid").ne("project_id", 0);
        qu4.ne("tenant_id", 0);
        Map o = (Map) iProgressPlanManageDetailsService.getBaseMapper().selectMaps(qu4).get(0);


        Map<String,Object> mapAll = new HashMap<>();
        mapAll.put("loftList",list1);
        if(map != null) {
            mapAll.put("maxLoft",map.get("maxLoft"));
        }
        mapAll.put("date",list);
        mapAll.put("minMdid",o.get("mdid"));

        return mapAll;
    }

    @Override
    public Map<String, Object> getBuildingNumberByPro() {
        Map<String, Object> map = getBaseMapper().getBuildingNumberByPro();
        return map;
    }

    @Override
    public List<Map<String, Object>> selectListByPro() {
        List<Map<String, Object>> list = getBaseMapper().selectListByPro();
        return list;
    }

    @Override
    public SingleBuildingInfo getInfoById(Integer id) {
        return getBaseMapper().getInfoById(id);
    }
}
