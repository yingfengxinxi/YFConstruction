package com.kakarote.build.project.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.entity.PO.SingleBuildingInfo;
import com.kakarote.build.project.service.ISingleBuildingInfoService;
import com.kakarote.core.common.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 单体楼座信息 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-03-15
 */
@RestController
@RequestMapping("/singleBuildingInfo")
public class SingleBuildingInfoController {

    @Autowired
    private ISingleBuildingInfoService iSingleBuildingInfoService;

    /**
    * @Description:新增楼座
    * @author shz
    * @date 2021-4-2 15:52
    * @param 
    * @return 
    */
    @PostMapping("/add")
    @ApiOperation("新增楼座")
    @OperateLog(detail = "新增楼座",types= LogTypes.BUILD ,behavior = LogBehavior.INSERT)
    public Result save(@RequestBody SingleBuildingInfo singleBuildingInfo){
        iSingleBuildingInfoService.saveOrUpdate(singleBuildingInfo);
        return R.ok();
    }
    @PostMapping("/selectByProjectId/{id}")
    @ApiOperation("楼座查询")
    @OperateLog(detail = "楼座查询",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectByProjectId(@PathVariable Integer id){
        QueryWrapper<SingleBuildingInfo> wrapper = new QueryWrapper<SingleBuildingInfo>();
        wrapper.eq("parent_id",id);
        wrapper.orderByAsc("sort");
        List<SingleBuildingInfo> list = iSingleBuildingInfoService.list(wrapper);
        return R.ok(list);
    }

    @PostMapping("/removeById/{id}")
    @ApiOperation("删除楼座信息")
    @OperateLog(detail = "删除楼座信息",types= LogTypes.BUILD ,behavior = LogBehavior.DELETE)
    public Result removeById(@PathVariable Integer id){
        iSingleBuildingInfoService.removeById(id);
        return R.ok();
    }

    @PostMapping("/selectLoft")
    @ApiOperation("查询楼座信息，用于计划生成")
    @OperateLog(detail = "查询楼座信息，用于计划生成",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectLoft(){
        Map map = iSingleBuildingInfoService.selectLoft();
        return R.ok(map);
    }

    /**
     * 工地端-三维大屏使用
     * @return
     */
    @PostMapping("/selectListByPro")
    @ApiOperation("查询当前项目的主体建筑列表")
    @OperateLog(detail = "查询当前项目的主体建筑列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectListByPro() {

        List<Map<String, Object>> projectList = iSingleBuildingInfoService.selectListByPro();
        return R.ok(projectList);
    }


    @PostMapping("/selectById/{id}")
    @ApiOperation("根据id查询楼座信息")
    @OperateLog(detail = "根据id查询楼座信息",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectById(@PathVariable Integer id){
        SingleBuildingInfo singleBuildingInfo = iSingleBuildingInfoService.getInfoById(id);
        return R.ok(singleBuildingInfo);
    }
}

