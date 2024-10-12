package com.qdoner.datarecept.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.qdoner.datarecept.entity.BO.ProjectUserBO;
import com.qdoner.datarecept.entity.PO.ProjectUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface ProjectUserMapper extends BaseMapper<ProjectUser> {

    BasePage<Map> queryPageListGrp(BasePage<ProjectUser> page, @Param("data") ProjectUserBO projectUserBO);

    /**
     * 当前用户项目列表
     *
     * @param userId
     * @return
     */
    @Select("select b.*,p.project_name as projectName from b_project_user b,b_project_info p" +
            " where b.user_id = #{userId} and b.project_id = p.id ")
    List<ProjectUser> userProListGrp(@Param("userId") Long userId);

    @Update("update wk_admin_user set project_id = null where user_id = #{userId} and project_id = #{projectId}")
    int removeUserProjectGrp(@Param("userId") Integer userId, @Param("projectId") Integer projectId);

    @Select("select * from b_project_user where id = #{id}")
    ProjectUser getByIdGrp(@Param("id") String id);

    @Delete("delete from b_project_user where id = #{id}")
    int removeByIdGrp(@Param("id") String id);

    BasePage<Map> queryProPageListGrp(BasePage<Object> parse, @Param("data") ProjectUserBO projectUserBO);
}