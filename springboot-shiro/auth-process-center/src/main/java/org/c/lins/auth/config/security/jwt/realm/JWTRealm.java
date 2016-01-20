package org.c.lins.auth.config.security.jwt.realm;


import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.c.lins.auth.config.security.ShiroUser;
import org.c.lins.auth.config.security.jwt.JWTToken;
import org.c.lins.auth.config.security.jwt.filter.JWTAuthenticationToken;
import org.c.lins.auth.entity.Role;
import org.c.lins.auth.entity.User;
import org.c.lins.auth.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;


public class JWTRealm extends AuthorizingRealm {

    @Autowired
    private AccountService accountService;
    @Autowired
    private JWTToken tokens;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof JWTAuthenticationToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        JWTAuthenticationToken upToken = (JWTAuthenticationToken) token;
        User user = accountService.findById(Long.parseLong(upToken.getUserId()+""));

        if (user != null && tokens.validateToken(upToken.getToken())) {
            SimpleAccount account = new SimpleAccount(user, upToken.getToken(), getName());
            for (Role role : user.getRoles()) {
                // 基于Role的权限信息
                account.addRole(role.roleName);

                // 基于Permission的权限信息
//            info.addStringPermissions(Lists.newArrayList("SHOW:UNOPENED"));
            }
            return account;
        }

        return null;
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
