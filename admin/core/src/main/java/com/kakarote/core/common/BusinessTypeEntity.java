package com.kakarote.core.common;

import lombok.Data;

@Data
public class BusinessTypeEntity {
    private Integer type;//类型

    private Integer value;//类型

    private String tableName;//表名

    private String idName;//主键

    private String colName;//备注说明

    private String name;//业务名称
}
