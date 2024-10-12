package com.qdoner.workflow.entity.BO;

import com.kakarote.core.entity.PageEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyExamineBO extends PageEntity {
    private Integer status;

    private Integer categoryType;
}
