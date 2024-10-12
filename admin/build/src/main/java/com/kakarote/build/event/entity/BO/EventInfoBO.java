package com.kakarote.build.event.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EventInfoBO extends PageEntity {

    @ApiModelProperty(value = "查询条件 事件状态 0：待分派、1：办理中、2：待审核、3：退回、4：完成 5:查询 办理中和退回数据")
    private String eventStatus;

    @ApiModelProperty(value = "项目名称 or 项目描述关键字")
    private String keyword;
}
