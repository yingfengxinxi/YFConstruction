package com.kakarote.build.safety.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("安全日志查询对象")
public class SafetyLogBO  extends PageEntity {

    private String constructionDateSta;

    private String constructionDateEnd;


}
