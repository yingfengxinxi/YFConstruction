package com.qdoner.workflow.entity.BO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class ExamineData {
    private Integer recordId;
    private Integer status;
    private List<Long> examineLogIdList;
}
