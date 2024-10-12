package com.kakarote.build.project.controller;


import com.kakarote.build.project.entity.BO.ProjectInfoBO;
import com.kakarote.build.project.entity.BO.ProjectUserBO;
import com.kakarote.build.project.entity.PO.ProjectUser;
import com.kakarote.build.project.service.IProjectUserService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-04-12
 */
@RestController
@RequestMapping("/projectUser")
public class ProjectUserController {
    @Autowired
    IProjectUserService projectUserService;


    /**
     * @param
     * @return
     * @Description:分页查询
     * @author wudw
     * @date 2021-3-16 9:59
     */
    @PostMapping("/queryPageList")
    @ApiOperation("查询列表页数据")
    @OperateLog(detail = "查询列表页数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map>> queryPageList(@RequestBody ProjectUserBO projectUserBO) {
        BasePage<Map> projectUserVOBasePage = projectUserService.queryPageList(projectUserBO);
        return R.ok(projectUserVOBasePage);
    }

    /**
     * @param
     * @return
     * @Description:分页查询
     * @author wudw
     * @date 2021-3-16 9:59
     */
    @PostMapping("/queryPageListAll")
    @ApiOperation("查询数据")
    @OperateLog(detail = "查询数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<ProjectUser>> queryPageListAll(@RequestBody ProjectUserBO projectUserBO) {
        List<ProjectUser> list = projectUserService.lambdaQuery().eq(ProjectUser::getProjectId, projectUserBO.getProjectId()).list();
        return R.ok(list);
    }

    @PostMapping("/delUser/{id}")
    @ApiOperation("删除授权用户")
    @OperateLog(detail = "删除授权用户", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delUser(@PathVariable String id) {
        return projectUserService.removeUserProject(id);
    }

    @PostMapping("/saveUsers")
    @ApiOperation("保存授权用户")
    @OperateLog(detail = "保存授权用户", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result saveUsers(@RequestBody ProjectUserBO projectUserBO) {
        List<Integer> users = projectUserBO.getUsers();
        Integer projectId = projectUserBO.getProjectId();
        if (users == null || projectId == null) {
            return Result.error(-1,"参数不足");
        }
        List<ProjectUser> list = new ArrayList<>();
        for(Integer userId : users){
            ProjectUser user = new ProjectUser();
            user.setId(UUID.randomUUID().toString());
            user.setProjectId(projectId);
            user.setUserId(userId);
            list.add(user);
        }
        projectUserService.saveBatch(list);
        return Result.ok();
    }

    @PostMapping("/userProList")
    @ApiOperation("用户项目列表")
    @OperateLog(detail = "用户项目列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<ProjectUser>> userProList() {
        Long userId = UserUtil.getUserId();
        List<ProjectUser> list = projectUserService.userProList(userId);
        return Result.ok(list);
    }


    /**
     * @description 根据人员查询授权项目列表，分页查询
     * @author wnj58 
     * @Params [projectUserBO]
     * @updateTime 2022/5/17 10:29
     * @return com.kakarote.core.common.Result<com.kakarote.core.entity.BasePage<java.util.Map>>
     * @throws 
     */
    @PostMapping("/queryProPageList")
    @ApiOperation("人员授权项目列表分页查询")
    @OperateLog(detail = "人员授权项目列表分页查询", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map>> queryProPageList(@RequestBody ProjectUserBO projectUserBO) {
        BasePage<Map> projectUserVOBasePage = projectUserService.queryProPageList(projectUserBO);
        return R.ok(projectUserVOBasePage);
    }

   /**
    * @description 根据人员查询授权项目列表，全部
    * @author wnj58 
    * @Params [projectUserBO]
    * @updateTime 2022/5/17 14:10
    * @return com.kakarote.core.common.Result<java.util.List<com.kakarote.build.project.entity.PO.ProjectUser>>
    * @throws 
    */
    @PostMapping("/queryProListAll")
    @ApiOperation("人员查询授权项目列表")
    @OperateLog(detail = "人员查询授权项目列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<ProjectUser>> queryProListAll(@RequestBody ProjectUserBO projectUserBO) {
        List<ProjectUser> list = projectUserService.lambdaQuery().eq(ProjectUser::getUserId, projectUserBO.getUserId()).list();
        return R.ok(list);
    }

    @PostMapping("/saveBindProjects")
    @ApiOperation("保存授权项目")
    @OperateLog(detail = "保存授权项目", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result saveBindProjects(@RequestBody ProjectUserBO projectUserBO) {
        List<Integer> projects = projectUserBO.getProjects();
        Integer userId = projectUserBO.getUserId();
        if (projects == null || userId == null) {
            return Result.error(-1,"参数不足");
        }
        List<ProjectUser> list = new ArrayList<>();
        for(Integer projectId : projects){
            ProjectUser user = new ProjectUser();
            user.setId(UUID.randomUUID().toString());
            user.setProjectId(projectId);
            user.setUserId(userId);
            list.add(user);
        }
        projectUserService.saveBatch(list);
        return Result.ok();
    }
}

