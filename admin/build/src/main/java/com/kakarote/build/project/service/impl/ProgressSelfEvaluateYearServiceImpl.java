package com.kakarote.build.project.service.impl;

import com.kakarote.build.project.entity.BO.ProgressSelfEvaluateYearBO;
import com.kakarote.build.project.entity.PO.ProgressSelfEvaluateYear;
import com.kakarote.build.project.mapper.ProgressSelfEvaluateYearMapper;
import com.kakarote.build.project.service.IProgressSelfEvaluateYearService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 年度自评 服务实现类
 * </p>
 *
 * @author shz
 * @since 2022-06-17
 */
@Service
public class ProgressSelfEvaluateYearServiceImpl extends BaseServiceImpl<ProgressSelfEvaluateYearMapper, ProgressSelfEvaluateYear> implements IProgressSelfEvaluateYearService {
    @Override
    public BasePage<ProgressSelfEvaluateYearBO> selectList(ProgressSelfEvaluateYearBO selfEvaluateBO) {
        return getBaseMapper().selectList(selfEvaluateBO.parse(),selfEvaluateBO);
    }

    @Override
    public List<ProgressSelfEvaluateYearBO> getEvaluateList(String id) {
        return getBaseMapper().getEvaluateList(id);
    }

    @Override
    public Map getPrintData(String id) {
        Map res = new HashMap();

        List<ProgressSelfEvaluateYearBO> list = getBaseMapper().getEvaluateList(id);
        res.put("securityEvaluation",list.get(0).getSecurityName());
        List<Map> resList = new ArrayList<>();
        int i = 1;
        for(ProgressSelfEvaluateYearBO selfEvaluateBO:list){
            Map map = new HashMap();
            map.put("a",i);
            map.put("b",selfEvaluateBO.getSecurityEvaluationName());
            map.put("c",selfEvaluateBO.getSecurityEvaluationFunction());
            map.put("d",selfEvaluateBO.getSecurityEvaluationStandard());
            map.put("e",selfEvaluateBO.getShouldScore());
            map.put("f",selfEvaluateBO.getMark());
            map.put("g",(selfEvaluateBO.getShouldScore() - selfEvaluateBO.getMark()));
            i++;
            resList.add(map);
        }
        Integer markTotal = list.stream().mapToInt(ProgressSelfEvaluateYearBO::getMark).sum();
        Integer shouldScoreTotal = list.stream().mapToInt(ProgressSelfEvaluateYearBO::getShouldScore).sum();
        Map zjmap = new HashMap();
        zjmap.put("h"," ");
        zjmap.put("i","总计");
        zjmap.put("j"," ");
        zjmap.put("k",shouldScoreTotal);
        zjmap.put("l",markTotal);
        zjmap.put("m",shouldScoreTotal - markTotal);
        i++;
        resList.add(zjmap);
        res.put("detailList",resList);
        return res;
    }
}
