package com.example.lwp.shiro.shirodemo;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class TestCustomRealm extends BaseTest {

    @Test
    public void testSingleRealm() {
        this.shiroInit("shiro-realm.ini");

        //3、得到 Subject 及创建用户名/密码身份验证 Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "1234");

        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            //5、身份验证失败
        }
        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录
        //6、退出
        subject.logout();
    }

    @Test
    public void testMultiRealm() {
        this.shiroInit("shiro-realms.ini");

        //3、得到 Subject 及创建用户名/密码身份验证 Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");

        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            //5、身份验证失败
        }
        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录
        //6、退出
        subject.logout();
    }

    /**
     * jdbc-realm 默认的 sql 语句
     *
     * DEFAULT_AUTHENTICATION_QUERY = "select password from users where username = ?"
     * DEFAULT_SALTED_AUTHENTICATION_QUERY = "select password, password_salt from users where username = ?"
     * DEFAULT_USER_ROLES_QUERY = "select role_name from user_roles where username = ?"
     * DEFAULT_PERMISSIONS_QUERY = "select permission from roles_permissions where role_name = ?";
     *
     *     protected String authenticationQuery = DEFAULT_AUTHENTICATION_QUERY;
     *
     *     protected String userRolesQuery = DEFAULT_USER_ROLES_QUERY;
     *
     *     protected String permissionsQuery = DEFAULT_PERMISSIONS_QUERY;
     */
    @Test
    public void testJDBCRealm() {
        this.shiroInit("shiro-jdbc-realm.ini");

        //3、得到 Subject 及创建用户名/密码身份验证 Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");

        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            //5、身份验证失败
        }
        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录
        //6、退出
        subject.logout();
    }
}
