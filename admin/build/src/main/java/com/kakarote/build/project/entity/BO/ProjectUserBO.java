package com.kakarote.build.project.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("项目授权对象")
public class ProjectUserBO extends PageEntity {

    private Integer projectId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    private List<Integer> users;


    /**
     * @description 根据人员授权项目
     * @author wnj58 
     * @Params 
     * @updateTime 2022/5/17 10:30
     * @return 
     * @throws 
     */
    private Integer userId;
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    private List<Integer> projects;
}
