package org.c.lins.auth.config.security.standard.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.c.lins.auth.config.security.ShiroUser;
import org.c.lins.auth.config.security.jwt.filter.JWTAuthenticationToken;
import org.c.lins.auth.entity.Role;
import org.c.lins.auth.entity.User;
import org.c.lins.auth.service.AccountService;
import org.c.lins.auth.utils.Encodes;
import org.c.lins.auth.utils.constants.Securitys;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by lins on 15-12-21.
 */
//@Component
public class StandardRealm extends AuthorizingRealm {

//    @Autowired
//    private SessionDAO sessionDAO;
    @Autowired
    private AccountService accountService;

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
        User user = accountService.findUserByLoginName(token.getUsername());
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
        User user = accountService.findUserByLoginName(shiroUser.loginName);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for (Role role : user.getRoles()) {
            // 基于Role的权限信息
            info.addRole(role.roleName);

            // 基于Permission的权限信息
//            info.addStringPermissions(Lists.newArrayList("SHOW:UNOPENED"));
        }
        return info;
    }

}
