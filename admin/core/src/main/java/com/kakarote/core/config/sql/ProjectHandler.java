/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.kakarote.core.config.sql;

import net.sf.jsqlparser.expression.Expression;

/**
 * 项目处理器（ ProjectId 行级 ）
 *
 * @author wudw
 * @since 2021-04-12
 */
public interface ProjectHandler {

    /**
     * 获取项目 ID 值表达式，支持多个 ID 条件查询
     * <p>
     * 支持自定义表达式，比如：Project_id in (1,2) @since 2019-8-2
     *
     * @param where 参数 true 表示为 where 条件 false 表示为 insert 或者 select 条件
     * @return 项目 ID 值表达式
     */
    Expression getProjectId(boolean where);

    /**
     * 获取项目字段名
     *
     * @return 项目字段名
     */
    String getProjectIdColumn();

    /**
     * 根据表名判断是否进行过滤
     *生效 action 级别
     * @param tableName 表
     * @param where 查询条件
     * @return 是否进行过滤, true:表示忽略，false:需要解析多项目字段
     */
    boolean doTableFilter(String tableName, Expression where);
}
