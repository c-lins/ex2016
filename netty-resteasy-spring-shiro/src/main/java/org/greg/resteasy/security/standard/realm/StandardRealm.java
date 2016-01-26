package org.greg.resteasy.security.standard.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import org.greg.resteasy.entity.Role;
import org.greg.resteasy.entity.User;
import org.greg.resteasy.security.ShiroUser;
import org.greg.resteasy.service.AccountService;
import org.greg.resteasy.utils.Encodes;
import org.greg.resteasy.utils.constants.Securitys;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by lins on 15-12-21.
 */
//@Component
public class StandardRealm extends AuthorizingRealm {

//    @Autowired
//    private SessionDAO sessionDAO;
//    @Autowired
//    private AccountService accountService;

    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Securitys.HASH_ALGORITHM);
        matcher.setHashIterations(Securitys.HASH_INTERATIONS);

        setCredentialsMatcher(matcher);
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
//        User user = accountService.findUserByLoginName(token.getUsername());
        User user = new User();
        user.id=1L;
        user.aliasName="张三";
        user.loginName="yyw01";
        user.salt="b6682f6ff51ca51e";
        user.email="linchao@111.com.cn";
        user.hashPassword="eea645f601a395e800163c89d18241a060a2d826";

//        1,'linchao@111.com.cn','张三','yyw01','b6682f6ff51ca51e','eea645f601a395e800163c89d18241a060a2d826'
        if (user != null) {
//            if ("disabled".equals(user.status())) {
//                throw new DisabledAccountException();
//            }

            byte[] salt = Encodes.decodeHex(user.salt);
            return new SimpleAuthenticationInfo(user, user.hashPassword, ByteSource.Util.bytes(salt), getName());
        } else {
            throw new UnknownAccountException();
        }

    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
//        User user = accountService.findUserByLoginName(shiroUser.loginName);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        for (Role role : user.getRoles()) {
            // 基于Role的权限信息
            info.addRole("system");

            // 基于Permission的权限信息
//            info.addStringPermissions(Lists.newArrayList("SHOW:UNOPENED"));
//        }
        return info;
    }

}
