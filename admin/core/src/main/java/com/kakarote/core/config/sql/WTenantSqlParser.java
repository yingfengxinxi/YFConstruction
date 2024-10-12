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

import com.baomidou.mybatisplus.core.parser.AbstractJsqlParser;
import com.baomidou.mybatisplus.core.parser.SqlInfo;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.kakarote.core.config.DataIsolationConfig;
import com.kakarote.core.utils.WildcardMatchUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;

import java.util.List;

/**
 * 项目 SQL 解析器（ TenantId 行级 ）
 *
 * @author hubin
 * @since 2017-09-01
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WTenantSqlParser extends AbstractJsqlParser {

    private WTenantHandler wTenantHandler;

    /**
     * 解析 SQL 方法
     *
     * @param metaObject 元对象
     * @param sql        SQL 语句
     * @return SQL 信息
     */

    @Override
    public SqlInfo parser(MetaObject metaObject, String sql) {
        if (this.allowProcess(metaObject)) {
            try {
                //logger.debug("Original SQL: " + sql);
                // fixed github pull/295
                StringBuilder sqlStringBuilder = new StringBuilder();
                Statements statements = CCJSqlParserUtil.parseStatements(sql);
                int i = 0;
                for (Statement statement : statements.getStatements()) {
                    if (null != statement) {
                        if (i++ > 0) {
                            sqlStringBuilder.append(';');
                        }
                        sqlStringBuilder.append(this.processParser(statement).getSql());
                    }
                }
                if (sqlStringBuilder.length() > 0) {
                    return SqlInfo.newInstance().setSql(sqlStringBuilder.toString());
                }
            } catch (JSQLParserException e) {
                throw ExceptionUtils.mpe("Failed to process, please exclude the tableName or statementId.\n Error SQL: %s", e, sql);
            }
        }
        return null;
    }

    /**
     * 执行 SQL 解析
     *
     * @param statement JsqlParser Statement
     * @return
     */
    @Override
    public SqlInfo processParser(Statement statement) {
        if (statement instanceof Insert) {
            this.processInsert((Insert) statement);
        } else if (statement instanceof Select) {
            this.processSelectBody(((Select) statement).getSelectBody());
        } else if (statement instanceof Update) {
            this.processUpdate((Update) statement);
        } else if (statement instanceof Delete) {
            this.processDelete((Delete) statement);
        }
        //logger.debug("parser sql: " + statement.toString());
        return SqlInfo.newInstance().setSql(statement.toString());
    }

    /**
     * 是否拦截parser
     * 生效 CLASS 级别
     *
     * @param metaObject
     * @param sql
     * @return
     */
    @Override
    public boolean doFilter(MetaObject metaObject, String sql) {
        MappedStatement ms = SqlParserHelper.getMappedStatement(metaObject);
        //ms.getId  参考：com.kakarote.build.labour.mapper.LabourSpeworktypeMapper.selectTabsByInfo
        //获取mapper名称和action名称
        String[] emps = ms.getId().split("\\.");
        String mapperName = emps[emps.length - 2];
        String actionName = emps[emps.length - 1];
        //mapper校验
        for (String mapperMatch : DataIsolationConfig.TenantIgnoreMappers) {
            if (WildcardMatchUtil.isMatch(mapperName, mapperMatch)) {
                return false;
            }
        }
        //action 校验
        //System.out.println(actionName);
        for (String actionMatch : DataIsolationConfig.TenantIgnoreMapperActions) {
            if (WildcardMatchUtil.isMatch(mapperName + "." + actionName, actionMatch)) {
                return false;
            }
        }
        return true;
    }


    /**
     * select 语句处理
     */
    @Override
    public void processSelectBody(SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect) selectBody);
        } else if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem) selectBody;
            if (withItem.getSelectBody() != null) {
                processSelectBody(withItem.getSelectBody());
            }
        } else {
            SetOperationList operationList = (SetOperationList) selectBody;
            if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
                operationList.getSelects().forEach(this::processSelectBody);
            }
        }
    }

    /**
     * insert 语句处理
     */
    @Override
    public void processInsert(Insert insert) {

        if (wTenantHandler.doTableFilter(insert.getTable().getName(), null)) {
            // 过滤退出执行
            return;
        }
        //新增和修改需要判断columns是否包含TenantId
        List<Column> list = insert.getColumns();
        for (Column column : list) {
            if (StringUtils.containsIgnoreCase(column.getColumnName(), wTenantHandler.getTenantIdColumn())) {
                return;
            }
        }
        insert.getColumns().add(new Column(wTenantHandler.getTenantIdColumn()));
        if (insert.getSelect() != null) {
            processPlainSelect((PlainSelect) insert.getSelect().getSelectBody(), true);
        } else if (insert.getItemsList() != null) {
            // fixed github pull/295
            ItemsList itemsList = insert.getItemsList();
            if (itemsList instanceof MultiExpressionList) {
                ((MultiExpressionList) itemsList).getExprList().forEach(el -> el.getExpressions().add(wTenantHandler.getTenantId(false)));
            } else {
                ((ExpressionList) insert.getItemsList()).getExpressions().add(wTenantHandler.getTenantId(false));
            }
        } else {
            throw ExceptionUtils.mpe("Failed to process multiple-table update, please exclude the tableName or statementId");
        }
    }

    /**
     * update 语句处理
     */
    @Override
    public void processUpdate(Update update) {
        final Table table = update.getTable();
        if (wTenantHandler.doTableFilter(table.getName(), update.getWhere())) {
            // 过滤退出执行
            return;
        }
        //新增和修改需要判断columns是否包含TenantId
        List<Column> list = update.getColumns();
        for (Column column : list) {
            if (StringUtils.containsIgnoreCase(column.getColumnName(), wTenantHandler.getTenantIdColumn())) {
                return;
            }
        }
        update.setWhere(this.andExpression(table, update.getWhere()));
    }

    /**
     * delete 语句处理
     */
    @Override
    public void processDelete(Delete delete) {
        if (wTenantHandler.doTableFilter(delete.getTable().getName(), delete.getWhere())) {
            // 过滤退出执行
            return;
        }
        delete.setWhere(this.andExpression(delete.getTable(), delete.getWhere()));
    }

    /**
     * delete update 语句 where 处理
     */
    protected BinaryExpression andExpression(Table table, Expression where) {
        //获得where条件表达式
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(this.getAliasColumn(table));
        equalsTo.setRightExpression(wTenantHandler.getTenantId(true));
        if (null != where) {
            if (where instanceof OrExpression) {
                return new AndExpression(equalsTo, new Parenthesis(where));
            } else {
                return new AndExpression(equalsTo, where);
            }
        }
        return equalsTo;
    }

    /**
     * 处理 PlainSelect
     */
    protected void processPlainSelect(PlainSelect plainSelect) {
        processPlainSelect(plainSelect, false);
    }

    /**
     * 处理 PlainSelect
     *
     * @param plainSelect ignore
     * @param addColumn   是否添加项目列,insert into select语句中需要
     */
    protected void processPlainSelect(PlainSelect plainSelect, boolean addColumn) {
        FromItem fromItem = plainSelect.getFromItem();
        if (fromItem instanceof Table) {
            Table fromTable = (Table) fromItem;
            if (!wTenantHandler.doTableFilter(fromTable.getName(), plainSelect.getWhere())) {
                //#1186 github
                plainSelect.setWhere(builderExpression(plainSelect.getWhere(), fromTable));
                if (addColumn) {
                    plainSelect.getSelectItems().add(new SelectExpressionItem(
                            new Column(wTenantHandler.getTenantIdColumn())));
                }
            }
        } else {
            processFromItem(fromItem);
        }
        List<Join> joins = plainSelect.getJoins();
        if (joins != null && joins.size() > 0) {
            joins.forEach(j -> {
                processJoin(j);
                processFromItem(j.getRightItem());
            });
        }
    }

    /**
     * 处理子查询等
     */
    protected void processFromItem(FromItem fromItem) {
        if (fromItem instanceof SubJoin) {
            SubJoin subJoin = (SubJoin) fromItem;
            if (subJoin.getJoinList() != null) {
                subJoin.getJoinList().forEach(this::processJoin);
            }
            if (subJoin.getLeft() != null) {
                processFromItem(subJoin.getLeft());
            }
        } else if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                processSelectBody(subSelect.getSelectBody());
            }
        } else if (fromItem instanceof ValuesList) {
            logger.debug("Perform a subquery, if you do not give us feedback");
        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect.getSubSelect();
                if (subSelect.getSelectBody() != null) {
                    processSelectBody(subSelect.getSelectBody());
                }
            }
        }
    }

    /**
     * 处理联接语句
     */
    protected void processJoin(Join join) {
        if (join.getRightItem() instanceof Table) {
            Table fromTable = (Table) join.getRightItem();
            if (this.wTenantHandler.doTableFilter(fromTable.getName(), join.getOnExpression())) {
                // 过滤退出执行
                return;
            }
            join.setOnExpression(builderExpression(join.getOnExpression(), fromTable));
        }
    }

    /**
     * 处理条件:
     * 支持 getTenantHandler().getTenantId()是一个完整的表达式：project in (1,2)
     * 默认projectId的表达式： LongValue(1)这种依旧支持
     */
    public Expression builderExpression(Expression currentExpression, Table table) {
        final Expression tenantExpression = wTenantHandler.getTenantId(false);
        Expression appendExpression;
        if (!(tenantExpression instanceof SupportsOldOracleJoinSyntax)) {
            appendExpression = new EqualsTo();
            ((EqualsTo) appendExpression).setLeftExpression(this.getAliasColumn(table));
            ((EqualsTo) appendExpression).setRightExpression(tenantExpression);
        } else {
            appendExpression = processTableAlias4CustomizedTenantIdExpression(tenantExpression, table);
        }
        if (currentExpression == null) {
            return appendExpression;
        }
        if (currentExpression instanceof BinaryExpression) {
            BinaryExpression binaryExpression = (BinaryExpression) currentExpression;
            doExpression(binaryExpression.getLeftExpression());
            doExpression(binaryExpression.getRightExpression());
        } else if (currentExpression instanceof InExpression) {
            InExpression inExp = (InExpression) currentExpression;
            ItemsList rightItems = inExp.getRightItemsList();
            if (rightItems instanceof SubSelect) {
                processSelectBody(((SubSelect) rightItems).getSelectBody());
            }
        }
        if (currentExpression instanceof OrExpression) {
            return new AndExpression(new Parenthesis(currentExpression), appendExpression);
        } else {
            return new AndExpression(currentExpression, appendExpression);
        }
    }

    protected void doExpression(Expression expression) {
        if (expression instanceof FromItem) {
            processFromItem((FromItem) expression);
        } else if (expression instanceof InExpression) {
            InExpression inExp = (InExpression) expression;
            ItemsList rightItems = inExp.getRightItemsList();
            if (rightItems instanceof SubSelect) {
                processSelectBody(((SubSelect) rightItems).getSelectBody());
            }
        }
    }

    /**
     * 目前: 针对自定义的tenantId的条件表达式[tenant_id in (1,2,3)]，无法处理多项目的字段加上表别名
     * select a.id, b.name
     * from a
     * join b on b.aid = a.id and [b.]tenant_id in (1,2) --别名[b.]无法加上 TODO
     *
     * @param expression
     * @param table
     * @return 加上别名的多项目字段表达式
     */
    protected Expression processTableAlias4CustomizedTenantIdExpression(Expression expression, Table table) {
        //cannot add table alias for customized tenantId expression,
        // when tables including tenantId at the join table poistion
        return expression;
    }

    /**
     * 项目字段别名设置
     * <p>tableName.tenantId 或 tableAlias.tenantId</p>
     *
     * @param table 表对象
     * @return 字段
     */
    protected Column getAliasColumn(Table table) {
        StringBuilder column = new StringBuilder();
        if (null == table.getAlias()) {
            column.append(table.getName());
        } else {
            column.append(table.getAlias().getName());
        }
        column.append(StringPool.DOT);
        column.append(wTenantHandler.getTenantIdColumn());
        return new Column(column.toString());
    }
}
