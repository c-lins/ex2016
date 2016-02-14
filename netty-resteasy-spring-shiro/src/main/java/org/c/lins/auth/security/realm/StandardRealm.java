package org.c.lins.auth.security.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.c.lins.auth.entity.User;
import org.c.lins.auth.security.ShiroUser;
import org.c.lins.auth.utils.Encodes;
import org.c.lins.auth.utils.constants.Securitys;

import javax.annotation.PostConstruct;

/**
 * Created by lins on 15-12-21.
 */
//@Component
public class StandardRealm extends AuthorizingRealm {

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
        user.setId(1L);
        user.setAliasName("系统管理员");
        user.setLoginName("yyw01");
        user.setSalt("b6682f6ff51ca51e");
        user.setHashPassword("eea645f601a395e800163c89d18241a060a2d826");

        if (user != null) {
//            if ("disabled".equals(user.status())) {
//                throw new DisabledAccountException();
//            }

            byte[] salt = Encodes.decodeHex(user.getSalt());
            return new SimpleAuthenticationInfo(new ShiroUser(user.getId(),user.getLoginName(),user.getAliasName()), user.getHashPassword(), ByteSource.Util.bytes(salt), getName());
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
//            info.addRole(role.roleName);
        info.addRole("system");

        // 基于Permission的权限信息
//            info.addStringPermissions(Lists.newArrayList("SHOW:UNOPENED"));
//        }
        return info;
    }

}
