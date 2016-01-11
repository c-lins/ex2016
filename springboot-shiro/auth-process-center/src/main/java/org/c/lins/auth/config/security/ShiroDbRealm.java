package org.c.lins.auth.config.security;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.c.lins.auth.entity.User;
import org.c.lins.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

/**
 * Created by lins on 15-12-21.
 */
//@Component
public class ShiroDbRealm extends AuthorizingRealm {
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;

//    @Autowired
//    private SessionDAO sessionDAO;
    @Autowired
    private UserService userService;

    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(HASH_ALGORITHM);
        //matcher.setHashIterations(HASH_INTERATIONS);

        setCredentialsMatcher(matcher);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        User user = userService.findByLoginName(token.getUsername());
        if (user != null) {

            return new SimpleAuthenticationInfo(user, user.loginName, getName());
        } else {
            throw new UnknownAccountException();
        }

    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User shiroUser = (User) principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //  0：超级管理员 1：一般用户
//        if ("0".equals(shiroUser.getUserType())) {
//            // 基于Role的权限信息
//            info.addRole("admin");
//
//            // 基于Permission的权限信息
//            info.addStringPermissions(new ArrayList<String>(){{add("sys:all:all");}});
//        }else{
//            // 基于Role的权限信息
//            info.addRole("other");
//
//            // 基于Permission的权限信息
//            info.addStringPermissions(new ArrayList<String>(){{add("sys:self:all");}});
//        }
        return info;
    }

}
