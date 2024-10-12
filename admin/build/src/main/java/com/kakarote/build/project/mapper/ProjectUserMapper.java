package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.BO.ProjectUserBO;
import com.kakarote.build.project.entity.PO.ProjectUser;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-04-12
 */
public interface ProjectUserMapper extends BaseMapper<ProjectUser> {

    BasePage<Map> queryPageListGrp(BasePage<ProjectUser> page, @Param("data") ProjectUserBO projectUserBO);

    /**
     * 当前用户项目列表
     *
     * @param userId
     * @return
     */
    @Select("select b.*,IFNULL(p.project_abbreviation, p.project_name) as projectName from b_project_user b,b_project_info p" +
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
