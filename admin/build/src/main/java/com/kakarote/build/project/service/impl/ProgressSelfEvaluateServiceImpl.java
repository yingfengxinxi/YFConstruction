package com.kakarote.build.project.service.impl;

import com.kakarote.build.project.entity.BO.ProgressSelfEvaluateBO;
import com.kakarote.build.project.entity.PO.ProgressSelfEvaluate;
import com.kakarote.build.project.mapper.ProgressSelfEvaluateMapper;
import com.kakarote.build.project.service.IProgressSelfEvaluateService;
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
 * 月度自评 服务实现类
 * </p>
 *
 * @author shz
 * @since 2022-06-07
 */
@Service
public class ProgressSelfEvaluateServiceImpl extends BaseServiceImpl<ProgressSelfEvaluateMapper, ProgressSelfEvaluate> implements IProgressSelfEvaluateService {

    @Override
    public BasePage<ProgressSelfEvaluateBO> selectList(ProgressSelfEvaluateBO selfEvaluateBO) {
        return getBaseMapper().selectList(selfEvaluateBO.parse(),selfEvaluateBO);
    }

    @Override
    public List<ProgressSelfEvaluateBO> getEvaluateList(String id) {
        return getBaseMapper().getEvaluateList(id);
    }

    @Override
    public Map getPrintData(String id) {
        Map res = new HashMap();

        List<ProgressSelfEvaluateBO> list = getBaseMapper().getEvaluateList(id);
        res.put("securityEvaluation",list.get(0).getSecurityName());
        List<Map> resList = new ArrayList<>();
        List<ProgressSelfEvaluateBO> bzxmList = list.stream().filter(item  -> item.getSecurityEvaluationName().equals("保证项目"))
                .collect(Collectors.toList());
        Integer bzmarkTotal = bzxmList.stream().mapToInt(ProgressSelfEvaluateBO::getMark).sum();
        Integer bzshouldScoreTotal = bzxmList.stream().mapToInt(ProgressSelfEvaluateBO::getShouldScore).sum();
        int i = 1;
        for(ProgressSelfEvaluateBO selfEvaluateBO:bzxmList){
            Map map = new HashMap();
            map.put("a",i);
            map.put("b",selfEvaluateBO.getSecurityEvaluationName());
            map.put("c",selfEvaluateBO.getSecurityEvaluationSubitem());
            map.put("d",selfEvaluateBO.getSecurityEvaluationStandard());
            map.put("e",selfEvaluateBO.getShouldScore());
            map.put("f",selfEvaluateBO.getMark());
            map.put("g",(selfEvaluateBO.getShouldScore() - selfEvaluateBO.getMark()));
            i++;
            resList.add(map);
        }
        Map bzmap = new HashMap();
        bzmap.put("h"," ");
        bzmap.put("i","小计");
        bzmap.put("j"," ");
        bzmap.put("k",bzshouldScoreTotal);
        bzmap.put("l",bzmarkTotal);
        bzmap.put("m",bzshouldScoreTotal - bzmarkTotal);
        i++;
        resList.add(bzmap);
        List<ProgressSelfEvaluateBO> ybxmList = list.stream().filter(item  -> item.getSecurityEvaluationName().equals("一般项目"))
                .collect(Collectors.toList());
        Integer ybmarkTotal = ybxmList.stream().mapToInt(ProgressSelfEvaluateBO::getMark).sum();
        Integer ybshouldScoreTotal = ybxmList.stream().mapToInt(ProgressSelfEvaluateBO::getShouldScore).sum();
        for(ProgressSelfEvaluateBO selfEvaluateBO:ybxmList){
            Map map = new HashMap();
            map.put("a",i);
            map.put("b",selfEvaluateBO.getSecurityEvaluationName());
            map.put("c",selfEvaluateBO.getSecurityEvaluationSubitem());
            map.put("d",selfEvaluateBO.getSecurityEvaluationStandard());
            map.put("e",selfEvaluateBO.getShouldScore());
            map.put("f",selfEvaluateBO.getMark());
            map.put("g",(selfEvaluateBO.getShouldScore() - selfEvaluateBO.getMark()));
            i++;
            resList.add(map);
        }
        Map ybmap = new HashMap();
        ybmap.put("h"," ");
        ybmap.put("i","小计");
        ybmap.put("j"," ");
        ybmap.put("k",ybshouldScoreTotal);
        ybmap.put("l",ybmarkTotal);
        ybmap.put("m",ybshouldScoreTotal - ybmarkTotal);
        i++;
        resList.add(ybmap);
        Integer markTotal = list.stream().mapToInt(ProgressSelfEvaluateBO::getMark).sum();
        Integer shouldScoreTotal = list.stream().mapToInt(ProgressSelfEvaluateBO::getShouldScore).sum();
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
