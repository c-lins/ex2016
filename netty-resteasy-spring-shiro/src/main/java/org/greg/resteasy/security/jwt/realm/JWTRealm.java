package org.greg.resteasy.security.jwt.realm;


import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import org.greg.resteasy.entity.Role;
import org.greg.resteasy.entity.User;
import org.greg.resteasy.security.ShiroUser;
import org.greg.resteasy.security.jwt.JWTToken;
import org.greg.resteasy.security.jwt.filter.JWTAuthenticationToken;
import org.greg.resteasy.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;


public class JWTRealm extends AuthorizingRealm {

//    @Autowired
//    private AccountService accountService;
    @Autowired
    private JWTToken tokens;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof JWTAuthenticationToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        JWTAuthenticationToken upToken = (JWTAuthenticationToken) token;
//        User user = accountService.findById(Long.parseLong(upToken.getUserId()+""));
        User user = new User();
        user.id=1L;
        user.aliasName="张三";
        user.loginName="yyw01";
        user.salt="b6682f6ff51ca51e";
        user.email="linchao@111.com.cn";
        user.hashPassword="eea645f601a395e800163c89d18241a060a2d826";

        if (user != null && tokens.validateToken(upToken.getToken())) {
            SimpleAccount account = new SimpleAccount(user, upToken.getToken(), getName());
//            for (Role role : user.getRoles()) {
                // 基于Role的权限信息
                account.addRole("system");

                // 基于Permission的权限信息
//            info.addStringPermissions(Lists.newArrayList("SHOW:UNOPENED"));
//            }
            return account;
        }

        return null;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
//        User user = accountService.findUserByLoginName(shiroUser.loginName);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        for (Role role : user.getRoles()) {
//            // 基于Role的权限信息
            info.addRole("system");
//
//            // 基于Permission的权限信息
////            info.addStringPermissions(Lists.newArrayList("SHOW:UNOPENED"));
//        }
        return info;
    }

}
