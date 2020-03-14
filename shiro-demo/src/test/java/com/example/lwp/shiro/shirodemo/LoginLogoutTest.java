package com.example.lwp.shiro.shirodemo;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lanwp
 * @Date 2019/9/22 17:42
 */
public class LoginLogoutTest {

    private void shiroInit() {
        // 指定 shiro.ini 时 realm 使用 org.apache.shiro.realm.text.IniRealm
        //1、获取 SecurityManager 工厂，此处使用 Ini 配置文件初始化 SecurityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");

        //2、得到 SecurityManager 实例 并绑定给 SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
    }

    @Test
    public void testHelloworld() {
        this.shiroInit();

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

    @Test
    public void testLogin(){
        this.shiroInit();
        Subject currentUser = SecurityUtils.getSubject();

        Session session = currentUser.getSession();
        session.setAttribute( "someKey", "aValue" );
        System.out.println(currentUser.getSession().getAttribute("someKey"));
        if ( !currentUser.isAuthenticated() ) {
            //collect user principals and credentials in a gui specific manner
            //such as username/password html form, X509 certificate, OpenID, etc.
            //We'll use the username/password example here since it is the most common.
            //(do you know what movie this is from? ;)
            UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
            //this is all you have to do to support 'remember me' (no config - built in!):
            token.setRememberMe(true);
            currentUser.login(token);
        }

        System.out.println(currentUser.isAuthenticated());
        System.out.println(currentUser.getPrincipal());
        System.out.println(currentUser.getSession().getAttribute("someKey"));
    }

    @Test
    public void testLoginAndLogout(){
        //1、获取 SecurityManager 工厂，此处使用 Ini 配置文件初始化 SecurityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");

        //2、得到 SecurityManager 实例 并绑定给 SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        //3、得到 Subject 及创建用户名/密码身份验证 Token（即用户身份/凭证）
        Subject user = SecurityUtils.getSubject();
        // 不存在的账号，账号异常 org.apache.shiro.authc.UnknownAccountException: Realm [org.apache.shiro.realm.text.IniRealm@78691363] was unable to find account data for the submitted AuthenticationToken
        // 密码填写错误：密码异常 org.apache.shiro.authc.IncorrectCredentialsException: Submitted credentials for token [org.apache.shiro.authc.UsernamePasswordToken
//        UsernamePasswordToken token = new UsernamePasswordToken("test", "123456");
        UsernamePasswordToken token = new UsernamePasswordToken("root", "123456");

        // 当前主体是否通过认证
        System.out.println(user.isAuthenticated());
//        token.setRememberMe(true);
        try {
            user.login(token);
            // 当前主体是否通过认证
            System.out.println(user.isAuthenticated());
            if (user.isAuthenticated()) {
                // 通过认证
                System.out.println("登录成功");
                // hasAllRoles , hasRole, hasRoles(返回一个boolean结果数组)
                if (user.hasRole("admin")) {
                    System.out.println("有admin角色");
                } else {
                    System.out.println("没有admin角色");
                }

                if (user.isPermitted("search")) {
                    System.out.println("有 search 权限");
                } else {
                    System.out.println("没有 search 权限");
                }
                if (user.isPermitted("del")) {
                    System.out.println("有 del 权限");
                } else {
                    System.out.println("没有 del 权限");
                }

            }
        } catch (AuthenticationException e) {
//            e.printStackTrace();
            System.out.println("用户名或密码错误，登录失败");
        } finally {
        }

    }
}