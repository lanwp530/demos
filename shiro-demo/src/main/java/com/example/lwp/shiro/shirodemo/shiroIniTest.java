package com.example.lwp.shiro.shirodemo;

import com.example.lwp.shiro.shirodemo.config.shiro.MyRealm1;
import com.example.lwp.shiro.shirodemo.config.shiro.MyRealm3;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Assert;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * shiro 自定义认证策略和权限相关配置
 */
public class shiroIniTest {
    public static void main(String[] args) {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        // 模块化域身份验证
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        // 设置认证策略
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        securityManager.setAuthenticator(authenticator);

        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        authorizer.setPermissionResolver(new WildcardPermissionResolver());
        securityManager.setAuthorizer(authorizer);
        // 没有使用iniRealm 使用自定义 Realm
        securityManager.setRealm(new MyRealm1());
        SecurityUtils.setSecurityManager(securityManager);


        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            //5、身份验证失败
            e.printStackTrace();
        }
//        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录
        //6、退出
        subject.logout();
    }

    public static void test() {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        // 模块化域身份验证
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        // 设置认证策略
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        securityManager.setAuthenticator(authenticator);

        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        authorizer.setPermissionResolver(new WildcardPermissionResolver());
        securityManager.setAuthorizer(authorizer);

        /*dataSource.driverClassName=com.mysql.jdbc.Driver
        dataSource.url=jdbc:mysql://localhost:3306/shiro?useSSL=false
        dataSource.username=root*/
        // 编码方式实现自定义数据源
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/shiro?useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("");// 数据库不需要密码则传空字符串
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // 没有使用iniRealm 使用自定义 Realm
        MyRealm3 realm3 = new MyRealm3();
        realm3.setJdbcTemplate(jdbcTemplate);
        securityManager.setRealm(realm3);
        SecurityUtils.setSecurityManager(securityManager);


        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            //5、身份验证失败
            e.printStackTrace();
        }
//        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录
        //6、退出
        subject.logout();
    }

}
