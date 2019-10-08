package com.example.lwp.shiro.shirodemo;

/**
 * @author lanwp
 * @Date 2019/9/23 22:40
 */
public class JdbcExample {
    public static void main(String[] args) throws ClassNotFoundException {
        String driverClass = "com.mysql.jdbc.Driver";
        String connectStr = "jdbc:mysql://localhost:3306/test";
        String sql = "select id, title, content, create_time from blog";
        Class.forName(driverClass);
    }
}
