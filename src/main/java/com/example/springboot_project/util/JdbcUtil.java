package com.example.springboot_project.util;

import java.sql.*;

/**
 * 原生JDBC工具类
 *
 * 1、什么是SQL注入？
 * SQL注入指的是通过字符串拼接SQL语句，造成SQL越权的问题
 * 比如定义了一条SQL语句："select * from stu where id = "
 * 本意是用户传入id值，查询出某条记录，但是由于字符串可以随意拼接，假设用户传入的变量是："1 or 1 = 1"
 * 那么拼接后的完整SQL变成："select * from stu where id = 1 or 1 = 1"
 * 由于or关键字的作用，直接把整张表的数据都查询出来了，造成越权
 *
 * 2、PreparedStatement对象如何解决SQL注入问题？
 *
 * 数据库接收到sql语句后，需要先进行编译，即进行词法/语法解析，优化sql语句，制定执行计划。
 * 多数情况下，相同的sql语句可能只是传入参数不同（如where条件后的值不同...）
 * 如果每次都需要经过上面的词法/语法解析、语句优化、制定执行计划等，则效率就明显不行了。
 * 所以预编译的优势就体现出来了，预编译语句在被MySQL编译器编译后，执行代码会被缓存下来。
 * 那么下次调用时，只要是相同的预编译语句就再不需要编译，只要将参数直接传入编译过的语句执行代码中即可。
 * 预编译语句就是将这类语句中的值用占位符替代（例如问号：?），可以视为将sql语句模板化或者说参数化。一次编译、多次运行，省去了解析优化等过程。
 * 使用预编译，而其后注入的参数将不会再进行SQL编译。也就是说其后注入进来的参数系统将不会认为它会是一条SQL命令，
 * 而默认其是一个参数，参数中的or或者and等就不是SQL语法关键字了，而是被当做纯数据处理。
 *
 * PreparedStatement就是通过预编译的方式，不仅防止了SQL注入问题，也提高了SQL执行效率
 * （因为类似"select * from stu where name = ?"这样的SQL被编译过后执行计划缓存下来，后续传递不同的参数值都是基于同一条SQL，只是参数值不同，不需要重新编译SQL）
 *
 * 3、备注
 * SQL执行计划会被缓存下来的前提是SQL语句必须一模一样
 * 如：select * from stu where id = 1; 和 select * from stu where id = 2;
 * 两条SQL即使是只有id值不同，也属于不同的SQL语句，会重复进行SQL编译、生成执行计划的过程
 * 而如果是通过一个占位符来替代，就可以将两条SQL语句抽取成模板：select * from stu where id = ?
 * 该模板第一次执行被编译过后，执行计划缓存下来，下次执行由于还是相同的SQL语句，只需要传入具体的参数，不会重复编译SQL
 */
public class JdbcUtil {
    private static String URL="jdbc:mysql://192.168.10.26:3306/test?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false";
    private static String USER="root";
    private static String PASSWROD ="test123456";

    public static void main(String[] args) throws Exception {
        testPreStatement();
    }

    /**
     * 演示Statement对象造成SQL注入的现象
     */
    public static void testStatement() throws Exception {
        String sql = "select * from stu where id = ";
        // 注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 获取数据库连接
        Connection conn = DriverManager.getConnection(URL,USER,PASSWROD);
        Statement statement = conn.createStatement();
        // 查询出整表数据
        ResultSet resultSet = statement.executeQuery(sql + "1 or 1 = 1");
        while (resultSet.next()) {
            System.out.println(resultSet.getInt("id"));
            System.out.println(resultSet.getString("name"));
        }
    }

    /**
     * 演示PreparedStatement对象通过预编译防止SQL注入的现象
     */
    public static void testPreStatement() throws Exception {
        String sql = "select * from stu where name = ?";
        // 注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 获取数据库连接
        Connection conn = DriverManager.getConnection(URL,USER,PASSWROD);
        // 预编译SQL
        PreparedStatement ps = conn.prepareStatement(sql);
        // 给占位符赋值
        ps.setObject(1, "a or 1 = 1");
        // 查询结果为空，因为表中没有任何记录的name值为："a or 1 = 1"
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getInt("id"));
            System.out.println(resultSet.getString("name"));
        }
    }
}
