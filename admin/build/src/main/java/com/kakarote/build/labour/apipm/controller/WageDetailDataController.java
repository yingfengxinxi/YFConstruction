package com.kakarote.build.labour.apipm.controller;


import com.kakarote.build.labour.apipm.service.IWageDetailDataService;
import com.kakarote.build.labour.apipm.service.SubcontractorDataService;
import com.kakarote.build.labour.entity.PO.BLabourWageDetail;
import com.kakarote.build.labour.service.IBLabourWageDetailService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工资管理详情 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-04-26
 */
@RestController
@RequestMapping("/apiLabour/wageDetailData")
public class WageDetailDataController {


    @Autowired
    private IWageDetailDataService iWageDetailDataService;
    /**
     * 传输新增工资数据
     * @param parpam
     * @return
     */
    @PostMapping("/post")
    @ApiOperation("工资数据新增")
    public Result wageDetailDataAdd(@RequestBody Map<String, Object> parpam) {
        return iWageDetailDataService.wageDetailDataAdd(parpam);
    }


}

