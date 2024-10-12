package com.kakarote.build.labour.service.impl;

import com.kakarote.build.labour.entity.PO.BLabour5gvideo;
import com.kakarote.build.labour.entity.PO.BLabourInspection;
import com.kakarote.build.labour.entity.PO.BLabourInspectionResults;
import com.kakarote.build.labour.mapper.BLabourInspectionResultsMapper;
import com.kakarote.build.labour.service.IBLabourInspectionResultsService;
import com.kakarote.build.labour.service.IBLabourInspectionService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 企业报验结果表 服务实现类
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
@Service
public class BLabourInspectionResultsServiceImpl extends BaseServiceImpl<BLabourInspectionResultsMapper, BLabourInspectionResults> implements IBLabourInspectionResultsService {


    @Autowired
    private IBLabourInspectionService iBLabourInspectionService;
    /**
     * 新增数据
     *
     */
    @Override
    public String add(BLabourInspectionResults bLabourInspectionResults)
    {
        bLabourInspectionResults.setCreateTime(new Date());
        save(bLabourInspectionResults);
        BLabourInspection bLabourInspection =iBLabourInspectionService.getById(bLabourInspectionResults.getInspectionId());
        if("0".equals(bLabourInspection.getRectificationInfo())){
            bLabourInspection.setInspectionType("2");
        }else{
            bLabourInspection.setInspectionType("3");
        }
        iBLabourInspectionService.edit(bLabourInspection);
        return  "1";
    }
    public BLabourInspectionResults getPid(String pid){
        BLabourInspectionResults bLabourInspectionResults =new BLabourInspectionResults();
        bLabourInspectionResults = getBaseMapper().getPid(pid);
        return bLabourInspectionResults;
    }
}
