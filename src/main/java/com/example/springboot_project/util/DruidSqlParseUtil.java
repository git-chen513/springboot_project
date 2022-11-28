package com.example.springboot_project.util;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;

import java.sql.SQLSyntaxErrorException;
import java.util.List;

/**
 * druid sql解析
 *
 * 参考文章：
 * http://t.zoukankan.com/heyang78-p-11451814.html
 * https://juejin.cn/post/7045473806508523557
 * https://github.com/alibaba/druid/wiki/SQL-Parser
 * https://github.com/alibaba/druid/wiki/Druid_SQL_AST
 * https://github.com/alibaba/druid/wiki/SQL_Parser_Demo_visitor
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/11/28 11:28
 */
public class DruidSqlParseUtil {

    public static void main(String[] args) throws SQLSyntaxErrorException {
        testVisitor();
    }

    public static void test1() {
        String sql = "select id, name from stu where id = 10";
        String dbType = "mysql";
        System.out.println("原始SQL为：" + sql);
        List<SQLStatement> list = SQLUtils.parseStatements(sql, dbType);
        SQLSelectStatement statement = (SQLSelectStatement) list.get(0);
        SQLSelect select = statement.getSelect();
        SQLSelectQueryBlock query = (SQLSelectQueryBlock) select.getQuery();
        SQLExprTableSource tableSource = (SQLExprTableSource) query.getFrom();
        String tableName = tableSource.getExpr().toString();
        System.out.println("获取的表名为：" + tableName);
        //修改表名为stu_1
        tableSource.setExpr("stu_1");
        System.out.println("修改表名后的SQL为 ： [" + statement + "]");
    }

    public static void test2() {
        String sql = "select id, name from stu union select id, name from teacher";
        String dbType = "mysql";
        List<SQLStatement> list = SQLUtils.parseStatements(sql, dbType);
        SQLSelectStatement statement = (SQLSelectStatement) list.get(0);
        SQLSelect select = statement.getSelect();
        SQLSelectQuery query = select.getQuery();
        SQLUnionQuery sqlUnionQuery = (SQLUnionQuery) query;
        List<SQLSelectQuery> relations = sqlUnionQuery.getRelations();
        for (SQLSelectQuery sqlSelectQuery : relations) {
            SQLTableSource sqlTableSource = ((SQLSelectQueryBlock) sqlSelectQuery).getFrom();
            SQLExpr expr = ((SQLExprTableSource) sqlTableSource).getExpr();
            System.out.println(expr.toString());
        }
    }

    /***
     * 测试sql语法出错的情况，比如select、from、where等关键字出错
     * 注意：对于字段名不存在、表名不存在等错误，不属于sql解析的范畴，也就说sql解析工具无法识别这种类型的错误
     *
     * sql解析阶段属于sql编译的一部分操作，sql解析由词法分析和语法/语义分析两个部分组成
     * 词法分析主要是把输入的sql语句转化成一个个Token，可以识别sql语法出错的情况
     * 语法/语义分析主要是生成语法树，即将第一步词法分析的结果构建成具有层级结构的抽象语法树
     *
     */
    public static void test3() {
        String sql = "selects name from stu where id = 10";
        String dbType = "mysql";
        SQLUtils.parseStatements(sql, dbType);
    }

    public static void testVisitor() {
        String sql = "select id, name from A left join B on A.id = B.id";
        String dbType = "mysql";
        SQLSelectStatement statement = (SQLSelectStatement) SQLUtils.parseStatements(sql, dbType).get(0);
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        statement.accept(visitor);
        System.out.println(visitor.getTables().toString());
    }
}
