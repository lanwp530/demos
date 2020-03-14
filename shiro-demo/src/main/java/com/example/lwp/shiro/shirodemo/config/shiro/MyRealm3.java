package com.example.lwp.shiro.shirodemo.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class MyRealm3 extends AuthorizingRealm {

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 权限验证调用
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("权限验证");
        String sql = "select role_name from user_roles where username = ?";
        String username = (String) principals.getPrimaryPrincipal();
        List<String> roles = jdbcTemplate.queryForList(sql, String.class, username);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(roles);
        return info;
    }

    // 登陆的时候调用
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String sql = "select password from users where username = ?";
        String username = (String) token.getPrincipal();
        String password = jdbcTemplate.queryForObject(sql, String.class, username);
        // Object principal, Object hashedCredentials, ByteSource credentialsSalt, String realmName
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, password, null, getName());
        return info;
    }
}
