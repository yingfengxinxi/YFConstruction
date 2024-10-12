package com.kakarote.build.labour.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Title: LabourWorktypeBO
 * @Package com.kakarote.build.labour.entity.BO
 * @Description: 描述
 * @author: ZangYu
 * @date: 2021-03-15 11:38
 */
@Data
@ApiModel("劳务管理--班组查询对象")
public class LabourTeamBO extends PageEntity {

    @ApiModelProperty(value = "分包单位")
    private String subcontractorId;

    @ApiModelProperty(value = "劳务工种")
    private List<String> workTypeId;

    @ApiModelProperty(value = "班组长名")
    private String teamLeaderName;

    @ApiModelProperty(value = "平台状态")
    private List<Integer> isSendStatus;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

}