package com.qdoner.workflow.entity.VO;

import com.qdoner.workflow.entity.PO.Examine;
import com.qdoner.workflow.entity.PO.ExamineStep;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("审批返回对象")
public class QueryExamineStepVO extends Examine {

    @ApiModelProperty(value = "审批用户")
    private Long examineUser;

    @ApiModelProperty(value = "审批用户名称")
    private String examineUserName;

    @ApiModelProperty(value = "审批步骤")
    private List<ExamineStep> stepList;
}
