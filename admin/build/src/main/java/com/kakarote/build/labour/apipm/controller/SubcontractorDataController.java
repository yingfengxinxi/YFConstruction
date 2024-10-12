package com.kakarote.build.labour.apipm.controller;



import com.kakarote.build.labour.apipm.service.SubcontractorDataService;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 分包商数据接取外放接口
 * </p>
 *
 * @author author
 * @since 2022-02-18
 */
@RestController
@RequestMapping("/apiLabour/subContractor")
public class SubcontractorDataController {

    @Autowired
    private SubcontractorDataService subcontractorDataService;
    /**
     * 传输新增分包商数据
     * @param parpam
     * @return
     */
    @PostMapping("/post")
    @ApiOperation("分包商数据新增")
    public Result subcontractorAdd(@RequestBody Map<String, Object> parpam) {
        return subcontractorDataService.subcontractorAdd(parpam);
    }

    /**
     * 传输修改分包商数据
     * @param parpam
     * @return
     */
    @PostMapping("/put")
    @ApiOperation("分包商数据修改")
    public Result subcontractorEdit(@RequestBody Map<String, Object> parpam) {
        return subcontractorDataService.subcontractorEdit(parpam);
    }

    /**
     * 分包商数据删除
     * @param parpam
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation("分包商数据删除")
    public Result subcontractorDelete(@RequestBody Map<String, Object> parpam) {
        return subcontractorDataService.subcontractorDelete(parpam);
    }

    /**
     * 分包商数据查询
     * @param parpam
     * @return
     */
    @PostMapping("/get")
    @ApiOperation("分包商数据查询")
    public Result subcontractorGet(@RequestBody Map<String, Object> parpam) {
        return subcontractorDataService.subcontractorGet(parpam);
    }

}

