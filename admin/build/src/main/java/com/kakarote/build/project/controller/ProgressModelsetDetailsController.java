package com.kakarote.build.project.controller;


import com.kakarote.build.project.entity.PO.ProgressModelsetDetails;
import com.kakarote.build.project.service.IProgressModelsetDetailsService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 进度管理--模板设置--明细表 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-04-28
 */
@RestController
@RequestMapping("/progressModelsetDetails")
public class ProgressModelsetDetailsController {

    @Autowired
    private IProgressModelsetDetailsService iProgressModelsetDetailsService;

    @PostMapping("/selectByMap")
    @ApiOperation("查询信息")
    @Transactional
    @OperateLog(detail = "查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectByMap(@RequestBody Map<String,Object> map) {
        List<ProgressModelsetDetails> listByMid = iProgressModelsetDetailsService.queryByPidAndMid(map);
        return Result.ok(listByMid);
    }

    @PostMapping("/selectTree/{mid}")
    @ApiOperation("查询树结构数据--前置节点使用")
    @OperateLog(detail = "查询树结构数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectTree(@PathVariable Integer mid){
        return Result.ok(iProgressModelsetDetailsService.selectTree(mid));
    }


    @PostMapping("/selectById/{id}")
    @ApiOperation("根据id查询信息")
    @Transactional
    @OperateLog(detail = "根据id查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectById(@PathVariable String id) {
        ProgressModelsetDetails byId =
                iProgressModelsetDetailsService.
                        query().
                       select("*","case when\n" +
                               "        pid != 0\n" +
                               "        then\n" +
                               "        (select s.name from b_progress_modelset_details s where s.id = b_progress_modelset_details.pid)\n" +
                               "        else '此任务为根任务，没有父任务' end\tas pname").
                        eq("id",id)
                        .ne("tenant_id",0).ne("project_id",0)//不做租户及项目数据隔离
                        .one();
        return Result.ok(byId);
    }

    @PostMapping("/selectByPid/{pid}")
    @ApiOperation("根据pid查询信息列表")
    @Transactional
    @OperateLog(detail = "根据pid查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectByPid(@PathVariable String pid) {
        List<ProgressModelsetDetails> list = iProgressModelsetDetailsService
                .query().select("*","CASE\n" +
                        "        WHEN (\n" +
                        "        SELECT\n" +
                        "        count(1)\n" +
                        "        FROM\n" +
                        "        b_progress_modelset_details m\n" +
                        "        WHERE\n" +
                        "        m.pid = b_progress_modelset_details.id\n" +
                        "        ) = 0 THEN\n" +
                        "        false\n" +
                        "        ELSE\n" +
                        "        true\n" +
                        "        END hasChildren")
                .eq("pid", pid)
                .ne("tenant_id",0).ne("project_id",0)//不做租户及项目数据隔离
                .orderByAsc("serial_number")
                .orderByDesc("create_time")
                .list();
        return Result.ok(list);
    }

    @PostMapping("/selectByMidAll/{mid}")
    @ApiOperation("查询信息")
    @OperateLog(detail = "根据mid查询全部id和name信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectByMidAll(@PathVariable Integer mid) {
        List<ProgressModelsetDetails> listByMid = iProgressModelsetDetailsService.query().select("id","name")
                .ne("tenant_id",0).ne("project_id",0)//不做租户及项目数据隔离
                .eq("mid",mid).list();
        return Result.ok(listByMid);
    }

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @Transactional
    @OperateLog(detail = "新增信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result save(@Validated @RequestBody ProgressModelsetDetails progressModelsetDetails) {
        iProgressModelsetDetailsService.save(progressModelsetDetails);
        return Result.ok(progressModelsetDetails.getId());
    }

    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @Transactional
    @OperateLog(detail = "修改信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateById(@Validated @RequestBody ProgressModelsetDetails progressModelsetDetails) {
        boolean b = iProgressModelsetDetailsService.updateById(progressModelsetDetails);
        return Result.ok(progressModelsetDetails.getId());
    }

    @PostMapping("/delById/{id}")
    @ApiOperation("根据主键删除信息")
    @Transactional
    @OperateLog(detail = "根据主键删除信息",types = LogTypes.BUILD,behavior = LogBehavior.DELETE)
    public Result delById(@PathVariable String id){
        iProgressModelsetDetailsService.delById(id);
        return Result.ok();
    }

    @PostMapping("/selectMapByMap")
    @ApiOperation("查询信息--计划生成使用")
    @Transactional
    @OperateLog(detail = "查询信息--计划生成使用", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectMapByMap(@RequestBody Map<String,Object> map) {
        List<Map> listByMid = iProgressModelsetDetailsService.queryMapByPidAndMid(map);
        return Result.ok(listByMid);
    }

    @PostMapping("/appSelectTree/{mid}")
    @ApiOperation("查询树结构数据--app前置节点使用")
    @OperateLog(detail = "查询树结构数据--app前置节点使用",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result appSelectTree(@PathVariable Integer mid){
        return Result.ok(iProgressModelsetDetailsService.appSelectTree(mid));
    }

}

