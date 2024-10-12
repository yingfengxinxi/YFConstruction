package com.kakarote.build.labour.apipm.controller;


import com.kakarote.build.labour.apipm.service.ITeamDataService;
import com.kakarote.build.labour.entity.BO.LabourTeamBO;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.entity.PO.LabourTeam;
import com.kakarote.build.labour.service.ILabourInfocollectionService;
import com.kakarote.build.labour.service.ILabourTeamService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 劳务管理--班组 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-03-19
 */
@RestController
@RequestMapping("/apiLabour/teamData")
public class LabourTeamDataController {

    @Autowired
    private ITeamDataService iTeamDataService;

    /**
     * 传输新增班组数据
     * @param parpam
     * @return
     */
    @PostMapping("/post")
    @ApiOperation("班组数据新增")
    public Result teamDataAdd(@RequestBody Map<String, Object> parpam) {
        return iTeamDataService.teamDataAdd(parpam);
    }

    /**
     * 传输修改班组数据
     * @param parpam
     * @return
     */
    @PostMapping("/put")
    @ApiOperation("班组数据修改")
    public Result teamDataEdit(@RequestBody Map<String, Object> parpam) {
        return iTeamDataService.teamDataEdit(parpam);
    }

    /**
     * 班组数据删除
     * @param parpam
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation("班组数据删除")
    public Result teamDataDelete(@RequestBody Map<String, Object> parpam) {
        return iTeamDataService.teamDataDelete(parpam);
    }

}

